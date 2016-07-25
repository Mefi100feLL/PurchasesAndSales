package com.PopCorp.Purchases.presentation.decorator;

import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ListItemDecorator {

    private String name;
    private boolean header;
    private ListItem item;
    private boolean buyed;
    private ListItemCategory category;

    public ListItemDecorator(String name, boolean header, ListItem item, boolean buyed, ListItemCategory category) {
        this.name = name;
        this.header = header;
        this.item = item;
        this.buyed = buyed;
        this.category = category;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListItemDecorator)) return false;
        ListItemDecorator decorator = (ListItemDecorator) object;
        boolean result = false;
        if (header && decorator.isHeader()) {
            result = name.equals(decorator.getName());
        } else if (!header && !decorator.isHeader()) {
            result = item.equals(decorator.getItem());
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        result += "name=" + name + ", ";
        result += "header=" + header + ", ";
        result += "buyed=" + buyed + ", ";
        result += "category=" + category + ", ";
        result += "item=" + item;
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public ListItem getItem() {
        return item;
    }

    public void setItem(ListItem item) {
        this.item = item;
    }

    public boolean isBuyed() {
        if (item != null) {
            return item.isBuyed();
        }
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    public ListItemCategory getCategory() {
        return category;
    }

    public void setCategory(ListItemCategory category) {
        this.category = category;
    }

    public boolean contentEquals(ListItemDecorator decorator) {
        boolean result = Arrays.equals(getFields(), decorator.getFields());
        if ((!header && !decorator.isHeader()) && result) {
            result = Arrays.equals(getItemFields(), decorator.getItemFields());
        }
        return result;
    }

    public String[] getFields() {
        String categoryId = "";
        if (category != null) {
            categoryId = String.valueOf(category.getId());
        }
        return new String[]{
                name,
                String.valueOf(header),
                String.valueOf(buyed),
                categoryId
        };
    }

    public String[] getItemFields() {
        String saleId = String.valueOf(item.getSale() != null ? item.getSale().getId() : "");
        return new String[]{
                item.getName(),
                item.getCountString(),
                item.getEdizm(),
                item.getCoastString(),
                String.valueOf(item.getCategory().getId()),
                item.getShop(),
                item.getComment(),
                String.valueOf(item.isBuyed()),
                String.valueOf(item.isImportant()),
                saleId
        };
    }
}
