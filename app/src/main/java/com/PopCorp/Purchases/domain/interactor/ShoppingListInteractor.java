package com.PopCorp.Purchases.domain.interactor;

import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.repository.db.ShoppingListDBRepository;

import java.util.List;

import rx.Observable;

public class ShoppingListInteractor {

    private ShoppingListDBRepository dbRepository = new ShoppingListDBRepository();

    public Observable<List<ShoppingList>> getData(){
        return dbRepository.getData();
    }

    public void addNewShoppingList(ShoppingList list) {
        dbRepository.addList(list);
    }

    public Observable<ShoppingList> getList(long listId) {
        return dbRepository.getListWithId(listId);
    }

    public void saveList(ShoppingList list) {
        dbRepository.saveList(list);
    }

    public void removeList(ShoppingList list) {
        dbRepository.removeList(list);
    }
}
