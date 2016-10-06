package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.AlarmListCallback;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.callback.ShareListCallback;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ShoppingListsPresenter extends MvpPresenter<ShoppingListsView> implements CreateEditListCallback, ShareListCallback, AlarmListCallback {

    private ShoppingListInteractor interactor = new ShoppingListInteractor();

    private ArrayList<ShoppingList> objects = new ArrayList<>();

    public ShoppingListsPresenter() {
        getViewState().showProgress();
        loadData();
    }

    public void loadData() {
        interactor.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ShoppingList>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showSnackBar(e);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                    }

                    @Override
                    public void onNext(List<ShoppingList> shoppingLists) {
                        if (shoppingLists.size() > 0) {
                            for (ShoppingList list : shoppingLists){
                                if (!objects.contains(list)){
                                    objects.add(list);
                                } else{
                                    objects.remove(list);
                                    objects.add(list);
                                }
                            }
                            ArrayList<ShoppingList> removedLists = new ArrayList<>();
                            for (ShoppingList list : objects){
                                if (!shoppingLists.contains(list)){
                                    removedLists.add(list);
                                }
                            }
                            objects.removeAll(removedLists);
                            if (removedLists.size() == 1) {
                                getViewState().showRemovedList(removedLists.get(0));
                            } else if (removedLists.size() > 1){
                                getViewState().showRemovedLists(removedLists);
                            }
                            getViewState().showData();
                        } else {
                            getViewState().showEmptyLists();
                        }
                        showTapTarget();
                    }
                });
    }

    public ArrayList<ShoppingList> getObjects() {
        return objects;
    }

    public void createNewList() {
        getViewState().showDialogForNewList();
    }

    @Override
    public void onListEdited(ShoppingList list, String name, String currency) {
        list.setName(name);
        list.setCurrency(currency);
        interactor.saveList(list);
        getViewState().showData();
    }

    @Override
    public void addNewList(String name, String currency) {
        Calendar calendar = Calendar.getInstance();
        ShoppingList list = new ShoppingList(-1, name, calendar.getTimeInMillis(), 0, currency);
        interactor.addNewShoppingList(list);
        objects.add(list);
        getViewState().showData();
        getViewState().openShoppingList(list);
    }

    public void removeList(ShoppingList list) {
        objects.remove(list);
        interactor.removeList(list);
        if (objects.size() > 0) {
            getViewState().showData();
        } else {
            getViewState().showEmptyLists();
        }
        getViewState().showRemovedList(list);
    }

    @Override
    public void setAlarm(ShoppingList list, Calendar date) {
        list.setAlarm(date.getTimeInMillis());
        interactor.saveList(list);
    }

    @Override
    public void removeAlarm(ShoppingList list) {
        list.setAlarm(0);
        interactor.saveList(list);
    }

    @Override
    public void shareAsSMS(ShoppingList list) {
        getViewState().shareListAsSMS(list);
    }

    @Override
    public void shareAsEmail(ShoppingList list) {
        getViewState().shareListAsEmail(list);
    }

    @Override
    public void shareAsText(ShoppingList list) {
        getViewState().shareListAsText(list);
    }

    public void returnList(ShoppingList list) {
        objects.add(list);
        interactor.addNewShoppingList(list);
        getViewState().showData();
    }

    public void returnLists(ArrayList<ShoppingList> removedLists) {
        objects.addAll(removedLists);
        for (ShoppingList list : removedLists) {
            interactor.addNewShoppingList(list);
        }
        getViewState().showData();
    }

    public void showTapTarget() {
        if (!PreferencesManager.getInstance().isTapTargetForListsCreateShown()){
            getViewState().showTapTargetForCreate();
            PreferencesManager.getInstance().putTapTargetForListsCreate(true);
            return;
        }
    }
}
