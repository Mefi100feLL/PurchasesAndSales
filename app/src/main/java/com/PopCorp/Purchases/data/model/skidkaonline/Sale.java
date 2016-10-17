package com.PopCorp.Purchases.data.model.skidkaonline;

import com.google.gson.annotations.SerializedName;

public class Sale {

    @SerializedName("id")
    private int id;
    @SerializedName("shopUrl")
    private String shopUrl;
    @SerializedName("imageSmall")
    private String imageSmall;
    @SerializedName("imageBig")
    private String imageBig;
    @SerializedName("periodStart")
    private long periodStart;
    @SerializedName("periodEnd")
    private long periodEnd;
    @SerializedName("catalog")
    private String catalog;
    @SerializedName("cityUrl")
    private String cityUrl;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("imageWidth")
    private int imageWidth;
    @SerializedName("imageHeight")
    private int imageHeight;

    private boolean favorite;

    public Sale(int id, String shopUrl, String imageSmall, String imageBig, long periodStart, long periodEnd, String catalog, String cityUrl, int cityId, int imageWidth, int imageHeight) {
        this.id = id;
        this.shopUrl = shopUrl;
        this.imageSmall = imageSmall;
        this.imageBig = imageBig;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.catalog = catalog;
        this.cityUrl = cityUrl;
        this.cityId = cityId;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sale)) return false;
        Sale sale = (Sale) object;
        return id == sale.getId() && cityId == sale.getCityId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getImageBig() {
        return imageBig;
    }

    public void setImageBig(String imageBig) {
        this.imageBig = imageBig;
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

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
