package com.PopCorp.Purchases.data.model;

import java.math.BigDecimal;

public class Product implements ContentSame<Product> {

    private long id;
    private String name;
    private BigDecimal count;
    private String edizm;
    private BigDecimal coast;
    private ListItemCategory category;
    private String shop;
    private String comment;
    private boolean favorite = false;

    public Product(long id, String name, String count, String edizm, String coast, ListItemCategory category, String shop, String comment, boolean favorite) {
        this.id = id;
        this.name = name;
        setCount(count);
        this.edizm = edizm;
        setCoast(coast);
        this.category = category;
        this.shop = shop;
        this.comment = comment;
        this.favorite = favorite;
    }

    private void setCoast(String coast) {
        try{
            this.coast = new BigDecimal((coast != null && !coast.isEmpty()) ? coast : "0");
        } catch (Exception e){
            this.coast = new BigDecimal("0");
        }
    }

    private void setCount(String count) {
        try {
            this.count = new BigDecimal((count != null && !count.isEmpty()) ? count : "0");
        } catch (Exception e){
            this.count = new BigDecimal("0");
        }
    }

    @Override
    public boolean equalsContent(Product object) {
        if (equals(object)){
            if (count.equals(object.getCount()) &&
                    favorite == object.isFavorite()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product)) return false;
        Product product = (Product) object;
        return name.equals(product.getName());
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

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getEdizm() {
        return edizm;
    }

    public void setEdizm(String edizm) {
        this.edizm = edizm;
    }

    public BigDecimal getCoast() {
        return coast;
    }

    public void setCoast(BigDecimal coast) {
        this.coast = coast;
    }

    public ListItemCategory getCategory() {
        return category;
    }

    public void setCategory(ListItemCategory category) {
        this.category = category;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getCountString() {
        return count.toString();
    }

    public String getCoastString() {
        return coast.toString();
    }
}
