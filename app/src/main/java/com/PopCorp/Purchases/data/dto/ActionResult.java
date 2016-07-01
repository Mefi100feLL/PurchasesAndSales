package com.PopCorp.Purchases.data.dto;

import com.google.gson.annotations.SerializedName;

public class ActionResult {

    @SerializedName("result")
    private boolean result;

    @SerializedName("message")
    private String message;


    public ActionResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
