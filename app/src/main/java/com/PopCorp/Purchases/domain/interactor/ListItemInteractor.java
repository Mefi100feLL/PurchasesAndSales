package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.repository.db.ListItemDBRepository;
import com.PopCorp.Purchases.data.repository.db.ProductDBRepository;

import java.util.List;

import rx.Observable;
import rx.Observer;

public class ListItemInteractor {

    private ListItemDBRepository dbRepository = new ListItemDBRepository();
    private ProductDBRepository productDBRepository = new ProductDBRepository();
    private ListItemSaleDAO saleDAO = new ListItemSaleDAO();

    public Observable<List<ListItem>> getForList(long listId) {
        return dbRepository.getForList(listId);
    }

    public Observable<ListItem> getWithId(long itemId) {
        return dbRepository.getWithId(itemId);
    }

    public boolean existsWithName(long listId, String name) {
        return dbRepository.existWithName(listId, name);
    }

    public void addItem(ListItem item) {
        if (item.getSale() != null) {
            saleDAO.updateOrAddToDB(item.getSale());
        }
        dbRepository.addItem(item);

        Product product = new Product(-1, item.getName(), item.getCountString(), item.getEdizm(), item.getCoastString(), item.getCategory(), item.getShop(), item.getComment(), true);
        productDBRepository.addItem(product);
    }

    public void updateItem(ListItem item) {
        dbRepository.updateItem(item);
    }

    public void removeItem(ListItem item) {
        dbRepository.removeItem(item);
    }

    public void addItems(ListItem[] items) {
        dbRepository.addItems(items);
    }

    public void removeWithSaleIdFromList(long listId, int saleId) {
        dbRepository.getForList(listId)
                .map(listItems -> {
                    if (listItems != null && listItems.size() > 0) {
                        for (ListItem item : listItems) {
                            if (item.getSale() != null && item.getSale().getSaleId() == saleId) {
                                removeItem(item);
                                return true;
                            }
                        }
                    }
                    return false;
                }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean o) {

            }
        });
    }
}
