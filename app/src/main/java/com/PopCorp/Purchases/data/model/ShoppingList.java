package com.PopCorp.Purchases.data.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {

    private long id;
    private String name;
    private long dateTime;
    private long alarm;
    private String currency;

    private List<ListItem> items = new ArrayList<>();

    public ShoppingList(long id, String name, long dateTime, long alarm, String currency) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.alarm = alarm;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getAlarm() {
        return alarm;
    }

    public void setAlarm(long alarm) {
        this.alarm = alarm;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }
}
