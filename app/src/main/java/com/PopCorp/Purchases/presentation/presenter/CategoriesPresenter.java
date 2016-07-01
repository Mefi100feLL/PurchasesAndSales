package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.CategoryInteractor;
import com.PopCorp.Purchases.domain.interactor.RegionInteractor;
import com.PopCorp.Purchases.presentation.view.adapter.CategoriesAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.ShopsAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.CategoriesView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class CategoriesPresenter extends MvpPresenter<CategoriesView> implements RecyclerCallback<Category> {

    private CategoryInteractor interactor = new CategoryInteractor();
    private RegionInteractor regionsInteractor = new RegionInteractor();

    private ArrayList<Category> objects = new ArrayList<>();

    private String currentFilter = CategoriesAdapter.FILTER_ALL;

    public CategoriesPresenter() {
        if (isRegionEmpty()) {
            getViewState().showRegionEmpty();
        } else {
            getViewState().showProgress();
            loadFromDB();
        }
    }

    private void loadFromDB(){
        interactor.loadFromDB()
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
                        loadFromNetwork();
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        if (categories.size() != 0){
                            objects.addAll(categories);
                            boolean isFavorite = false;
                            for (Category category : categories){
                                if (category.isFavorite()){
                                    isFavorite = true;
                                    currentFilter = CategoriesAdapter.FILTER_FAVORITE;
                                    break;
                                }
                            }
                            getViewState().showData();
                            getViewState().selectSpinner(isFavorite ? 1 : 0);
                        }
                        loadFromNetwork();
                    }
                });
    }

    private void loadFromNetwork(){
        interactor.loadFromNet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Category>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                        e.printStackTrace();
                        if (objects.size() == 0) {
                            getViewState().showCategoriesEmpty();
                        }
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        getViewState().refreshing(false);
                        if (categories.size() == 0) {
                            if (objects.size() == 0) {
                                getViewState().showCategoriesEmpty();
                            }
                        } else {
                            ArrayList<Category> newShops = new ArrayList<>();
                            for (Category category : categories){
                                if (!objects.contains(category)){
                                    newShops.add(category);
                                    objects.add(category);
                                }
                            }
                            if (newShops.size() > 0) {
                                getViewState().showData();
                                getViewState().filter(currentFilter);
                                if (newShops.size() > 1) {
                                    getViewState().showSnackBarWithNewCategories(newShops.size(), currentFilter.equals(ShopsAdapter.FILTER_FAVORITE));
                                } else if (newShops.size() == 1) {
                                    getViewState().showSnackBarWithNewCategory(newShops.get(0), currentFilter.equals(ShopsAdapter.FILTER_FAVORITE));
                                }
                            }
                        }
                    }
                });
    }

    public void onRefresh() {
        if (!isRegionEmpty()) {
            getViewState().refreshing(true);
            loadFromNetwork();
        }
    }

    public void onFilter(int position) {
        if (objects.size() > 0) {
            if (position == 0) {
                currentFilter = ShopsAdapter.FILTER_ALL;
            } else {
                currentFilter = ShopsAdapter.FILTER_FAVORITE;
            }
            getViewState().showData();
            getViewState().filter(currentFilter);
        }
    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onItemClicked(View view, Category item) {
        getViewState().openCategory(view, item);
    }

    @Override
    public void onItemLongClicked(View view, Category item) {

    }

    @Override
    public void onEmpty() {
        if (!PreferencesManager.getInstance().getRegionId().isEmpty()) { // чтобы не убрать empty с невыбранным регионом
            if (currentFilter.equals(ShopsAdapter.FILTER_ALL)) {
                getViewState().showCategoriesEmpty();
            } else {
                getViewState().showFavoriteCategoriesEmpty();
            }
        }
    }

    public void loadRegions() {
        getViewState().showProgress();
        regionsInteractor.getData()
                .subscribe(new Observer<List<Region>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showSnackBar(ErrorManager.getErrorResource(e));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Region> regions) {
                        getViewState().showRegionEmpty();
                        if (regions.size() != 0) {
                            getViewState().showSelectingRegions(regions);
                        }
                    }
                });
    }

    public void onRegionSelected() {
        getViewState().showProgress();
        loadFromDB();
    }

    public void tryAgainLoadShops() {
        getViewState().showProgress();
        loadFromDB();
    }

    private boolean isRegionEmpty(){
        return PreferencesManager.getInstance().getRegionId().isEmpty();
    }

    public ArrayList<Category> getObjects() {
        return objects;
    }


}
