package com.PopCorp.Purchases.presentation.presenter;

import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.domain.interactor.ListItemInteractor;
import com.PopCorp.Purchases.domain.interactor.ProductInteractor;
import com.PopCorp.Purchases.presentation.view.moxy.InputListItemView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class InputListItemPresenter extends MvpPresenter<InputListItemView> {

    private ListItemInteractor interactor = new ListItemInteractor();
    private ProductInteractor productInteractor = new ProductInteractor();

    private ListItem item;

    private List<ListItemCategory> categories;
    private List<Product> products = new ArrayList<>();
    private long[] listsIds;

    public InputListItemPresenter(){
        ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO();
        categories = categoryDAO.getAllCategories();
        getViewState().showCategoriesSpinner(categories);
        loadProducts();
    }

    private void loadProducts() {
        productInteractor.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Product> productsList) {
                        products.addAll(productsList);
                        ArrayList<String> names = new ArrayList<>();
                        for (Product product : productsList){
                            names.add(product.getName());
                        }
                        getViewState().setNameAdapter(names);
                    }
                });
    }

    public void setItem(ListItem listItem) {
        if (item == null){
            item = listItem;
        }
    }

    public List<ListItemCategory> getCategories() {
        return categories;
    }

    public void buildItem(String name, String count, String unit, String coast, boolean important, String shop, int category, String comment) {
        if (name.isEmpty()){
            getViewState().showNameEmpty();
            return;
        }
        if (item == null || (item.getId() == -1) || (!name.equals(item.getName()))){
            for (long listId : listsIds) {
                if (interactor.existsWithName(listId, name)) {
                    getViewState().showListItemWithNameExists();
                    return;
                }
            }
        }
        getViewState().hideNameError();
        count = count.isEmpty() ? "0" : count;
        coast = coast.isEmpty() ? "0" : coast;
        if (item == null) {
            item = new ListItem(-1, -1, name, count, unit, coast, categories.get(category), shop, comment, false, important, null);
        } else {
            item.setName(name);
            item.setCount(count);
            item.setEdizm(unit);
            item.setCoast(coast);
            item.setCategory(categories.get(category));
            item.setShop(shop);
            item.setComment(comment);
            item.setImportant(important);
        }
        getViewState().returnItemAndClose(item);
    }

    public void onProductSelected(String name) {
        Product product = null;
        for (Product item : products){
            if (item.getName().equals(name)){
                product = item;
                break;
            }
        }
        getViewState().setFields(product);
    }

    public ListItem getItem() {
        return item;
    }

    public void setListsIds(long[] listsIds) {
        this.listsIds = listsIds;
    }
}
