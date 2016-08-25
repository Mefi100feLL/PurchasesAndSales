package com.PopCorp.Purchases.data.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.PopCorp.Purchases.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

public class ErrorManager {

    public static String getErrorText(Throwable e, Context context) {
        String result;
        if (e instanceof HttpException) {
            result = context.getString(R.string.error_server_no_response);
        } else if (e instanceof UnknownHostException) {
            result = context.getString(R.string.error_no_internet_connection);
        } else if (e instanceof SocketTimeoutException) {
            result = context.getString(R.string.error_timeout);
        } else if (e instanceof ConnectException) {
            result = context.getString(R.string.error_can_not_connect);
        } else {
            result = e.getMessage();
        }
        return result;
    }

    public static int getErrorResource(Throwable e) {
        int result;
        if (e instanceof HttpException) {
            result = R.string.error_server_no_response;
        } else if (e instanceof UnknownHostException) {
            result = R.string.error_no_internet_connection;
        } else if (e instanceof SocketTimeoutException) {
            result = R.string.error_timeout;
        } else if (e instanceof ConnectException) {
            result = R.string.error_can_not_connect;
        } else {
            result = R.string.error_unknown_error;
        }
        return result;
    }

    public static int getErrorImage(Throwable e) {
        int result = R.drawable.ic_menu_gallery;//default error image
        if (e instanceof HttpException) {
            result = R.drawable.ic_menu_gallery;
        } else if (e instanceof UnknownHostException) {
            result = R.drawable.ic_menu_gallery;
        } else if (e instanceof SocketTimeoutException) {
            result = R.drawable.ic_menu_gallery;
        } else if (e instanceof ConnectException) {
            result = R.drawable.ic_menu_gallery;
        }
        return result;
    }

    public static int getErrorButtonTextResource(Throwable e) {
        int result;
        if (e instanceof HttpException) {
            result = R.string.button_exit;
        } else if (e instanceof UnknownHostException) {
            result = R.string.button_try_again;
        } else if (e instanceof SocketTimeoutException) {
            result = R.string.button_try_again;
        } else if (e instanceof ConnectException) {
            result = R.string.button_try_again;
        } else {
            result = R.string.button_try_again;
        }
        return result;
    }

    public static String getErrorExpandedText(Throwable e, Context context) {
        String result;
        if (e instanceof HttpException) {
            result = context.getString(R.string.error_server_no_response_expanded);
        } else if (e instanceof UnknownHostException) {
            result = context.getString(R.string.error_no_internet_connection_expanded);
        } else if (e instanceof SocketTimeoutException) {
            result = context.getString(R.string.error_timeout_expanded);
        } else if (e instanceof ConnectException) {
            result = context.getString(R.string.error_can_not_connect_expanded);
        } else {
            result = e.getMessage();
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
