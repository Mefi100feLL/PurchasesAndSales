package com.PopCorp.Purchases.data.model;

import com.google.gson.annotations.SerializedName;

public class SaleComment {

    @SerializedName("saleId")
    private int saleId;

    @SerializedName("author")
    private String author;

    @SerializedName("whom")
    private String whom;

    @SerializedName("text")
    private String text;

    @SerializedName("dateTime")
    private long dateTime;

    private int tmpText;
    private int error;
    private String errorText;

    public SaleComment(int saleId, String author, String whom, String text, long dateTime) {
        this.saleId = saleId;
        this.author = author;
        this.whom = whom;
        this.text = text;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SaleComment)) return false;
        SaleComment saleComment = (SaleComment) object;
        if (saleId == saleComment.getSaleId() &&
                author.equals(saleComment.getAuthor()) &&
                text.equals(saleComment.getText()) &&
                dateTime == saleComment.getDateTime()){
            return true;
        } else{
            return false;
        }
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
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
