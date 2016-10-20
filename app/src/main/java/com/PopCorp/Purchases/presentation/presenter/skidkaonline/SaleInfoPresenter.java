package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.graphics.Bitmap;
import android.view.View;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.mapper.SaleTOListItemMapper;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleInteractor;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.ShopInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleInfoView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SaleInfoPresenter extends MvpPresenter<SaleInfoView> implements CreateEditListCallback {

    public static final String PRESENTER_ID = "SaleInfoPresenter";

    private SaleInteractor interactor = new SaleInteractor();
    private ShoppingListInteractor listInteractor = new ShoppingListInteractor();
    private ListItemInteractor listItemInteractor = new ListItemInteractor();

    private Sale sale;
    private boolean editMode;

    public List<ShoppingList> lists = new ArrayList<>();
    private ArrayList<ShoppingList> selectedLists = new ArrayList<>();

    public SaleInfoPresenter() {
        getViewState().showProgress();
    }

    public void setSale(int saleId, boolean isCurrent) {
        if (sale == null) {
            interactor.getSale(Integer.valueOf(PreferencesManager.getInstance().getCity()), saleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Sale>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            AnalyticsTrackers.getInstance().sendError(e);
                            ErrorManager.printStackTrace(e);
                            getViewState().showError(e);
                        }

                        @Override
                        public void onNext(Sale result) {
                            if (result != null) {
                                sale = result;
                                showSale();
                                if (isCurrent){
                                    showTapTarget();
                                }
                            } else {
                                getViewState().showSaleEmpty();
                            }
                        }
                    });
        }
    }

    private void showSale() {
        getViewState().showInfo(sale);
        File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImageSmall());
        if (smallFile != null) {
            getViewState().showImage(ImageSource.uri(smallFile.getAbsolutePath()));
            loadBigImage(sale);
        } else {
            loadSmallImage();
        }
        if (editMode) {
            getViewState().showSendButton();
            getViewState().showCropButton();
            getViewState().showCommentsButton();
            getViewState().hideCommentsMenuItem();
            if (sale.isFavorite()) {
                getViewState().showFavorite(sale.isFavorite());
            }
        } else {
            getViewState().hideSendButton();
            getViewState().hideCropButton();
            getViewState().hideCommentsButton();
            getViewState().showCommentsMenuItem();
            getViewState().hideFavorite();
        }
    }

    private void loadSmallImage() {
        ImageLoader.getInstance().loadImage(sale.getImageSmall(), null, UIL.getScaleImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                getViewState().showProgress();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                getViewState().hideProgress();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImageSmall());
                if (smallFile != null) {
                    getViewState().showImage(ImageSource.uri(smallFile.getAbsolutePath()));
                }
                bitmap.recycle();
                loadBigImage(sale);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                getViewState().hideProgress();
            }
        }, (s, view, progress, size) -> getViewState().setProgress(progress * 500 / size));
    }


    private void loadBigImage(Sale sale) {
        ImageLoader.getInstance().loadImage(sale.getImageBig(), null, UIL.getScaleImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                getViewState().showProgress();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                getViewState().hideProgress();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                File file = ImageLoader.getInstance().getDiskCache().get(sale.getImageBig());
                if (file != null) {
                    getViewState().showImage(ImageSource.uri(file.getAbsolutePath()));
                }
                getViewState().hideProgress();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                getViewState().hideProgress();
            }
        }, (s, view, progress, size) -> getViewState().setProgress(500 + progress * 500 / size));
    }

    public Sale getSale() {
        return sale;
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
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        getViewState().showErrorLoadingLists(e);
                    }

                    @Override
                    public void onNext(List<ShoppingList> shoppingLists) {
                        if (shoppingLists != null && shoppingLists.size() > 0) {
                            Collections.sort(shoppingLists, PreferencesManager.getInstance().getShoppingListComparator());
                            lists = shoppingLists;
                            if (lists.size() > 1) {
                                getViewState().showListsSelecting(shoppingLists);
                            } else {
                                selectedLists.clear();
                                selectedLists.addAll(lists);
                                openInputListItem();
                            }
                        } else {
                            getViewState().showEmptyLists();
                        }
                    }
                });
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
        ListItem item = SaleTOListItemMapper.getListItem(sale);
        long[] ids = new long[selectedLists.size()];
        for (int i = 0; i < selectedLists.size(); i++) {
            ids[i] = selectedLists.get(i).getId();
        }
        getViewState().openInputListItemFragment(item, ids);
    }

    public String getShopNameForUrl(String shopUrl) {
        return new ShopInteractor().getForUrl(shopUrl, Integer.parseInt(PreferencesManager.getInstance().getCity()));
    }

    public void showTapTarget() {
        /*if (!PreferencesManager.getInstance().isTapTargetForSOSaleCommentsShown()) {
            getViewState().showTapTargetForComments();
            PreferencesManager.getInstance().putTapTargetForSOSaleComments(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForSOSaleSendingShown() && editMode) {
            getViewState().showTapTargetForSending();
            PreferencesManager.getInstance().putTapTargetForSOSaleSending(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForSaleSOCroppingShown() && editMode) {
            getViewState().showTapTargetForCropping();
            PreferencesManager.getInstance().putTapTargetForSOSaleCropping(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForSaleSOSharingShown()) {
            getViewState().showTapTargetForSharing();
            PreferencesManager.getInstance().putTapTargetForSOSaleSharing(true);
            return;
        }*/
    }

    public void onToFavoriteClicked() {
        if (sale.isFavorite()){
            listItemInteractor.removeWithSaleIdFromList(listInteractor.getDefaultList().getId(), sale.getId());
        } else {
            ListItem item = SaleTOListItemMapper.getListItem(sale);
            item.setListId(listInteractor.getDefaultList().getId());
            listItemInteractor.addItem(item);
        }
        sale.setFavorite(!sale.isFavorite());
        getViewState().showFavorite(sale.isFavorite());
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }
}
