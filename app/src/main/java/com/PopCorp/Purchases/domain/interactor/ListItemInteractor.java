package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.repository.db.ListItemDBRepository;
import com.PopCorp.Purchases.data.repository.db.ProductDBRepository;

import java.util.List;

import rx.Observable;

public class ListItemInteractor {

    private ListItemDBRepository dbRepository = new ListItemDBRepository();
    private ProductDBRepository productDBRepository = new ProductDBRepository();

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
        dbRepository.addItem(item);
        Product product = new Product(-1, item.getName(), item.getCountString(), item.getEdizm(), item.getCoastString(), item.getCategory(), item.getShop(), item.getComment(), true);
        productDBRepository.addItem(product);
    }

    public void updateItem(ListItem item) {
        dbRepository.addItem(item);
    }

    public void removeItem(ListItem item) {
        dbRepository.removeItem(item);
    }
}
