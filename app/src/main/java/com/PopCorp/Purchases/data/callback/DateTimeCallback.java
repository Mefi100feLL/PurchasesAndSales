package com.PopCorp.Purchases.data.callback;

import java.util.Calendar;

public interface DateTimeCallback {

    void onDateSelected(Calendar date);
    void onTimeSelected(Calendar time);
}
