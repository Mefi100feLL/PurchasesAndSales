package com.PopCorp.Purchases.data.repository.db;

import com.PopCorp.Purchases.data.dao.ShoppingListDAO;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.domain.repository.ShoppingListRepository;

import java.util.List;

import rx.Observable;

public class ShoppingListDBRepository implements ShoppingListRepository {

    private ShoppingListDAO dao = new ShoppingListDAO();

    @Override
    public Observable<List<ShoppingList>> getData() {
        return Observable.just(dao.getAllLists());
    }

    public void addList(ShoppingList list) {
        list.setId(dao.updateOrAddToDB(list));
    }

    public Observable<ShoppingList> getListWithId(long listId) {
        return Observable.just(dao.getListWithId(listId));
    }

    public void saveList(ShoppingList list) {
        dao.updateOrAddToDB(list);
    }

    public void removeList(ShoppingList list) {
        dao.remove(list);
    }
}
