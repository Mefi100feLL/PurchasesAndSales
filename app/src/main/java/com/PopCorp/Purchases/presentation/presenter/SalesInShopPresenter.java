package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.CategoryComparator;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.CategoryInteractor;
import com.PopCorp.Purchases.domain.interactor.SaleInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.SalesInShopView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SalesInShopPresenter extends MvpPresenter<SalesInShopView> implements RecyclerCallback<Sale> {

    private CategoryInteractor categoryInteractor = new CategoryInteractor();
    private SaleInteractor interactor = new SaleInteractor();

    private ArrayList<Sale> objects = new ArrayList<>();

    private Shop currentShop;

    private ArrayList<Category> favoriteCategories = new ArrayList<>();
    private ArrayList<Category> allCategories = new ArrayList<>();
    private ArrayList<Category> filterCategories = new ArrayList<>();

    private String currentFilter = "";
    private int filterPosition = 0;

    public SalesInShopPresenter() {
        getViewState().showProgress();
    }

    public void loadData() {
        int regionId = Integer.valueOf(PreferencesManager.getInstance().getRegionId());
        int[] shops = new int[]{currentShop.getId()};
        int[] categories = new int[favoriteCategories.size()];
        int[] types = new int[favoriteCategories.size()];
        for (int i = 0; i < favoriteCategories.size(); i++) {
            categories[i] = favoriteCategories.get(i).getId();
            types[i] = favoriteCategories.get(i).getType();
        }
        interactor.getData(regionId, shops, categories, types)
                .subscribe(new Observer<List<Sale>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        e.printStackTrace();
                        if (objects.size() == 0){
                            getViewState().showErrorLoadingSales(e);
                        } else{
                            getViewState().showSnackBar(ErrorManager.getErrorResource(e));
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
                        }
                    }
                });
    }

    public void setCurrentShop(Shop shop) {
        if (shop != null && currentShop == null) {
            currentShop = shop;
            categoryInteractor.loadFromDB()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Category>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                            e.printStackTrace();
                            getViewState().showCategoriesEmpty();
                        }

                        @Override
                        public void onNext(List<Category> categories) {
                            if (categories.size() == 0) {
                                getViewState().showCategoriesEmpty();
                            } else {
                                allCategories.addAll(categories);
                                for (Category category : categories) {
                                    if (category.isFavorite()) {
                                        favoriteCategories.add(category);
                                    }
                                }
                                if (favoriteCategories.size() == 0) {
                                    getViewState().showFavoriteCategoriesEmpty();
                                } else {
                                    loadData();
                                }
                            }
                        }
                    });
        }
    }

    public void onRefresh() {
        if (favoriteCategories.size() != 0) {
            getViewState().refreshing(true);
            loadData();
        }
    }

    public void selectCategories() {
        if (allCategories.size() == 0) {
            getViewState().showProgress();
            categoryInteractor.loadFromNet()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Category>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                            e.printStackTrace();
                            getViewState().showCategoriesEmpty();
                        }

                        @Override
                        public void onNext(List<Category> categories) {
                            getViewState().showCategoriesEmpty();
                            allCategories.addAll(categories);
                            getViewState().showCategoriesForSelectingFavorites(allCategories);
                        }
                    });
        } else {
            getViewState().showCategoriesForSelectingFavorites(allCategories);
        }
    }

    public void onFavoriteCategoriesSelected(List<Category> categories) {
        favoriteCategories.addAll(categories);
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

    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    private void initFilters() {
        boolean added = false;
        for (Sale sale : objects) {
            if (!filterCategories.contains(sale.getCategory())) {
                filterCategories.add(sale.getCategory());
                added = true;
            }
        }
        Collections.sort(filterCategories, new CategoryComparator());
        if (added && filterPosition != 0){
            for (Category category : filterCategories){
                if (category.getName().equals(currentFilter)){
                    filterPosition = filterCategories.indexOf(category) + 1;
                }
            }
        }
        if (filterCategories.size() > 1) {
            getViewState().showSpinner();
            getViewState().selectSpinner(filterPosition);
        }
    }

    public ArrayList<String> getFilterStrings() {
        ArrayList<String> names = new ArrayList<>();
        for (Category category : filterCategories) {
            names.add(category.getName());
        }
        return names;
    }

    public void onFilter(int position) {
        filterPosition = position;
        if (position == 0) {
            currentFilter = "";
        } else {
            currentFilter = filterCategories.get(position - 1).getName();
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
        return currentShop.getName();
    }
}
