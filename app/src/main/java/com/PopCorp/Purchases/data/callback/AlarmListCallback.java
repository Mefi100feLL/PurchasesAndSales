package com.PopCorp.Purchases.data.callback;

import com.PopCorp.Purchases.data.model.ShoppingList;

import java.util.Calendar;

public interface AlarmListCallback {

    void setAlarm(ShoppingList list, Calendar date);
    void removeAlarm(ShoppingList list);
}
