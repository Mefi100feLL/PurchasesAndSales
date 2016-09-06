package com.PopCorp.Purchases.data.mapper;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.Product;

public class ListItemProductMapper {

    public static ListItem productToListItem(Product product){
        return new ListItem(-1, -1, product.getName(), product.getCountString(), product.getEdizm(), product.getCoastString(), product.getCategory(), product.getShop(), product.getComment(), false, false, null);
    }

    public static Product listitemToProduct(ListItem listItem){
        return new Product(-1, listItem.getName(), listItem.getCountString(), listItem.getEdizm(), listItem.getCoastString(), listItem.getCategory(), listItem.getShop(), listItem.getComment(), true);
    }
}
