package com.PopCorp.Purchases.data.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList implements ContentSame<ShoppingList> {

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

    @Override
    public boolean equalsContent(ShoppingList object) {
        if (equals(object)) {
            if (name.equals(object.getName()) && items.size() == object.getItems().size()) {
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).equalsContent(object.getItems().get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShoppingList)) return false;
        ShoppingList list = (ShoppingList) object;
        return getId() == list.getId();
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
