package com.PopCorp.Purchases.data.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.PopCorp.Purchases.BuildConfig;
import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorAddedComparator;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorAlphabetAZComparator;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorAlphabetZAComparator;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorBuyedComparator;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorCategoryComparator;
import com.PopCorp.Purchases.data.comparator.ListItemDecoratorHeaderComparator;
import com.PopCorp.Purchases.data.comparator.ShoppingListAlphabetComparator;
import com.PopCorp.Purchases.data.comparator.ShoppingListDateComparator;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.presentation.decorator.ListItemDecorator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PreferencesManager {

    public static final String PREFS_CITY = "city";
    public static final String PREFS_SKIDKAONLINE_CITY = "skidkaonline_city";

    public static final String PREFS_SIZE_TABLE_LISTS = "size_table_lists";
    public static final String PREFS_SIZE_TABLE_SHOPS = "size_table_shops";
    public static final String PREFS_SIZE_TABLE_SALES = "size_table_sales";
    public static final String PREFS_SIZE_TABLE_CATEGS = "size_table_categs";
    public static final String PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE = "size_table_shops_skidkaonline";
    public static final String PREFS_SIZE_TABLE_SALES_SKIDKAONLINE = "size_table_sales_sakidkaonline";
    public static final String PREFS_SIZE_TABLE_LISTITEMS = "size_table_listitems";


    public static final String PREFS_SIZE_TABLE_LISTS_LANDSCAPE = "size_table_lists_landscape";
    public static final String PREFS_SIZE_TABLE_SHOPS_LANDSCAPE = "size_table_shops_landscape";
    public static final String PREFS_SIZE_TABLE_SALES_LANDSCAPE = "size_table_sales_landscape";
    public static final String PREFS_SIZE_TABLE_CATEGS_LANDSCAPE = "size_table_categs_landscape";
    public static final String PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE_LANDSCAPE = "size_table_shops_skidkaonline_landscape";
    public static final String PREFS_SIZE_TABLE_SALES_SKIDKAONLINE_LANDSCAPE = "size_table_sales_sakidkaonline_landscape";
    public static final String PREFS_SIZE_TABLE_LISTITEMS_LANDSCAPE = "size_table_listitems_landscape";

    public static final String PREFS_DISPLAY_NO_OFF = "displaynooff";
    public static final String PREFS_LIST_ITEM_FONT_SIZE = "listsize";
    public static final String PREFS_LIST_ITEM_FONT_SIZE_SMALL = "listsizesmall";
    public static final String PREFS_REPLACE_BUYED = "replacebuyed";
    public static final String PREFS_SHOW_CATEGORIES = "showcategories";
    public static final String PREFS_SORT_LIST_ITEM = "sortlistitem";
    public static final String PREFS_CATEGORIES = "categories";

    public static final String PREFS_SHOPES = "shopes";

    public static final String PREFS_CURRENCY = "currency";
    public static final String PREFS_CURRENCYS = "currencys";
    public static final String PREFS_DEF_CURRENCY = "defcurrency";

    public static final String PREFS_UNIT = "unit";
    public static final String PREFS_EDIZMS = "units";
    public static final String PREFS_DEF_EDIZM = "defunit";

    public static final String PREFS_SORT_LISTS = "sort_list";

    public static final String PREFS_FILTER_LIST = "filter_list";

    public static final String PREFS_ABOUT = "about";
    public static final String PREFS_OPEN_DEFAULT_LIST = "open_default_list";

    public static final String PREFS_COLOR_PRIMARY = "colorPrimary";
    public static final String PREFS_COLOR_ACCENT = "colorAccent";
    public static final String PREFS_THEME = "theme";
    public static final String PREFS_DIALOG_THEME = "dialog_theme";
    public static final String PREFS_HEADER = "header";
    private static final String PREFS_AUTHOR_COMMENT = "author_comment";
    public static final String PREFS_THEME_LIGHT_DARK = "theme_light_dark";


    private static final String PREFS_TAP_TARGET_SHOPS_FILTER = "PREFS_TAP_TARGET_SHOPS_FILTER";
    private static final String PREFS_TAP_TARGET_SHOPS_FAVORITE = "PREFS_TAP_TARGET_SHOPS_FAVORITE";
    private static final String PREFS_TAP_TARGET_SALES_FILTER_BY_SHOPS = "PREFS_TAP_TARGET_SALES_FILTER_BY_SHOPS";
    private static final String PREFS_TAP_TARGET_SALES_FILTER_BY_CATEGS = "PREFS_TAP_TARGET_SALES_FILTER_BY_CATEGS";
    private static final String PREFS_TAP_TARGET_SALES_SEARCH = "PREFS_TAP_TARGET_SALES_SEARCH";
    private static final String PREFS_TAP_TARGET_SALE_COMMENTS = "PREFS_TAP_TARGET_SALE_COMMENTS";
    private static final String PREFS_TAP_TARGET_SALE_SHARING = "PREFS_TAP_TARGET_SALE_SHARING";
    private static final String PREFS_TAP_TARGET_SALE_SENDING = "PREFS_TAP_TARGET_SALE_SENDING";
    private static final String PREFS_TAP_TARGET_CATEGS_FILTER = "PREFS_TAP_TARGET_CATEGS_FILTER";
    private static final String PREFS_TAP_TARGET_CATEGS_FAVORITE = "PREFS_TAP_TARGET_CATEGS_FAVORITE";
    private static final String PREFS_TAP_TARGET_CITIES_SEARCH = "PREFS_TAP_TARGET_CITIES_SEARCH";
    private static final String PREFS_TAP_TARGET_CITY_SELECT = "PREFS_TAP_TARGET_CITY_SELECT";
    private static final String PREFS_TAP_TARGET_SALES_FILTER_BY_CATALOG = "PREFS_TAP_TARGET_SALES_FILTER_BY_CATALOG";
    private static final String PREFS_TAP_TARGET_SO_SALE_SENDING = "PREFS_TAP_TARGET_SO_SALE_SENDING";
    private static final String PREFS_TAP_TARGET_SO_SALE_COMMENTS = "PREFS_TAP_TARGET_SO_SALE_COMMENTS";
    private static final String PREFS_TAP_TARGET_SO_SALE_CROPPING = "PREFS_TAP_TARGET_SO_SALE_CROPPING";
    private static final String PREFS_TAP_TARGET_SO_SALE_SHARING = "PREFS_TAP_TARGET_SO_SALE_SHARING";
    private static final String PREFS_TAP_TARGET_CROPPING_ROTATE_SKIP = "PREFS_TAP_TARGET_CROPPING_ROTATE_SKIP";
    private static final String PREFS_TAP_TARGET_CROPPING_CROP = "PREFS_TAP_TARGET_CROPPING_CROP";
    private static final String PREFS_TAP_TARGET_CROPPING_SCALE_SKIP = "PREFS_TAP_TARGET_CROPPING_SCALE_SKIP";
    private static final String PREFS_TAP_TARGET_LISTS_CREATE = "PREFS_TAP_TARGET_LISTS_CREATE";
    private static final String PREFS_TAP_TARGET_ITEMS_CREATE = "PREFS_TAP_TARGET_ITEMS_CREATE";
    private static final String PREFS_TAP_TARGET_ITEMS_ADD = "PREFS_TAP_TARGET_ITEMS_ADD";
    private static final String PREFS_TAP_TARGET_ITEM_INFO = "PREFS_TAP_TARGET_ITEM_INFO";
    private static final String PREFS_TAP_TARGET_ITEMS_FILTER_BY_SHOP = "PREFS_TAP_TARGET_ITEMS_FILTER_BY_SHOP";
    private static final String PREFS_TAP_TARGET_ITEMS_ADD_TO_ACTION_MODE = "PREFS_TAP_TARGET_ITEMS_ADD_TO_ACTION_MODE";
    private static final String PREFS_TAP_TARGET_ITEM_EDIT_IN_ACTION_MODE = "PREFS_TAP_TARGET_ITEM_EDIT_IN_ACTION_MODE";
    private static final String PREFS_TAP_TARGET_ITEMS_REMOVE_IN_ACTION_MODE = "PREFS_TAP_TARGET_ITEMS_REMOVE_IN_ACTION_MODE";
    private static final String PREFS_TAP_TARGET_CLOSE_ACTION_MODE = "PREFS_TAP_TARGET_CLOSE_ACTION_MODE";
    private static final String PREFS_TAP_TARGET_PRODUCTS_SEARCH = "PREFS_TAP_TARGET_PRODUCTS_SEARCH";
    private static final String PREFS_TAP_TARGET_PRODUCTS_FILTER = "PREFS_TAP_TARGET_PRODUCTS_FILTER";
    private static final String PREFS_TAP_TARGET_PRODUCTS_SORTING = "PREFS_TAP_TARGET_PRODUCTS_SORTING";
    private static final String PREFS_TAP_TARGET_PRODUCTS_RETURN = "PREFS_TAP_TARGET_PRODUCTS_RETURN";
    private static final String PREFS_TAP_TARGET_SALES_FAVORITE = "PREFS_TAP_TARGET_SALES_FAVORITE";

    private Context context;
    private SharedPreferences sPref;
    private SharedPreferences.Editor editor;

    private static PreferencesManager instance;
    //private static Comparator<ShoppingListDecorator> listComparator;

    public static void setInstance(Context context) {
        instance = new PreferencesManager(context);
    }

    public static PreferencesManager getInstance() {
        return instance;
    }

    private PreferencesManager(Context context) {
        this.context = context;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sPref.edit();
    }

    /*public Comparator<ShoppingListDecorator> getListComparator() {
        String value = sPref.getString(PREFS_SORT_LISTS, context.getString(R.string.prefs_default_sort_list_one));
        if (value.equals(context.getString(R.string.prefs_default_sort_list_one))) {
            return new ShoppingListDecoratorAlphabetComparator();
        } else {
            return new ShoppingListDecoratorDateComparator();
        }
    }*/

    public void putRegion(String regionId) {
        editor.putString(PREFS_CITY, regionId).commit();
    }

    public String getRegionId() {
        return sPref.getString(PREFS_CITY, "");
    }

    public int getShopTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_SHOPS, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putShopTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SHOPS, size).commit();
    }

    public int getCategoryTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_CATEGS, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putCategoryTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_CATEGS, size).commit();
    }

    public int getSaleTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_SALES, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putSaleTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SALES, size).commit();
    }


    public int getSkidkaonlineSaleTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_SALES_SKIDKAONLINE, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putSkidkaonlineSaleTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SALES_SKIDKAONLINE, size).commit();
    }

    public int getSkidkaonlineShopTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putSkidkaonlineShopTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE, size).commit();
    }


    public int getListTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_LISTS, context.getResources().getInteger(R.integer.default_size_table_lists));
    }

    public void putListTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_LISTS, size).commit();
    }




    //////////////////////////////////table sizes for landscape

    public int getShopTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_SHOPS_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public void putShopTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SHOPS_LANDSCAPE, size).commit();
    }

    public int getCategoryTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_CATEGS_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public void putCategoryTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_CATEGS_LANDSCAPE, size).commit();
    }

    public int getSaleTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_SALES_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public int getListItemTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_LISTITEMS_LANDSCAPE, 1);
    }

    public int getListItemTableSize() {
        return sPref.getInt(PREFS_SIZE_TABLE_LISTITEMS, 1);
    }

    public void putSaleTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SALES_LANDSCAPE, size).commit();
    }


    public int getSkidkaonlineSaleTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_SALES_SKIDKAONLINE_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public void putSkidkaonlineSaleTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SALES_SKIDKAONLINE_LANDSCAPE, size).commit();
    }

    public int getSkidkaonlineShopTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public void putSkidkaonlineShopTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_SHOPS_SKIDKAONLINE_LANDSCAPE, size).commit();
    }


    public int getListTableSizeLandscape() {
        return sPref.getInt(PREFS_SIZE_TABLE_LISTS_LANDSCAPE, context.getResources().getInteger(R.integer.default_size_table_lists_landscape));
    }

    public void putListTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_LISTS_LANDSCAPE, size).commit();
    }

    public void putListItemTableSizeLandscape(int size) {
        editor.putInt(PREFS_SIZE_TABLE_LISTITEMS_LANDSCAPE, size).commit();
    }

    public void putListItemTableSize(int size) {
        editor.putInt(PREFS_SIZE_TABLE_LISTITEMS, size).commit();
    }



    public boolean isDisplayNoOff() {
        return sPref.getBoolean(PREFS_DISPLAY_NO_OFF, true);
    }

    public void setDisplayNoOff(boolean value) {
        editor.putBoolean(PREFS_DISPLAY_NO_OFF, value).commit();
    }

    public String getCity() {
        return sPref.getString(PREFS_SKIDKAONLINE_CITY, "");
    }

    public void setCity(String city) {
        editor.putString(PREFS_SKIDKAONLINE_CITY, city).commit();
    }


    public Set<String> getCurrencies() {
        return sPref.getStringSet(PREFS_CURRENCYS, new LinkedHashSet<String>());
    }

    public String getCurrentCurrency() {
        return sPref.getString(PREFS_DEF_CURRENCY, context.getString(R.string.default_one_currency));
    }


    public void firstStart() {
        if (!sPref.getBoolean(String.valueOf(BuildConfig.VERSION_CODE), false)) {
            if (BuildConfig.VERSION_CODE >= 18) {
                putCurrencys();
                putDefaultEdizms();
                editFontSizes();
            }
            editor.putBoolean(String.valueOf(BuildConfig.VERSION_CODE), true);
        }
    }

    private void editFontSizes() {
        editor.putString(PREFS_LIST_ITEM_FONT_SIZE, "16").commit();
        editor.putString(PREFS_LIST_ITEM_FONT_SIZE_SMALL, "14").commit();
    }

    public String getListItemFontSize(){
        return sPref.getString(PREFS_LIST_ITEM_FONT_SIZE, "16");
    }

    public String getListItemFontSizeSmall(){
        return sPref.getString(PREFS_LIST_ITEM_FONT_SIZE_SMALL, "14");
    }

    private void putCurrencys() {
        Set<String> currencys = sPref.getStringSet(PREFS_CURRENCYS, new LinkedHashSet<String>());
        if (!currencys.contains(context.getString(R.string.default_one_currency))) {
            currencys.add(context.getString(R.string.default_one_currency));
        }
        if (!currencys.contains(context.getString(R.string.default_two_currency))) {
            currencys.add(context.getString(R.string.default_two_currency));
        }
        if (!currencys.contains(context.getString(R.string.default_three_currency))) {
            currencys.add(context.getString(R.string.default_three_currency));
        }
        editor.putStringSet(PREFS_CURRENCYS, currencys).commit();
    }

    private void putDefaultEdizms() {
        Set<String> units = sPref.getStringSet(PREFS_EDIZMS, new LinkedHashSet<String>());
        if (!units.contains(context.getString(R.string.default_unit_one))) {
            units.add(context.getString(R.string.default_unit_one));
        }
        if (!units.contains(context.getString(R.string.default_unit_two))) {
            units.add(context.getString(R.string.default_unit_two));
        }
        if (!units.contains(context.getString(R.string.default_unit_three))) {
            units.add(context.getString(R.string.default_unit_three));
        }
        if (!units.contains(context.getString(R.string.default_unit_four))) {
            units.add(context.getString(R.string.default_unit_four));
        }
        if (!units.contains(context.getString(R.string.default_unit_five))) {
            units.add(context.getString(R.string.default_unit_five));
        }
        editor.putStringSet(PREFS_EDIZMS, units).commit();
    }

    public int getPrimaryColor() {
        return sPref.getInt(PREFS_COLOR_PRIMARY, context.getResources().getColor(R.color.primary_color));
    }

    public void putPrimaryColor(int color) {
        editor.putInt(PREFS_COLOR_PRIMARY, color).commit();
    }

    public int getAccentColor() {
        return sPref.getInt(PREFS_COLOR_ACCENT, context.getResources().getColor(R.color.accent));
    }

    public void putAccentColor(int color) {
        editor.putInt(PREFS_COLOR_ACCENT, color).commit();
    }

    public int getTheme() {
        return sPref.getInt(PREFS_THEME, -1);
    }

    public void putTheme(int position) {
        editor.putInt(PREFS_THEME, position).commit();
    }

    public int getDialogTheme() {
        return sPref.getInt(PREFS_DIALOG_THEME, -1);
    }

    public void putDialogTheme(int position) {
        editor.putInt(PREFS_DIALOG_THEME, position).commit();
    }

    public int getHeader() {
        return sPref.getInt(PREFS_HEADER, -1);
    }

    public void putHeader(int position) {
        editor.putInt(PREFS_HEADER, position).commit();
    }

    public boolean replaceBuyed(){
        return sPref.getBoolean(PREFS_REPLACE_BUYED, true);
    }

    public void putReplaceBuyed(boolean value){
        editor.putBoolean(PREFS_REPLACE_BUYED, value).commit();
    }

    public boolean showCategories(){
        return sPref.getBoolean(PREFS_SHOW_CATEGORIES, true);
    }

    public void putShowCategories(boolean value){
        editor.putBoolean(PREFS_SHOW_CATEGORIES, value).commit();
    }

    public String getSortingListItems(){
        return sPref.getString(PREFS_SORT_LIST_ITEM, context.getString(R.string.prefs_default_sort_listitem_one));
    }

    public void putSortingListItems(String value){
        editor.putString(PREFS_SORT_LIST_ITEM, value).commit();
    }

    public Set<String> getEdizms() {
        return sPref.getStringSet(PREFS_EDIZMS, new LinkedHashSet<>());
    }

    public void putEdizms(List<String> units){
        Set<String> set = new LinkedHashSet<>(units);
        editor.putStringSet(PREFS_EDIZMS, set).commit();
    }

    public String getDefaultEdizm() {
        return sPref.getString(PREFS_DEF_EDIZM, context.getResources().getString(R.string.default_unit_one));
    }

    public void putDefaultEdizm(String value){
        editor.putString(PREFS_DEF_EDIZM, value).commit();
    }

    public Set<String> getShops() {
        return sPref.getStringSet(PREFS_SHOPES, new LinkedHashSet<>());
    }

    public void putShopes(ArrayList<String> shops) {
        Set<String> set = new LinkedHashSet<>(shops);
        editor.putStringSet(PREFS_SHOPES, set).commit();
    }

    public String getCurrentSortingList() {
        return sPref.getString(PreferencesManager.PREFS_SORT_LISTS, context.getString(R.string.prefs_default_sort_listitem_one));
    }

    public String getCurrentFilteringList() {
        return sPref.getString(PreferencesManager.PREFS_FILTER_LIST, context.getString(R.string.prefs_filter_list_default_one));
    }

    public boolean isFilterListOnlyProductsOfShop(){
        return getCurrentFilteringList().equals(context.getString(R.string.prefs_filter_list_default_one));
    }

    public void putCurrentCurrency(String selectedCurrency) {
        editor.putString(PREFS_DEF_CURRENCY, selectedCurrency).commit();
    }

    public void putCurrencies(ArrayList<String> currencies) {
        Set<String> set = new LinkedHashSet<>(currencies);
        editor.putStringSet(PREFS_CURRENCYS, set).commit();
    }

    public Comparator<ShoppingList> getShoppingListComparator() {
        if (getCurrentSortingList().equals(context.getString(R.string.prefs_default_sort_listitem_one))){
            return new ShoppingListAlphabetComparator();
        } else {
            return new ShoppingListDateComparator();
        }
    }

    public Comparator<ListItemDecorator> getListItemDecoratorComparator() {
        Comparator<ListItemDecorator> result;
        if (getSortingListItems().equals(context.getString(R.string.prefs_default_sort_listitem_one))){
            result = new ListItemDecoratorAlphabetAZComparator();
        } else if (getSortingListItems().equals(context.getString(R.string.prefs_default_sort_listitem_two))){
            result = new ListItemDecoratorAlphabetZAComparator();
        } else {
            result = new ListItemDecoratorAddedComparator();
        }
        result = new ListItemDecoratorHeaderComparator(result);
        if (showCategories()){
            result = new ListItemDecoratorCategoryComparator(result);
        }
        if (replaceBuyed()){
            result = new ListItemDecoratorBuyedComparator(result);
        }
        return result;
    }

    public void putAuthorComment(String author) {
        editor.putString(PREFS_AUTHOR_COMMENT, author).commit();
    }

    public String getAuthorCOmment(){
        return sPref.getString(PREFS_AUTHOR_COMMENT, "");
    }

    public String getThemeLightDark() {
        return sPref.getString(PREFS_THEME_LIGHT_DARK, context.getString(R.string.prefs_light_theme));
    }

    public boolean isOpenDefaultList(){
        return sPref.getBoolean(PREFS_OPEN_DEFAULT_LIST, false);
    }




    public boolean isTapTargetForShopsFilterShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SHOPS_FILTER, false);
    }

    public void putTapTargetForShopsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SHOPS_FILTER, shown).commit();
    }

    public boolean isTapTargetForShopsFavoriteShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SHOPS_FAVORITE, false);
    }

    public void putTapTargetForShopsFavorite(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SHOPS_FAVORITE, shown).commit();
    }

    public boolean isTapTargetForSalesFilterByShopsShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_SHOPS, false);
    }

    public void putTapTargetForSalesByShopsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_SHOPS, shown).commit();
    }

    public boolean isTapTargetForSalesSearchShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALES_SEARCH, false);
    }

    public void putTapTargetForSalesSearch(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALES_SEARCH, shown).commit();
    }

    public boolean isTapTargetForSaleCommentsShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALE_COMMENTS, false);
    }

    public void putTapTargetForSaleComments(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALE_COMMENTS, shown).commit();
    }

    public boolean isTapTargetForSaleSharingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALE_SHARING, false);
    }

    public void putTapTargetForSaleSharing(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALE_SHARING, shown).commit();
    }

    public boolean isTapTargetForSaleSendingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALE_SENDING, false);
    }

    public void putTapTargetForSaleSending(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALE_SENDING, shown).commit();
    }

    public boolean isTapTargetForCategsFilterShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CATEGS_FILTER, false);
    }

    public void putTapTargetForCategsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CATEGS_FILTER, shown).commit();
    }

    public boolean isTapTargetForCategsFavoriteShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CATEGS_FAVORITE, false);
    }

    public void putTapTargetForCategsFavorite(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CATEGS_FAVORITE, shown).commit();
    }

    public boolean isTapTargetForSalesFilterByCategsShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_CATEGS, false);
    }

    public void putTapTargetForSalesByCategsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_CATEGS, shown).commit();
    }

    public boolean isTapTargetForCitiesSearch() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CITIES_SEARCH, false);
    }

    public void putTapTargetForCitiesSearch(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CITIES_SEARCH, shown).commit();
    }

    public boolean isTapTargetForCitySelect() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CITY_SELECT, false);
    }

    public void putTapTargetForCitySelect(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CITY_SELECT, shown).commit();
    }

    public boolean isTapTargetForSalesFilterByCatalogsShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_CATALOG, false);
    }

    public void putTapTargetForSalesByCatalogsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALES_FILTER_BY_CATALOG, shown).commit();
    }



    public boolean isTapTargetForSOSaleSendingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SO_SALE_SENDING, false);
    }

    public void putTapTargetForSOSaleSending(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SO_SALE_SENDING, shown).commit();
    }

    public boolean isTapTargetForSOSaleCommentsShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SO_SALE_COMMENTS, false);
    }

    public void putTapTargetForSOSaleComments(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SO_SALE_COMMENTS, shown).commit();
    }

    public boolean isTapTargetForSaleSOCroppingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SO_SALE_CROPPING, false);
    }

    public void putTapTargetForSOSaleCropping(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SO_SALE_CROPPING, shown).commit();
    }

    public boolean isTapTargetForSaleSOSharingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SO_SALE_SHARING, false);
    }

    public void putTapTargetForSOSaleSharing(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SO_SALE_SHARING, shown).commit();
    }

    public boolean isTapTargetForCroppingRotateSkipShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CROPPING_ROTATE_SKIP, false);
    }

    public void putTapTargetForCroppingRotateSkip(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CROPPING_ROTATE_SKIP, shown).commit();
    }

    public boolean isTapTargetForCroppingCropShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CROPPING_CROP, false);
    }

    public void putTapTargetForCroppingCrop(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CROPPING_CROP, shown).commit();
    }

    public boolean isTapTargetForCroppingScaleSkipShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CROPPING_SCALE_SKIP, false);
    }

    public void putTapTargetForCroppingScaleSkip(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CROPPING_SCALE_SKIP, shown).commit();
    }

    public boolean isTapTargetForListsCreateShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_LISTS_CREATE, false);
    }

    public void putTapTargetForListsCreate(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_LISTS_CREATE, shown).commit();
    }


    public boolean isTapTargetForItemsCreateShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEMS_CREATE, false);
    }

    public void putTapTargetForItemsCreate(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEMS_CREATE, shown).commit();
    }

    public boolean isTapTargetForItemsAddShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEMS_ADD, false);
    }

    public void putTapTargetForItemsAdd(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEMS_ADD, shown).commit();
    }

    public boolean isTapTargetForItemInfoShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEM_INFO, false);
    }

    public void putTapTargetForItemInfo(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEM_INFO, shown).commit();
    }

    public boolean isTapTargetForItemsFilterByShopShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEMS_FILTER_BY_SHOP, false);
    }

    public void putTapTargetForItemsFilterByShop(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEMS_FILTER_BY_SHOP, shown).commit();
    }

    //
    public boolean isTapTargetForItemsAddToActionModeShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEMS_ADD_TO_ACTION_MODE, false);
    }

    public void putTapTargetForItemsAddToActionMode(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEMS_ADD_TO_ACTION_MODE, shown).commit();
    }

    public boolean isTapTargetForItemEditInActionModeShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEM_EDIT_IN_ACTION_MODE, false);
    }

    public void putTapTargetForItemEditInActionMode(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEM_EDIT_IN_ACTION_MODE, shown).commit();
    }

    public boolean isTapTargetForItemsRemoveInActionModeShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_ITEMS_REMOVE_IN_ACTION_MODE, false);
    }

    public void putTapTargetForItemsRemoveInActionMode(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_ITEMS_REMOVE_IN_ACTION_MODE, shown).commit();
    }

    public boolean isTapTargetForCloseActionModeShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_CLOSE_ACTION_MODE, false);
    }

    public void putTapTargetForCloseActionMode(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_CLOSE_ACTION_MODE, shown).commit();
    }

    //
    public boolean isTapTargetForProductsSearchShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_PRODUCTS_SEARCH, false);
    }

    public void putTapTargetForProductsSearch(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_PRODUCTS_SEARCH, shown).commit();
    }

    public boolean isTapTargetForProductsFilterShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_PRODUCTS_FILTER, false);
    }

    public void putTapTargetForProductsFilter(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_PRODUCTS_FILTER, shown).commit();
    }

    public boolean isTapTargetForProductsSortingShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_PRODUCTS_SORTING, false);
    }

    public void putTapTargetForProductsSorting(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_PRODUCTS_SORTING, shown).commit();
    }

    public boolean isTapTargetForProductsReturnShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_PRODUCTS_RETURN, false);
    }

    public void putTapTargetForProductsReturn(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_PRODUCTS_RETURN, shown).commit();
    }

    public boolean isTapTargetForSalesFavoriteShown() {
        return sPref.getBoolean(PREFS_TAP_TARGET_SALES_FAVORITE, false);
    }

    public void putTapTargetForSalesFavorite(boolean shown) {
        editor.putBoolean(PREFS_TAP_TARGET_SALES_FAVORITE, shown).commit();
    }
}