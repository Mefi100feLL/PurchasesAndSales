package com.PopCorp.Purchases.data.model.skidkaonline;

import com.google.gson.annotations.SerializedName;

public class SaleComment {

    @SerializedName("saleId")
    private int saleId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("createdTime")
    private String createdTime;

    @SerializedName("text")
    private String text;

    @SerializedName("cityName")
    private String cityName;

    private int tmpText;
    private int error;
    private String errorText;

    public SaleComment(int saleId, String userName, String createdTime, String text, String cityName) {
        this.saleId = saleId;
        this.userName = userName;
        this.createdTime = createdTime;
        this.text = text;
        this.cityName = cityName;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SaleComment)) return false;
        SaleComment comment = (SaleComment) object;
        return saleId == comment.getSaleId() && userName.equals(comment.getUserName()) && cityName.equals(comment.getCityName()) && text.equals(comment.getText());
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTmpText() {
        return tmpText;
    }

    public void setTmpText(int tmpText) {
        this.tmpText = tmpText;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
