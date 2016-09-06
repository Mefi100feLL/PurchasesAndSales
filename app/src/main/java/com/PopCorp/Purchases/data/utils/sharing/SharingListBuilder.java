package com.PopCorp.Purchases.data.utils.sharing;

import android.content.Intent;

import com.PopCorp.Purchases.data.model.ListItem;

import java.util.List;

public interface SharingListBuilder {

    Intent getIntent(String name, String currency, List<ListItem> items);
}
