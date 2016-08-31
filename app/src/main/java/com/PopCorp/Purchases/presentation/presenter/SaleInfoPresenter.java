package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.mapper.SaleToListItemMapper;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.SameSale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.SaleInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SaleInfoView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleInfoPresenter extends MvpPresenter<SaleInfoView> implements CreateEditListCallback, RecyclerCallback<SameSale> {

    public static final String PRESENTER_ID = "SaleInfoPresenter";

    private SaleInteractor interactor = new SaleInteractor();
    private ShoppingListInteractor listInteractor = new ShoppingListInteractor();
    private ListItemInteractor listItemInteractor = new ListItemInteractor();

    private Sale sale;
    private List<ShoppingList> lists = new ArrayList<>();
    private ArrayList<ShoppingList> selectedLists = new ArrayList<>();

    public void setSale(int saleId) {
        if (sale == null) {
            interactor.getSale(Integer.valueOf(PreferencesManager.getInstance().getRegionId()), saleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Sale>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ErrorManager.printStackTrace(e);
                            getViewState().showError(e);
                        }

                        @Override
                        public void onNext(Sale result) {
                            if (result != null) {
                                sale = result;
                                getViewState().showInfo(sale);
                            } else {
                                getViewState().showSaleEmpty();
                            }
                        }
                    });
        }
    }

    public void loadShoppingLists() {
        listInteractor.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ShoppingList>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorManager.printStackTrace(e);
                        getViewState().showErrorLoadingLists(e);
                    }

                    @Override
                    public void onNext(List<ShoppingList> shoppingLists) {
                        if (shoppingLists != null && shoppingLists.size() > 0) {
                            Collections.sort(shoppingLists, PreferencesManager.getInstance().getShoppingListComparator());
                            lists = shoppingLists;
                            getViewState().showListsSelecting(shoppingLists);
                        } else {
                            getViewState().showEmptyLists();
                        }
                    }
                });
    }

    @Override
    public void onListEdited(ShoppingList list, String name, String currency) {

    }

    @Override
    public void addNewList(String name, String currency) {
        Calendar calendar = Calendar.getInstance();
        ShoppingList list = new ShoppingList(-1, name, calendar.getTimeInMillis(), 0, currency);
        listInteractor.addNewShoppingList(list);
        selectedLists.add(list);
        openInputListItem();
    }

    private void openInputListItem() {
        ListItem item = SaleToListItemMapper.getListItem(sale);
        long[] ids = new long[selectedLists.size()];
        for (int i = 0; i < selectedLists.size(); i++) {
            ids[i] = selectedLists.get(i).getId();
        }
        getViewState().openInputListItemFragment(item, ids);
    }

    public void listsSelected(Integer[] which) {
        selectedLists.clear();
        for (int i : which) {
            selectedLists.add(lists.get(i));
        }
        openInputListItem();
    }

    public void onItemsRerurned(ListItem item) {
        for (ShoppingList list : selectedLists) {
            item.setListId(list.getId());
            listItemInteractor.addItem(item);
        }
        getViewState().showItemAdded();
    }

    public Sale getSale() {
        return sale;
    }

    @Override
    public void onItemClicked(View view, SameSale item) {
        getViewState().openSameSale(view, item.getSaleId());
    }

    @Override
    public void onItemLongClicked(View view, SameSale item) {

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
}