package com.PopCorp.Purchases.presentation.decorator;

import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;

public class SaleShopDecorator extends SaleDecorator {

    private Shop shop;

    public SaleShopDecorator(Sale sale, boolean header, Shop shop){
        super(sale, header);
        setShop(shop);
    }

    @Override
    public String getName() {
        return shop.getName();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop(){
        return shop;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof SaleShopDecorator)) return false;

        SaleShopDecorator another = (SaleShopDecorator) object;
        if (isHeader() && another.isHeader()){
            return (getShop().equals(another.getShop()));
        }
        if (!(isHeader() || another.isHeader())){
            return getSale().equals(another.getSale());
        }
        return false;
    }
}
