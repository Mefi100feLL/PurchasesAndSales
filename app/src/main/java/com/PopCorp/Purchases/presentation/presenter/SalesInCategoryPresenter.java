package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.FavoriteRecyclerCallback;
import com.PopCorp.Purchases.data.comparator.ShopComparator;
import com.PopCorp.Purchases.data.mapper.SaleTOListItemMapper;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.SaleInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.domain.interactor.ShopsInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SalesInCategoryView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SalesInCategoryPresenter extends MvpPresenter<SalesInCategoryView> implements FavoriteRecyclerCallback<Sale> {

    private ShopsInteractor shopsInteractor = new ShopsInteractor();
    private SaleInteractor interactor = new SaleInteractor();
    private ShoppingListInteractor listInteractor = new ShoppingListInteractor();
    private ListItemInteractor listItemInteractor = new ListItemInteractor();

    private Category currentCategory;

    private ArrayList<Sale> objects = new ArrayList<>();

    private ArrayList<Shop> favoriteShops = new ArrayList<>();
    private ArrayList<Shop> allShops = new ArrayList<>();
    private ArrayList<Shop> filterShops = new ArrayList<>();

    private String currentFilter = "";
    private int filterPosition = 0;

    public SalesInCategoryPresenter() {
        getViewState().showProgress();
    }

    public void setCurrentCategory(Category currentCategory) {
        if (currentCategory != null) {
            this.currentCategory = currentCategory;
            loadShops();
        }
    }

    public void loadShops() {
        shopsInteractor.getData(Integer.valueOf(PreferencesManager.getInstance().getRegionId()))
                .subscribe(new Observer<List<Shop>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (allShops.size() == 0) {
                            getViewState().showError(e);
                        } else {
                            getViewState().showSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        if (shops.size() == 0) {
                            getViewState().showShopsEmpty();
                        } else {
                            if (allShops.size() == 0) {
                                allShops.addAll(shops);
                                for (Shop shop : shops) {
                                    if (shop.isFavorite()) {
                                        favoriteShops.add(shop);
                                    }
                                }
                                if (favoriteShops.size() == 0) {
                                    getViewState().showFavoriteShopsEmpty();
                                } else {
                                    loadData();
                                }
                            }
                        }
                    }
                });
    }

    public void loadData() {
        int regionId = Integer.valueOf(PreferencesManager.getInstance().getRegionId());
        int[] shops = new int[favoriteShops.size()];
        for (int i = 0; i < favoriteShops.size(); i++) {
            shops[i] = favoriteShops.get(i).getId();
        }
        int[] categories = new int[]{currentCategory.getId()};
        int[] types = new int[]{currentCategory.getType()};
        interactor.getData(regionId, shops, categories, types)
                .subscribe(new Observer<List<Sale>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (objects.size() == 0) {
                            getViewState().showError(e);
                        } else {
                            getViewState().showSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(List<Sale> sales) {
                        getViewState().refreshing(false);
                        if (sales.size() == 0) {
                            getViewState().showSalesEmpty();
                        } else {
                            objects.clear();
                            objects.addAll(sales);
                            initFilters();
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                            showTapTarget();
                        }
                    }
                });
    }

    private void initFilters() {
        boolean added = false;
        for (Sale sale : objects) {
            if (!filterShops.contains(sale.getShop())) {
                filterShops.add(sale.getShop());
                added = true;
            }
        }
        Collections.sort(filterShops, new ShopComparator());
        if (added && filterPosition != 0){
            for (Shop shop : filterShops){
                if (shop.getName().equals(currentFilter)){
                    filterPosition = filterShops.indexOf(shop) + 1;
                }
            }
        }
        if (!currentFilter.startsWith("query") && filterShops.size() > 1) {
            getViewState().showSpinner();
            getViewState().selectSpinner(filterPosition);
        }
    }

    public void onRefresh() {
        if (favoriteShops.size() != 0) {
            getViewState().refreshing(true);
            loadData();
        }
    }

    public void selectShops() {
        if (allShops.size() == 0) {
            getViewState().showProgress();
            loadShops();
        } else {
            getViewState().showShopsForSelectingFavorites(allShops);
        }
    }

    public void onFavoriteShopsSelected(List<Shop> shops) {
        favoriteShops.addAll(shops);
        getViewState().showProgress();
        loadData();
    }

    @Override
    public void onItemClicked(View view, Sale item) {
        getViewState().showSales(view, item);
    }

    @Override
    public void onItemLongClicked(View view, Sale item) {

    }

    @Override
    public void onEmpty() {
        if (currentFilter.startsWith("query")){
            getViewState().showEmptyForSearch(currentFilter.replace("query=", ""));
        }
    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    public ArrayList<String> getFilterStrings() {
        ArrayList<String> names = new ArrayList<>();
        for (Shop shop : filterShops) {
            names.add(shop.getName());
        }
        return names;
    }

    public void onFilter(int position) {
        filterPosition = position;
        if (position == 0) {
            currentFilter = "";
        } else {
            currentFilter = filterShops.get(position - 1).getName();
        }
        getViewState().filter(currentFilter);
    }

    public void tryAgain() {
        getViewState().showProgress();
        loadData();
    }

    public ArrayList<Sale> getObjects() {
        return objects;
    }

    public String getTitle() {
        return currentCategory.getName();
    }

    public void search(String query) {
        if (query.isEmpty()) {
            getViewState().showSpinner();
            currentFilter = "";
        } else {
            getViewState().hideSpinner();
            currentFilter = "query=" + query;
        }
        getViewState().showData();
        getViewState().filter(currentFilter);
    }

    public void showTapTarget() {
        /*if (!currentFilter.startsWith("query") && filterShops.size() > 1) {
            if (!PreferencesManager.getInstance().isTapTargetForSalesFilterByShopsShown()) {
                getViewState().showTapTargetForFilter();
                PreferencesManager.getInstance().putTapTargetForSalesByShopsFilter(true);
                return;
            }
        }
        if (!PreferencesManager.getInstance().isTapTargetForSalesSearchShown()) {
            getViewState().showTapTargetForSalesSearch();
            PreferencesManager.getInstance().putTapTargetForSalesSearch(true);
            return;
        }*/
    }

    @Override
    public void onFavoriteClicked(Sale sale) {
        if (sale.isFavorite()){
            listItemInteractor.removeWithSaleIdFromList(listInteractor.getDefaultList().getId(), sale.getId());
        } else {
            ListItem item = SaleTOListItemMapper.getListItem(sale);
            item.setListId(listInteractor.getDefaultList().getId());
            listItemInteractor.addItem(item);
        }
        sale.setFavorite(!sale.isFavorite());
    }

    public void refreshFavorites() {
        interactor.refreshFavorites(objects)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                    }

                    @Override
                    public void onNext(Boolean edited) {
                        if (edited){
                            getViewState().update();
                        }
                    }
                });
    }
}
