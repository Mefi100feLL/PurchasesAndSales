package com.PopCorp.Purchases.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Sale {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("subTitle")
    private String subTitle;

    @SerializedName("periodStart")
    private long periodStart;

    @SerializedName("periodEnd")
    private long periodEnd;

    @SerializedName("coast")
    private String coast;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("coastForQuantity")
    private String coastForQuantity;

    @SerializedName("image")
    private String image;

    @SerializedName("cityId")
    private int cityId;

    @SerializedName("shopId")
    private int shopId;

    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("categoryType")
    private int categoryType;

    @SerializedName("countComments")
    private int countComments;

    private Category category;
    private Shop shop;

    private boolean favorite;

    @SerializedName("comments")
    private List<SaleComment> comments = new ArrayList<>();

    @SerializedName("sameSales")
    private List<SameSale> sameSales = new ArrayList<>();


    public Sale(int id, String title, String subTitle, long periodStart, long periodEnd, String coast, String quantity, String coastForQuantity, String image, int cityId, int shopId, int categoryId, int categoryType, int countComments) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.coast = coast;
        this.quantity = quantity;
        this.coastForQuantity = coastForQuantity;
        this.image = image;
        this.cityId = cityId;
        this.shopId = shopId;
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.countComments = countComments;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sale)) return false;
        Sale sale = (Sale) object;
        return getId() == sale.getId() && getCityId() == sale.getCityId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public long getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(long periodStart) {
        this.periodStart = periodStart;
    }

    public long getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(long periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCoastForQuantity() {
        return coastForQuantity;
    }

    public void setCoastForQuantity(String coastForQuantity) {
        this.coastForQuantity = coastForQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public List<SaleComment> getComments() {
        return comments;
    }

    public void setComments(List<SaleComment> comments) {
        this.comments = comments;
    }

    public List<SameSale> getSameSales() {
        return sameSales;
    }

    public void setSameSales(List<SameSale> sameSales) {
        this.sameSales = sameSales;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}