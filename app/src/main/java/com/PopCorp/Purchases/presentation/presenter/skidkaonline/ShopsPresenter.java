package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.view.View;

import com.PopCorp.Purchases.data.callback.FavoriteRecyclerCallback;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.ShopInteractor;
import com.PopCorp.Purchases.presentation.view.adapter.ShopsAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.skidkaonline.ShopAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.ShopsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class ShopsPresenter extends MvpPresenter<ShopsView> implements FavoriteRecyclerCallback<Shop> {

    private ShopInteractor interactor = new ShopInteractor();

    private ArrayList<Shop> objects = new ArrayList<>();

    private String currentFilter = ShopAdapter.FILTER_ALL;

    public ShopsPresenter() {
        if (isCityEmpty()) {
            getViewState().showRegionEmpty();
        } else {
            getViewState().showProgress();
            loadFromDB();
        }
    }

    private void loadFromDB() {
        interactor.loadFromDB(Integer.parseInt(PreferencesManager.getInstance().getCity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Shop>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showSnackBar(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        if (shops.size() != 0) {
                            objects.addAll(shops);
                            boolean isFavorite = false;
                            for (Shop shop : shops) {
                                if (shop.isFavorite()) {
                                    isFavorite = true;
                                    currentFilter = ShopAdapter.FILTER_FAVORITE;
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

    private void loadFromNetwork() {
        interactor.loadFromNet(Integer.parseInt(PreferencesManager.getInstance().getCity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Shop>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().refreshing(false);
                        getViewState().showSnackBar(e);
                        e.printStackTrace();
                        if (objects.size() == 0) {
                            getViewState().showShopsEmpty();
                        }
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        getViewState().refreshing(false);
                        if (shops.size() == 0) {
                            if (objects.size() == 0) {
                                getViewState().showShopsEmpty();
                            }
                        } else {
                            ArrayList<Shop> newShops = new ArrayList<>();
                            for (Shop shop : shops) {
                                if (!objects.contains(shop)) {
                                    newShops.add(shop);
                                    objects.add(shop);
                                }
                            }
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                            if (newShops.size() > 1) {
                                getViewState().showSnackBarWithNewShops(newShops.size(), currentFilter.equals(ShopAdapter.FILTER_FAVORITE));
                            } else if (newShops.size() == 1) {
                                getViewState().showSnackBarWithNewShop(newShops.get(0), currentFilter.equals(ShopAdapter.FILTER_FAVORITE));
                            }
                        }
                    }
                });
    }

    public void onRefresh() {
        if (!isCityEmpty()) {
            getViewState().refreshing(true);
            loadFromNetwork();
        }
    }

    public void onFilter(int position) {
        if (objects.size() > 0) {
            if (position == 0) {
                currentFilter = ShopAdapter.FILTER_ALL;
            } else {
                currentFilter = ShopAdapter.FILTER_FAVORITE;
            }
            getViewState().showData();
            getViewState().filter(currentFilter);
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
        if (!isCityEmpty()) { // чтобы не убрать empty с невыбранным регионом
            if (currentFilter.equals(ShopAdapter.FILTER_ALL)) {
                getViewState().showShopsEmpty();
            } else {
                getViewState().showFavoriteShopsEmpty();
            }
        }
    }

    public void tryAgainLoadShops() {
        getViewState().showProgress();
        loadFromDB();
    }

    private boolean isCityEmpty() {
        return PreferencesManager.getInstance().getCity().isEmpty();
    }

    public ArrayList<Shop> getObjects() {
        return objects;
    }

    @Override
    public void onFavoriteClicked(Shop item) {
        item.setFavorite(!item.isFavorite());
        getViewState().filter(currentFilter);
        interactor.update(item);
    }
}
