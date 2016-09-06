package com.PopCorp.Purchases.data.utils.sharing;

import android.content.Intent;

import com.PopCorp.Purchases.data.model.ListItem;

import java.util.List;

public class SharingListAsSMSBuilder implements SharingListBuilder{

    @Override
    public Intent getIntent(String name, String currency, List<ListItem> items) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, name);
        intent.putExtra(Intent.EXTRA_TEXT, getItems(items, currency));
        return intent;
    }

    private String getItems(List<ListItem> items, String currency){
        String result = "";
        for (ListItem item : items){
            result += item.getName();
            result += item.getComment().isEmpty() ? " " : " (" + item.getComment() + ") ";
            result += item.getCountString() + " " + item.getEdizm() + " по " + item.getCoastString() + " " + currency;
            result += item.getShop().isEmpty() ? "" : " в " + item.getShop();
            result += "\n";
        }
        return result;
    }
}