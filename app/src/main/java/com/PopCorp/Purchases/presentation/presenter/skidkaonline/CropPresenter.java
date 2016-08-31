package com.PopCorp.Purchases.presentation.presenter.skidkaonline;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class CropPresenter extends MvpPresenter<CropView> implements CreateEditListCallback {

    private SaleInteractor saleInteractor = new SaleInteractor();
    private CropInteractor interactor = new CropInteractor();
    private ShoppingListInteractor listInteractor = new ShoppingListInteractor();
    private ListItemInteractor listItemInteractor = new ListItemInteractor();
    private ListItemCategoryDAO listItemCategoryDAO = new ListItemCategoryDAO();

    private Sale currentSale;
    private String croppedImageUri;


    public List<ShoppingList> lists = new ArrayList<>();
    private ArrayList<ShoppingList> selectedLists = new ArrayList<>();

    public void setSaleId(int saleId) {
        if (currentSale == null) {
            saleInteractor.getSale(Integer.valueOf(PreferencesManager.getInstance().getRegionId()), saleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Sale>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Sale result) {
                            currentSale = result;
                            getViewState().showSkips();
                            getViewState().showFab();
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
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy", new Locale("ru"));
        List<ListItemCategory> categories = listItemCategoryDAO.getAllCategories();
        ListItemCategory listItemCategory = null;
        if (categories != null && categories.size() > 0) {
            listItemCategory = categories.get(0);
            for (ListItemCategory category : categories) {
                if (category.getName().contains("Акци")) {
                    listItemCategory = category;
                }
            }
        }
        String count = "1";
        String edizm = "шт ";
        String coast = "0";
        String shopName = "";
        Shop shop = new ShopDAO().getWithUrl(currentSale.getShopUrl(), currentSale.getCityId());
        if (shop != null) {
            shopName = shop.getName();
        }
        String comment = format.format(new Date(currentSale.getPeriodStart())) + " - " + format.format(new Date(currentSale.getPeriodEnd()));

        ListItem item = new ListItem(
                -1,
                -1,
                "",
                count,
                edizm,
                coast,
                listItemCategory,
                shopName,
                comment,
                false,
                false,
                new ListItemSale(-1, croppedImageUri, format.format(new Date(currentSale.getPeriodStart())), format.format(new Date(currentSale.getPeriodEnd())))
        );
        long[] ids = new long[selectedLists.size()];
        for (int i = 0; i < selectedLists.size(); i++) {
            ids[i] = selectedLists.get(i).getId();
        }
        getViewState().openInputListItemFragment(item, ids);
    }

    public String getShopNameForUrl(String shopUrl) {
        return new ShopInteractor().getForUrl(shopUrl, Integer.parseInt(PreferencesManager.getInstance().getCity()));
    }
}
