package com.PopCorp.Purchases.presentation.decorator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Category;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;

public class ShopDecorator {

    private String name;
    private boolean header;
    private Shop shop;
    private Category category;

    public ShopDecorator(String name, boolean header, Shop shop, Category category) {
        this.name = name;
        this.header = header;
        this.shop = shop;
        this.category = category;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
