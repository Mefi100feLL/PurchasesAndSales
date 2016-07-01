package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.ListItemDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.domain.repository.ListItemRepository;

import java.util.List;

import rx.Observable;

public class ListItemDBRepository implements ListItemRepository {

    private ListItemDAO dao = new ListItemDAO();

    @Override
    public Observable<List<ListItem>> getForList(long listId) {
        return Observable.just(dao.getAllItemsForList(listId));
    }

    @Override
    public Observable<ListItem> getWithId(long itemId) {
        return Observable.just(dao.getWithId(itemId));
    }

    public boolean existWithName(long listId, String name) {
        return dao.existsWithName(listId, name);
    }

    public void addItem(ListItem item) {
        item.setId(dao.updateOrAddToDB(item));
    }

    public void removeItem(ListItem item) {
        dao.remove(item);
    }
}
