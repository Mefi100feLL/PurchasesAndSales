package com.PopCorp.Purchases.data.dto;

import com.google.gson.annotations.SerializedName;

public class CommentResult extends ActionResult {

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("dateTime")
    private long dateTime;

    public CommentResult(boolean result, String message, String date, String time, long dateTime) {
        super(result, message);
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
