package com.PopCorp.Purchases.data.utils;

import com.PopCorp.Purchases.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

public class ErrorManager {

    public static int getErrorResource(Throwable e) {
        int result;
        if (e instanceof HttpException) {
            result = R.string.error_server_no_response;
        } else if (e instanceof UnknownHostException) {
            result = R.string.error_no_internet_connection;
        } else if (e instanceof SocketTimeoutException) {
            result = R.string.error_timeout;
        } else if (e instanceof ConnectException){
            result = R.string.error_can_not_connect;
        } else {
            result = R.string.error_unknown_error;
        }
        return result;
    }

    /*public static int getErrorImage(Throwable e) {
        int result;
        if (e instanceof HttpException) {
            result = R.drawable.ic_server_network_off_grey600_48dp;
        } else if (e instanceof UnknownHostException) {
            result = R.drawable.ic_close_network_grey600_48dp;
        } else {
            result = R.drawable.ic_alert_grey600_48dp;
        }
        return result;
    }*/
}
