package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.AlarmListCallback;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.callback.ShareListCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListView;
import com.afollestad.materialcab.MaterialCab;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ShoppingListPresenter extends MvpPresenter<ShoppingListView> implements RecyclerCallback<ListItem>, CreateEditListCallback, ShareListCallback, AlarmListCallback {

    private ShoppingListInteractor interactor = new ShoppingListInteractor();
    private ListItemInteractor itemsInteractor = new ListItemInteractor();

    private ShoppingList currentList;
    private String currentFilter = "";

    private final ArrayList<ListItem> selectedItems = new ArrayList<>();

    public ShoppingListPresenter() {
        getViewState().showProgress();
    }

    public void setListId(long listId) {
        interactor.getList(listId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShoppingList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShoppingList list) {
                        if (list != null) {
                            currentList = list;
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                            getViewState().showTitle(currentList.getName());
                        }
                    }
                });
    }

    public List<ListItem> getObjects() {
        return currentList.getItems();
    }

    @Override
    public void onItemClicked(View view, ListItem item) {
        if (selectedItems.size() > 0) {
            changeSelectedItems(item);
        } else {
            item.setBuyed(!item.isBuyed());
            itemsInteractor.updateItem(item);
            getViewState().filter(currentFilter);
        }
    }

    private void changeSelectedItems(ListItem item){
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        if (selectedItems.size() != 0) {
            getViewState().changeItemInActionMode(selectedItems.size(), item);
        } else{
            getViewState().finishActionMode();
        }
    }

    @Override
    public void onItemLongClicked(View view, ListItem item) {
        if (selectedItems.size() > 0) {
            changeSelectedItems(item);
        } else {
            selectedItems.add(item);
            getViewState().startActionMode();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    public String getCurrency() {
        return currentList.getCurrency();
    }

    public ShoppingList getList() {
        return currentList;
    }

    public void onItemsRerurned(ListItem... item) {
        if (currentList.getItems().contains(item)) {
            currentList.getItems().remove(item);
        }
        Collections.addAll(currentList.getItems(), item);
        itemsInteractor.addItems(item);
        if (currentList.getItems().size() == 1) {
            getViewState().showData();
        } else {
            getViewState().filter(currentFilter);
        }
    }

    public void editItem() {
        getViewState().showInputFragment(selectedItems.get(0));
        closeActionMode();
    }

    public void closeActionMode(){
        selectedItems.clear();
        getViewState().finishActionMode();
        getViewState().updateAllItems();
        getViewState().filter(currentFilter);
    }

    public void removeItem() {
        ArrayList<ListItem> itemsForRemove = new ArrayList<>(selectedItems);
        getObjects().removeAll(itemsForRemove);
        if (itemsForRemove.size() == 1){
            getViewState().showItemRemoved(itemsForRemove.get(0));
        } else{
            getViewState().showItemsRemoved(itemsForRemove);
        }
        for (ListItem item : itemsForRemove){
            itemsInteractor.removeItem(item);
        }
        closeActionMode();
    }

    public ArrayList<ListItem> getSelectedItems() {
        return selectedItems;
    }

    public void removeBuyed() {
        ArrayList<ListItem> itemsForRemove = new ArrayList<>();
        for (ListItem item : getObjects()){
            if (item.isBuyed()){
                itemsForRemove.add(item);
            }
        }
        if (itemsForRemove.size() > 0){
            getViewState().showItemsRemoved(itemsForRemove);
            getObjects().removeAll(itemsForRemove);
            for (ListItem item : itemsForRemove){
                itemsInteractor.removeItem(item);
            }
            getViewState().filter(currentFilter);
        } else{
            getViewState().showNothingRemoving();
        }
    }

    @Override
    public void onListEdited(ShoppingList list, String name, String currency) {
        currentList.setName(name);
        currentList.setCurrency(currency);
        interactor.saveList(currentList);
        getViewState().showTitle(name);
        getViewState().updateCurrency(currency);
    }

    @Override
    public void addNewList(String name, String currency) {

    }

    public void removeList() {
        interactor.removeList(currentList);
        getViewState().finish();
    }

    @Override
    public void shareAsSMS(ShoppingList list) {

    }

    @Override
    public void shareAsEmail(ShoppingList list) {

    }

    @Override
    public void shareAsText(ShoppingList list) {

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
}
