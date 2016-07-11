package com.PopCorp.Purchases.presentation.decorator.skidkaonline;

import com.PopCorp.Purchases.data.model.skidkaonline.Category;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.presentation.decorator.SaleCategoryDecorator;

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

    @Override
    public boolean equals(Object object){
        if (!(object instanceof ShopDecorator)) return false;

        ShopDecorator another = (ShopDecorator) object;
        if (isHeader() && another.isHeader()){
            return (getCategory().equals(another.getCategory()));
        }
        if (!(isHeader() || another.isHeader())){
            return getShop().equals(another.getShop());
        }
        return false;
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
