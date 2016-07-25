package com.PopCorp.Purchases.data.dto;

import com.google.gson.annotations.SerializedName;

import rx.Observable;

public class UniversalDTO<T> {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private T result;

    public UniversalDTO(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Observable<T> getData(){
        if (error) {
            return Observable.error(new Throwable(message));
        }
        return Observable.just(result);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}