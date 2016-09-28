package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.FavoriteRecyclerCallback;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.RegionInteractor;
import com.PopCorp.Purchases.domain.interactor.ShopsInteractor;
import com.PopCorp.Purchases.presentation.view.adapter.ShopsAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.ShopsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ShopsPresenter extends MvpPresenter<ShopsView> implements FavoriteRecyclerCallback<Shop> {

    private ShopsInteractor interactor = new ShopsInteractor();
    private RegionInteractor regionsInteractor = new RegionInteractor();

    private ArrayList<Shop> objects = new ArrayList<>();

    private String currentFilter = ShopsAdapter.FILTER_ALL;

    public ShopsPresenter() {
        if (isRegionEmpty()) {
            getViewState().showRegionEmpty();
        } else {
            getViewState().showProgress();
            loadFromDB();
        }
    }

    private void loadFromDB() {
        interactor.loadFromDB(Integer.parseInt(PreferencesManager.getInstance().getRegionId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Shop>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        loadFromNetwork();
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        if (shops != null && shops.size() != 0) {
                            objects.addAll(shops);
                            boolean isFavorite = findFavoriteShops(shops);
                            getViewState().showData();
                            getViewState().selectSpinner(isFavorite ? 1 : 0);
                        }
                        loadFromNetwork();
                    }
                });
    }

    private boolean findFavoriteShops(List<Shop> shops) {
        boolean result = false;
        for (Shop shop : shops) {
            if (shop.isFavorite()) {
                result = true;
                currentFilter = ShopsAdapter.FILTER_FAVORITE;
                break;
            }
        }
        return result;
    }

    private void loadFromNetwork() {
        interactor.loadFromNet(Integer.parseInt(PreferencesManager.getInstance().getRegionId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Shop>>() {
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
                    public void onNext(List<Shop> shops) {
                        getViewState().refreshing(false);
                        if (shops != null) {
                            if (shops.size() == 0) {
                                removeAllShops();
                            } else {
                                ArrayList<Shop> newShops = findNewShops(shops);
                                //removeNotExistsShops(shops);
                                getViewState().showData();
                                getViewState().filter(currentFilter);
                                if (newShops.size() > 1) {
                                    getViewState().showSnackBarWithNewShops(newShops.size(), currentFilter.equals(ShopsAdapter.FILTER_FAVORITE));
                                } else if (newShops.size() == 1) {
                                    getViewState().showSnackBarWithNewShop(newShops.get(0), currentFilter.equals(ShopsAdapter.FILTER_FAVORITE));
                                }
                            }
                        }
                    }
                });
    }

    private void removeNotExistsShops(List<Shop> shops) {
        ListIterator<Shop> iterator = objects.listIterator();
        while (iterator.hasNext()) {
            Shop shop = iterator.next();
            if (!shops.contains(shop)) {
                interactor.remove(shop);
                iterator.remove();
            }
        }
    }

    private ArrayList<Shop> findNewShops(List<Shop> shops) {
        ArrayList<Shop> result = new ArrayList<>();
        for (Shop shop : shops) {
            if (!objects.contains(shop)) {
                result.add(shop);
                objects.add(shop);
            } else {
                Shop exist = objects.get(objects.indexOf(shop));
                exist.setImageUrl(shop.getImageUrl());
                exist.setCountSales(shop.getCountSales());
                exist.setName(shop.getName());
            }
        }
        return result;
    }

    private void removeAllShops() {
        for (Shop shop : objects) {
            interactor.remove(shop);
        }
        objects.clear();
        getViewState().showShopsEmpty();
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
            selectSpinner(position);
        }
    }

    public void selectSpinner(int position) {
        getViewState().selectSpinner(position);
    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onItemClicked(View view, Shop item) {
        getViewState().openShop(view, item);
    }

    @Override
    public void onItemLongClicked(View view, Shop item) {

    }

    @Override
    public void onEmpty() {
        if (!isRegionEmpty()) { // чтобы не убрать empty с невыбранным регионом
            if (currentFilter.equals(ShopsAdapter.FILTER_ALL)) {
                getViewState().showShopsEmpty();
            } else {
                getViewState().showFavoriteShopsEmpty();
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
                        getViewState().showSnackBar(e);
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                    }

                    @Override
                    public void onNext(List<Region> regions) {
                        getViewState().showRegionEmpty();
                        if (regions.size() != 0) {
                            getViewState().showSelectingRegions(regions);
                        } else {
                            getViewState().showEmptyRegions();
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

    private boolean isRegionEmpty() {
        return PreferencesManager.getInstance().getRegionId().isEmpty();
    }

    public ArrayList<Shop> getObjects() {
        return objects;
    }

    @Override
    public void onFavoriteClicked(Shop item) {
        item.setFavorite(!item.isFavorite());
        interactor.update(item);
        getViewState().filter(currentFilter);
    }
}
