package com.PopCorp.Purchases.data.model;

import com.google.gson.annotations.SerializedName;

public class SameSale {

    @SerializedName("parentSaleId")
    private int parentSaleId;

    @SerializedName("cityId")
    private int cityId;

    @SerializedName("saleId")
    private int saleId;

    @SerializedName("text")
    private String text;

    @SerializedName("coast")
    private String coast;

    @SerializedName("shopName")
    private String shopName;

    @SerializedName("periodStart")
    private String periodStart;

    @SerializedName("periodEnd")
    private String periodEnd;


    public SameSale(int parentSaleId, int cityId, int saleId, String text, String coast, String shopName, String periodStart, String periodEnd) {
        this.parentSaleId = parentSaleId;
        this.cityId = cityId;
        this.saleId = saleId;
        this.text = text;
        this.coast = coast;
        this.shopName = shopName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SameSale)) return false;
        SameSale sameSale = (SameSale) object;
        return saleId == sameSale.getSaleId() && cityId == sameSale.getCityId() && parentSaleId == sameSale.getParentSaleId();
    }

    public int getParentSaleId() {
        return parentSaleId;
    }

    public void setParentSaleId(int parentSaleId) {
        this.parentSaleId = parentSaleId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }
}
