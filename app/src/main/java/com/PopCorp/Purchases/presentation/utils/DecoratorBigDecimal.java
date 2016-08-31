package com.PopCorp.Purchases.presentation.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DecoratorBigDecimal {

    private static DecimalFormat df = new DecimalFormat();

    static {
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(true);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(otherSymbols);
    }

    public static String decor(BigDecimal decimal){
        return df.format(decimal);
    }

    public static String decor(String decimal){
        return df.format(new BigDecimal(decimal));
    }
}
