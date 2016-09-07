package com.PopCorp.Purchases.presentation.utils;

import android.app.Activity;

import com.PopCorp.Purchases.data.utils.PreferencesManager;

public class TableSizes {

    // shops
    public static int getShopTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getShopTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getShopTableSize();
    }

    public static void putShopTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putShopTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putShopTableSize(size);
    }

    //categs
    public static int getCategoryTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getCategoryTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getCategoryTableSize();
    }

    public static void putCategoryTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putCategoryTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putCategoryTableSize(size);
    }

    //sales
    public static int getSaleTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getSaleTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getSaleTableSize();
    }

    public static void putSaleTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putSaleTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putSaleTableSize(size);
}

    //skidkaonline shops
    public static int getSkidkaonlineShopTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getSkidkaonlineShopTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getSkidkaonlineShopTableSize();
    }

    public static void putSkidkaonlineShopTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putSkidkaonlineShopTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putSkidkaonlineShopTableSize(size);
    }


    //skidkaonline sales
    public static int getSkidkaonlineSaleTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getSkidkaonlineSaleTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getSkidkaonlineSaleTableSize();
    }

    public static void putSkidkaonlineSaleTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putSkidkaonlineSaleTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putSkidkaonlineSaleTableSize(size);
    }


    //lists
    public static int getListTableSize(Activity context) {
        if (WindowUtils.isLandscape(context)){
            return PreferencesManager.getInstance().getListTableSizeLandscape();
        }
        return PreferencesManager.getInstance().getListTableSize();
    }

    public static void putListTableSize(Activity context, int size) {
        if (WindowUtils.isLandscape(context)){
            PreferencesManager.getInstance().putListTableSizeLandscape(size);
            return;
        }
        PreferencesManager.getInstance().putListTableSize(size);
    }
}
