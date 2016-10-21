package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.AlarmListCallback;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.callback.ListItemCallback;
import com.PopCorp.Purchases.data.callback.ShareListCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.ShoppingListView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ShoppingListPresenter extends MvpPresenter<ShoppingListView> implements ListItemCallback, CreateEditListCallback, ShareListCallback, AlarmListCallback {

    private ShoppingListInteractor interactor = new ShoppingListInteractor();
    private ListItemInteractor itemsInteractor = new ListItemInteractor();

    private ShoppingList currentList;

    private String currentFilter = "";

    private ArrayList<String> shops = new ArrayList<>();
    private ArrayList<ListItem> selectedItems = new ArrayList<>();

    private boolean showSales = true;

    public ShoppingListPresenter() {
        getViewState().showProgress();
    }

    public void setListId(long listId) {
        if (currentList == null) {
            interactor.getList(listId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ShoppingList>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            AnalyticsTrackers.getInstance().sendError(e);
                            ErrorManager.printStackTrace(e);
                            getViewState().showErrorLoadingList();
                        }

                        @Override
                        public void onNext(ShoppingList list) {
                            if (list != null) {
                                currentList = list;
                                if (getObjects().size() > 0) {
                                    getViewState().showData();
                                    getViewState().filter(currentFilter);
                                } else {
                                    getViewState().showEmptyItems();
                                }
                                calculateTotals();
                                calculateShops();
                                getViewState().showTitle(currentList.getName());
                                showTapTarget();
                            } else {
                                getViewState().showErrorLoadingList();
                            }
                        }
                    });
        }
    }

    @Override
    public void onItemClicked(View view, ListItem item) {
        if (selectedItems.size() > 0) {
            changeSelectedItems(item);
        } else {
            getViewState().onItemBuyedChanged(item);
            //item.setBuyed(!item.isBuyed());
            itemsInteractor.updateItem(item);
            //getViewState().filter(currentFilter);
            calculateTotals();
        }
    }

    private void calculateTotals() {
        BigDecimal buyed = new BigDecimal("0");
        BigDecimal total = new BigDecimal("0");
        int countBuyed = 0;
        for (ListItem item : currentList.getItems()) {
            BigDecimal coast = item.getCount().multiply(item.getCoast());
            total = total.add(coast);
            if (item.isBuyed()) {
                countBuyed++;
                buyed = buyed.add(coast);
            }
        }
        getViewState().showBuyedTotals(countBuyed, buyed);
        getViewState().showTotals(getObjects().size(), total);
    }

    private void calculateShops() {
        shops.clear();
        for (ListItem item : getObjects()) {
            if (item.getShop() != null && !item.getShop().isEmpty() && !shops.contains(item.getShop())) {
                shops.add(item.getShop());
            }
        }
        if (!shops.contains(currentFilter)) {
            currentFilter = "";
        }
        if (shops.size() > 0) {
            if (shops.size() == 1 && !PreferencesManager.getInstance().isFilterListOnlyProductsOfShop()) {
                getViewState().hideShopsFilter();
                return;
            }
            getViewState().showShopsFilter(shops, currentFilter);
            return;
        }
        getViewState().hideShopsFilter();
    }

    private void changeSelectedItems(ListItem item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        if (selectedItems.size() != 0) {
            getViewState().changeItemInActionMode(selectedItems.size(), item);
        } else {
            getViewState().finishActionMode();
        }
        getViewState().filter(currentFilter);
    }

    @Override
    public void onItemLongClicked(View view, ListItem item) {
        if (selectedItems.size() > 0) {
            changeSelectedItems(item);
        } else {
            selectedItems.add(item);
            getViewState().startActionMode();
            getViewState().filter(currentFilter);
        }
    }

    @Override
    public void onEmpty() {
        if (!currentFilter.isEmpty() && !showSales){
            getViewState().showEmptyNoSaleItemsForShop(currentFilter);
        } else if (currentFilter.isEmpty() && !showSales){
            getViewState().showEmptyNoSaleItems();
        }
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

    public void onItemsRerurned(ListItem... items) {
        for (ListItem listItem : items){
            listItem.setListId(currentList.getId());
            if (getObjects().contains(listItem)){
                getObjects().remove(listItem);
                getObjects().add(listItem);
                itemsInteractor.updateItem(listItem);
            } else{
                getObjects().add(listItem);
                itemsInteractor.addItem(listItem);
            }
        }
        calculateTotals();
        calculateShops();
        if (getObjects().size() > 0){
            getViewState().showData();
            getViewState().filter(currentFilter);
            showTapTarget();
        } else{
            getViewState().showEmptyItems();
        }
    }

    public void editItem() {
        getViewState().showInputFragment(selectedItems.get(0));
        closeActionMode();
    }

    public void closeActionMode() {
        clearSelectedItems();
        getViewState().finishActionMode();
    }

    public void clearSelectedItems(){
        selectedItems.clear();
        getViewState().filter(currentFilter);
    }

    public void removeItem() {
        ArrayList<ListItem> itemsForRemove = new ArrayList<>(selectedItems);
        getObjects().removeAll(itemsForRemove);
        if (itemsForRemove.size() == 1) {
            getViewState().showItemRemoved(itemsForRemove.get(0));
        } else {
            getViewState().showItemsRemoved(itemsForRemove);
        }
        for (ListItem item : itemsForRemove) {
            itemsInteractor.removeItem(item);
        }
        calculateTotals();
        calculateShops();
        closeActionMode();
        if (getObjects().size() == 0){
            getViewState().showEmptyItems();
        }
    }

    public ArrayList<ListItem> getSelectedItems() {
        return selectedItems;
    }

    public void removeBuyed() {
        ArrayList<ListItem> itemsForRemove = new ArrayList<>();
        for (ListItem item : getObjects()) {
            if (item.isBuyed()) {
                itemsForRemove.add(item);
            }
        }
        if (itemsForRemove.size() > 0) {
            getViewState().showItemsRemoved(itemsForRemove);
            getObjects().removeAll(itemsForRemove);
            for (ListItem item : itemsForRemove) {
                itemsInteractor.removeItem(item);
            }
            calculateShops();
            calculateTotals();
            getViewState().filter(currentFilter);
            if (getObjects().size() == 0){
                getViewState().showEmptyItems();
            }
        } else {
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
        calculateTotals();
    }

    @Override
    public void addNewList(String name, String currency) {

    }

    public void removeList() {
        if (currentList.getDateTime() == 0){
            getViewState().showCantRemoveDefaultList();
            return;
        }
        interactor.removeList(currentList);
        getViewState().finish();
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

    public void onShopFilter(int id) {
        if (id == 0) {
            currentFilter = "";
        } else {
            currentFilter = shops.get(id);
        }
        getViewState().showData();
        getViewState().checkFilter(id);
        getViewState().filter(currentFilter);
    }

    public List<ListItem> getObjects() {
        return currentList.getItems();
    }

    public void addFromProducts(ArrayList<ListItem> products) {
        if (products != null) {
            ListIterator<ListItem> iterator = getObjects().listIterator();
            while (iterator.hasNext()) {
                ListItem item = iterator.next();
                if (!products.contains(item)) {
                    iterator.remove();
                    itemsInteractor.removeItem(item);
                }
            }
            for (ListItem item : products) {
                if (getObjects().contains(item)) {
                    ListItem exists = getObjects().get(getObjects().indexOf(item));
                    exists.setCount(item.getCount());
                    getObjects().remove(item);
                    getObjects().add(exists);
                    itemsInteractor.updateItem(exists);
                } else {
                    item.setListId(currentList.getId());
                    getObjects().add(item);
                    itemsInteractor.addItem(item);
                }
            }
            calculateTotals();
            calculateShops();
            if (getObjects().size() > 0){
                getViewState().showData();
                getViewState().filter(currentFilter);
                showTapTarget();
            } else{
                getViewState().showEmptyItems();
            }
        }
    }

    @Override
    public void onItemSaleClicked(View v, ListItemSale sale) {
        if (sale.getSaleId() == 0){
            getViewState().onItemSaleClicked(v, sale);
        } else {
            if (sale.getSaleId() > 999999) {
                getViewState().openSkidkaonlineSale(sale.getSaleId());
            } else {
                getViewState().openSale(sale.getSaleId());
            }
        }
    }

    public void showTapTarget() {
        /*if (!PreferencesManager.getInstance().isTapTargetForItemsCreateShown()){
            getViewState().showTapTargetForCreate();
            PreferencesManager.getInstance().putTapTargetForItemsCreate(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForItemsAddShown()){
            getViewState().showTapTargetForAdd();
            PreferencesManager.getInstance().putTapTargetForItemsAdd(true);
            return;
        }
        if (shops.size() > 0 && !(shops.size() == 1 && !PreferencesManager.getInstance().isFilterListOnlyProductsOfShop())) {
            if (!PreferencesManager.getInstance().isTapTargetForItemsFilterByShopShown()){
                getViewState().showTapTargetForItemsFilterForShop();
                PreferencesManager.getInstance().putTapTargetForItemsFilterByShop(true);
                return;
            }
        }
        if (getObjects().size() > 0){
            if (!PreferencesManager.getInstance().isTapTargetForItemInfoShown()){
                getViewState().showTapTargetForItemInfo();
                PreferencesManager.getInstance().putTapTargetForItemInfo(true);
                return;
            }
        }*/
    }

    public void showTapTargetForActionMode() {
        /*if (!PreferencesManager.getInstance().isTapTargetForItemsAddToActionModeShown()){
            getViewState().showTapTargetForItemsAddToActionMode();
            PreferencesManager.getInstance().putTapTargetForItemsAddToActionMode(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForItemEditInActionModeShown()){
            getViewState().showTapTargetForItemEditInActionMode();
            PreferencesManager.getInstance().putTapTargetForItemEditInActionMode(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForItemsRemoveInActionModeShown()){
            getViewState().showTapTargetForItemsRemoveInActionMode();
            PreferencesManager.getInstance().putTapTargetForItemsRemoveInActionMode(true);
            return;
        }*/
    }

    public void showSales(boolean checked) {
        showSales = checked;
        getViewState().showData();
        getViewState().checkShowSales(checked);
        getViewState().filter(currentFilter);
    }

    public boolean getShowSales() {
        return showSales;
    }

    public boolean isShowSales() {
        return showSales;
    }
}
