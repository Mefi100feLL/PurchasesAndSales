package com.PopCorp.Purchases.presentation.presenter;

import android.view.View;

import com.PopCorp.Purchases.AnalyticsTrackers;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.ProductAlphabetComparator;
import com.PopCorp.Purchases.data.comparator.ProductCategoryComparator;
import com.PopCorp.Purchases.data.mapper.ListItemProductMapper;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.domain.interactor.ProductInteractor;
import com.PopCorp.Purchases.presentation.view.adapter.SelectingProductsAdapter;
import com.PopCorp.Purchases.presentation.view.adapter.ShopsAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.SelectingProductsView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class SelectingProductsPresenter extends MvpPresenter<SelectingProductsView> implements RecyclerCallback<Product> {

    private ProductInteractor interactor = new ProductInteractor();

    private ArrayList<Product> objects = new ArrayList<>();

    private ArrayList<Product> selectedProducts;
    private String currentFilter = SelectingProductsAdapter.FILTER_ALL;

    private Comparator<Product> comparator = new ProductAlphabetComparator();


    public SelectingProductsPresenter() {
        getViewState().showProgress();
    }

    public void setListItems(ArrayList<ListItem> listItems) {
        if (selectedProducts == null) {
            selectedProducts = new ArrayList<>();
            for (ListItem item : listItems) {
                Product product = ListItemProductMapper.listitemToProduct(item);
                selectedProducts.add(product);
                objects.add(product);
            }
            loadData();
            if (objects.size() > 0) {
                getViewState().showData();
                getViewState().filter(currentFilter);
            }
        }
    }

    private void loadData() {
        interactor.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AnalyticsTrackers.getInstance().sendError(e);
                        ErrorManager.printStackTrace(e);
                        if (objects.size() == 0){
                            getViewState().showProductsEmpty();
                        } else{
                            getViewState().showSnackBar(e);
                        }
                    }

                    @Override
                    public void onNext(List<Product> productsList) {
                        for (Product product : productsList) {
                            if (!objects.contains(product)) {
                                objects.add(product);
                            }
                        }
                        if (objects.size() > 0) {
                            getViewState().showData();
                            getViewState().filter(currentFilter);
                        } else {
                            getViewState().showProductsEmpty();
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(View view, Product item) {

    }

    @Override
    public void onItemLongClicked(View view, Product item) {

    }

    @Override
    public void onEmpty() {
        switch (currentFilter) {
            case SelectingProductsAdapter.FILTER_ALL:
                getViewState().showProductsEmpty();
                break;
            case SelectingProductsAdapter.FILTER_FAVORITE:
                getViewState().showFavoriteProductsEmpty();
                break;
            default:
                getViewState().showSearchProductsEmpty(currentFilter);
                break;
        }
    }

    @Override
    public void onEmpty(int stringRes, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    @Override
    public void onEmpty(String string, int drawableRes, int buttonRes, View.OnClickListener listener) {

    }

    public void onFabClicked() {
        ArrayList<ListItem> result = new ArrayList<>();
        for (Product product : selectedProducts){
            result.add(ListItemProductMapper.productToListItem(product));
        }
        getViewState().setResultAndExit(result);
    }

    public void search(String newText) {
        currentFilter = newText;
        getViewState().showData();
        getViewState().filter(newText);
        if (newText.isEmpty() && comparator instanceof ProductAlphabetComparator) {
            getViewState().showFastScroll();
        } else {
            getViewState().hideFastScroll();
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

    public void sortByAlphabet(int itemId) {
        comparator = new ProductAlphabetComparator();
        setComparator();
        getViewState().checkItem(itemId);
        getViewState().showFastScroll();
    }

    public void sortByCategories(int itemId) {
        comparator = new ProductCategoryComparator();
        setComparator();
        getViewState().checkItem(itemId);
        getViewState().hideFastScroll();
    }

    private void setComparator() {
        getViewState().setAdapterComparator(comparator);
    }

    public ArrayList<Product> getObjects() {
        return objects;
    }

    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }
}
