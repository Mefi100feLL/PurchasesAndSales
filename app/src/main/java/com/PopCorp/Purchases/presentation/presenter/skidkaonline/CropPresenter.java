package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.graphics.Bitmap;

import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.mapper.SaleTOListItemMapper;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.ShoppingListInteractor;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.CropInteractor;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.SaleInteractor;
import com.PopCorp.Purchases.domain.interactor.skidkaonline.ShopInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.CropView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yalantis.ucrop.view.TransformImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class CropPresenter extends MvpPresenter<CropView> implements CreateEditListCallback, TransformImageView.TransformImageListener {

    private SaleInteractor saleInteractor = new SaleInteractor();
    private CropInteractor interactor = new CropInteractor();
    private ShoppingListInteractor listInteractor = new ShoppingListInteractor();
    private ListItemInteractor listItemInteractor = new ListItemInteractor();

    private Sale currentSale;
    private String croppedImageUri;

    public List<ShoppingList> lists = new ArrayList<>();
    private ArrayList<ShoppingList> selectedLists = new ArrayList<>();

    private boolean imageSended = false;

    public void setSaleId(int saleId) {
        if (currentSale == null) {
            saleInteractor.getSale(Integer.valueOf(PreferencesManager.getInstance().getCity()), saleId)
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
                            currentSale = result;
                            getViewState().showSkips();
                            getViewState().showFab();
                            showTapTarget();
                        }
                    });
        }
    }

    public void saveCroppedImage(Bitmap bitmap) {
        getViewState().showImage(bitmap);
        getViewState().showProgress();
        getViewState().hideSkips();
        getViewState().hideFab();
        interactor.saveBitmapInFile(bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        getViewState().hideProgress();
                        getViewState().showSkips();
                        getViewState().showFab();
                        getViewState().showErrorCanNotCropImage();
                    }

                    @Override
                    public void onNext(String uri) {
                        getViewState().hideProgress();
                        if (uri != null && !uri.isEmpty()) {
                            croppedImageUri = uri;
                            getViewState().showImage(croppedImageUri);
                            getViewState().showSendingButton();
                        } else {
                            getViewState().showSkips();
                            getViewState().showFab();
                            getViewState().showErrorCanNotCropImage();
                        }
                    }
                });
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
        imageSended = true;
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
        currentSale.setImageBig(croppedImageUri);
        ListItem item = SaleTOListItemMapper.getListItem(currentSale);
        long[] ids = new long[selectedLists.size()];
        for (int i = 0; i < selectedLists.size(); i++) {
            ids[i] = selectedLists.get(i).getId();
        }
        getViewState().openInputListItemFragment(item, ids);
    }

    public String getShopNameForUrl(String shopUrl) {
        return new ShopInteractor().getForUrl(shopUrl, Integer.parseInt(PreferencesManager.getInstance().getCity()));
    }

    public void clearImage() {
        if (croppedImageUri != null && !imageSended) {
            ImageLoader.getInstance().getDiskCache().remove(croppedImageUri);
        }
    }

    @Override
    public void onRotate(float currentAngle) {

    }

    @Override
    public void onScale(float currentScale) {

    }

    public Sale getSale() {
        return currentSale;
    }

    public boolean isCropped(){
        return croppedImageUri != null;
    }

    public void showTapTarget() {
        /*if (!PreferencesManager.getInstance().isTapTargetForCroppingRotateSkipShown()) {
            getViewState().showTapTargetForRotateSkip();
            PreferencesManager.getInstance().putTapTargetForCroppingRotateSkip(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForCroppingCropShown()) {
            getViewState().showTapTargetForCrop();
            PreferencesManager.getInstance().putTapTargetForCroppingCrop(true);
            return;
        }
        if (!PreferencesManager.getInstance().isTapTargetForCroppingScaleSkipShown()) {
            getViewState().showTapTargetForScaleSkip();
            PreferencesManager.getInstance().putTapTargetForCroppingScaleSkip(true);
            return;
        }*/
    }
}
