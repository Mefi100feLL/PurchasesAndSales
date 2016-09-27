package com.PopCorp.Purchases.data.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import com.PopCorp.Purchases.R;

public class ThemeManager {

    public static final String PRIMARY_COLOR = "primary_color_version_four";
    public static final String ACCENT_COLOR = "accent_color_version_four";
    public static final String THEME = "theme_version_four";
    public static final String HEADER = "header_version_four";

    private Context context;
    private SharedPreferences sPref;

    private static ThemeManager singleton;

    public static void setInstance(Context context){
        singleton = new ThemeManager(context);
    }

    public static ThemeManager getInstance(){
        return singleton;
    }

    private ThemeManager(Context context){
        this.context = context;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getPrimaryColorRes(){
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.primary_colors);
        final int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int color = R.color.primary_color;
        int colorPosition = sPref.getInt(PRIMARY_COLOR, -1);
        if (colorPosition != -1 && colorPosition < colors.length) {
            color = colors[colorPosition];
        }
        return color;
    }

    public int getPrimaryColor(){
        return context.getResources().getColor(getPrimaryColorRes());
    }

    public void setPrimaryColor(int position){
        sPref.edit().putInt(PRIMARY_COLOR, position).commit();
    }




    public int getAccentColorRes(){
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.primary_colors);
        final int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int color = R.color.accent;
        int colorPosition = sPref.getInt(ACCENT_COLOR, -1);
        if (colorPosition != -1 && colorPosition < colors.length) {
            color = colors[colorPosition];
        }
        return color;
    }

    public int getAccentColor(){
        return context.getResources().getColor(getAccentColorRes());
    }

    public void setAccentColor(int position){
        sPref.edit().putInt(ACCENT_COLOR, position).commit();
    }


    public int getPrimaryDarkColorRes() {
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.primary_dark_colors);
        final int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int color = R.color.primary_color_dark;
        int colorPosition = sPref.getInt(PRIMARY_COLOR, -1);
        if (colorPosition != -1 && colorPosition < colors.length) {
            color = colors[colorPosition];
        }
        return color;
    }


    public int getThemeRes(){
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.themes);
        final int[] themes = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            themes[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int theme = R.style.PinkTheme;
        int themePosition = sPref.getInt(THEME, -1);
        if (themePosition != -1 && themePosition < themes.length) {
            theme = themes[themePosition];
        }
        return theme;
    }

    public void putPrimaryColor(int position){
        sPref.edit().putInt(THEME, position).commit();
    }


    public int getDialogThemeRes(){
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.dialog_themes);
        final int[] themes = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            themes[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int theme = R.style.IndigoThemeDialog;
        int themePosition = sPref.getInt(THEME, -1);
        if (themePosition != -1 && themePosition < themes.length) {
            theme = themes[themePosition];
        }
        return theme;
    }

    public int getHeaderRes(){
        final TypedArray ta = context.getResources().obtainTypedArray(R.array.headers);
        final int[] headers = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            headers[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        int header = R.drawable.header_background_indigo;
        int headerPosition = sPref.getInt(PRIMARY_COLOR, -1);
        if (headerPosition != -1 && headerPosition < headers.length) {
            header = headers[headerPosition];
        }
        return header;
    }


    public static int shiftColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public int getColor(String key){
        int color = 0;
        switch (key){
            case PRIMARY_COLOR:
                color = getPrimaryColor();
                break;
            case ACCENT_COLOR:
                color = getAccentColor();
                break;
        }
        return color;
    }

    public boolean putColor(String key, int position) {
        switch (key){
            case PRIMARY_COLOR:
                if (sPref.getInt(PRIMARY_COLOR, -1) != position) {
                    setPrimaryColor(position);
                    return true;
                }
                break;
            case ACCENT_COLOR:
                if (sPref.getInt(ACCENT_COLOR, -1) != position) {
                    setAccentColor(position);
                    putPrimaryColor(position);
                    return true;
                }
                break;
        }
        return false;
    }

    public void putPrimaryColor(View view) {
        view.setBackgroundColor(getPrimaryColor());
    }

    public void putPrimaryDarkColor(View view) {
        view.setBackgroundColor(context.getResources().getColor(getPrimaryDarkColorRes()));
    }

    public void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(getPrimaryDarkColorRes()));
        }
    }
}