package com.PopCorp.Purchases.domain.repository;

import com.PopCorp.Purchases.data.model.ListItem;

import java.util.List;

import rx.Observable;

public interface ListItemRepository {

    Observable<List<ListItem>> getForList(long listId);
    Observable<ListItem> getWithId(long itemId);
}
