package com.PopCorp.Purchases.presentation.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.AlarmListCallback;
import com.PopCorp.Purchases.data.callback.CreateEditListCallback;
import com.PopCorp.Purchases.data.callback.DateTimeCallback;
import com.PopCorp.Purchases.data.callback.DialogFavoriteCallback;
import com.PopCorp.Purchases.data.callback.DialogRegionsCallback;
import com.PopCorp.Purchases.data.callback.ShareListCallback;
import com.PopCorp.Purchases.data.comparator.CategoryComparator;
import com.PopCorp.Purchases.data.comparator.RegionComparator;
import com.PopCorp.Purchases.data.comparator.ShopComparator;
import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DialogController {

    public static void showDialogWithRegions(final Context context, final List<Region> regions, final DialogRegionsCallback callback) {
        Collections.sort(regions, new RegionComparator());
        ArrayList<String> items = new ArrayList<>();
        int selectedRegion = -1;
        for (Region region : regions) {
            items.add(region.getName());
            if (!PreferencesManager.getInstance().getRegionId().isEmpty() && region.getId() == Integer.valueOf(PreferencesManager.getInstance().getRegionId())) {
                selectedRegion = regions.indexOf(region);
            }
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(R.string.dialog_title_select_region);
        builder.items(items.toArray(new String[items.size()]));
        builder.itemsCallbackSingleChoice(selectedRegion, (dialog, view, which, text) -> {
            if (which == -1) {
                Toast.makeText(context, R.string.toast_please_select_region, Toast.LENGTH_SHORT).show();
                return false;
            }
            PreferencesManager.getInstance().putRegion(String.valueOf(regions.get(which).getId()));
            callback.onSelected(regions.get(which));
            dialog.dismiss();
            return true;
        });
        builder.onNegative((dialog, which) -> dialog.cancel());
        builder.cancelListener(dialog -> callback.onCancel());
        builder.positiveText(R.string.dialog_button_select);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.autoDismiss(false);
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void showDialogForFavoriteShops(final Context context, final ArrayList<Shop> shops, final DialogFavoriteCallback<Shop> callback) {
        Collections.sort(shops, new ShopComparator());
        String[] names = new String[shops.size()];
        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            names[i] = shop.getName();
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(R.string.dialog_title_select_favorite_shops);
        builder.items(names);
        builder.itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {
            if (which.length == 0) {
                Toast.makeText(context, R.string.toast_please_select_favorite_shops, Toast.LENGTH_SHORT).show();
                return true;
            }
            ArrayList<Shop> result = new ArrayList<>();
            ShopDAO shopDAO = new ShopDAO();
            for (int i : which) {
                Shop shop = shops.get(i);
                result.add(shop);
                shop.setFavorite(true);
                shopDAO.updateOrAddToDB(shop);
            }
            callback.onSelected(result);
            dialog.dismiss();
            return true;
        });
        builder.onNegative((dialog, which) -> dialog.cancel());
        builder.cancelListener(dialog -> callback.onCancel());
        builder.positiveText(R.string.dialog_button_select);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.autoDismiss(false);
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showDialogForFavoriteCategories(final Context context, final ArrayList<Category> categories, final DialogFavoriteCallback<Category> callback) {
        Collections.sort(categories, new CategoryComparator());
        String[] names = new String[categories.size()];
        Integer[] integers = new Integer[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            names[i] = category.getName();
            integers[i] = i;
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(R.string.dialog_title_select_favorite_categories);
        builder.items(names);
        builder.itemsCallbackMultiChoice(integers, (dialog, which, text) -> {
            if (which.length == 0) {
                Toast.makeText(context, R.string.toast_please_select_favorite_categories, Toast.LENGTH_SHORT).show();
                return true;
            }
            ArrayList<Category> result = new ArrayList<>();
            CategoryDAO categoryDAO = new CategoryDAO();
            for (int i : which) {
                Category category = categories.get(i);
                result.add(category);
                category.setFavorite(true);
                categoryDAO.updateOrAddToDB(category);
            }
            callback.onSelected(result);
            dialog.dismiss();
            return true;
        });
        builder.onNegative((dialog, which) -> dialog.cancel());
        builder.cancelListener(dialog -> callback.onCancel());
        builder.positiveText(R.string.dialog_button_select);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.autoDismiss(false);
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showDialogForNewList(final Context activity, final CreateEditListCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_new_list, null);

        final EditText edittextForName = (EditText) layout.findViewById(R.id.edittext_name);
        final TextInputLayout inputLayout = (TextInputLayout) layout.findViewById(R.id.input_layout);
        final Spinner spinnerForCurrency = (Spinner) layout.findViewById(R.id.spinner_currency);

        Set<String> currencys = PreferencesManager.getInstance().getCurrencies();
        ArrayAdapter<String> adapterForSpinnerCurrency = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, currencys.toArray(new String[currencys.size()]));
        adapterForSpinnerCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForCurrency.setAdapter(adapterForSpinnerCurrency);
        spinnerForCurrency.setSelection(adapterForSpinnerCurrency.getPosition(PreferencesManager.getInstance().getCurrentCurrency()));

        builder.title(R.string.dialog_title_new_list);
        builder.autoDismiss(false);
        builder.customView(layout, false);
        builder.positiveText(R.string.dialog_button_create);
        builder.negativeText(R.string.dialog_button_cancel);

        builder.onPositive((dialog, which) -> {
            if (checkNameAndCreateNewList(edittextForName.getText().toString(), (String) spinnerForCurrency.getSelectedItem(), callback)) {
                dialog.dismiss();
            } else{
                inputLayout.setError(activity.getString(R.string.error_enter_list_name));
            }
        });
        builder.onNegative((dialog, which) -> {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edittextForName.getWindowToken(), 0);
            dialog.dismiss();
        });

        final Dialog dialog = builder.build();

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        edittextForName.setOnEditorActionListener((v, actionId, event) -> {
            if (checkNameAndCreateNewList(edittextForName.getText().toString(), (String) spinnerForCurrency.getSelectedItem(), callback)) {
                dialog.dismiss();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextForName.getWindowToken(), 0);
            } else{
                inputLayout.setError(activity.getString(R.string.error_enter_list_name));
            }
            return true;
        });
    }

    private static boolean checkNameAndCreateNewList(String name, String currency, CreateEditListCallback callback) {
        if (name.isEmpty()) {
            return false;
        }
        callback.addNewList(name, currency);
        return true;
    }

    public static void showDialogForEditingList(final Context context, final ShoppingList list, final CreateEditListCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_new_list, null);
        final EditText edittextForName = (EditText) layout.findViewById(R.id.edittext_name);
        final TextInputLayout inputLayout = (TextInputLayout) layout.findViewById(R.id.input_layout);
        final Spinner spinnerForCurrency = (Spinner) layout.findViewById(R.id.spinner_currency);
        edittextForName.setText(list.getName());

        Set<String> currencys = PreferencesManager.getInstance().getCurrencies();
        ArrayAdapter<String> adapterForSpinnerCurrency = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, currencys.toArray(new String[currencys.size()]));
        adapterForSpinnerCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForCurrency.setAdapter(adapterForSpinnerCurrency);
        if (currencys.contains(list.getCurrency())) {
            spinnerForCurrency.setSelection(adapterForSpinnerCurrency.getPosition(list.getCurrency()));
        } else {
            spinnerForCurrency.setSelection(adapterForSpinnerCurrency.getPosition(PreferencesManager.getInstance().getCurrentCurrency()));
        }

        builder.title(R.string.dialog_title_new_list);
        builder.autoDismiss(false);
        builder.customView(layout, false);
        builder.positiveText(R.string.dialog_button_edit);
        builder.negativeText(R.string.dialog_button_cancel);

        builder.onPositive((dialog, which) -> {
            if (checkNameAndChangeList(list, edittextForName.getText().toString(), (String) spinnerForCurrency.getSelectedItem(), callback)) {
                dialog.dismiss();
            } else{
                inputLayout.setError(context.getString(R.string.error_enter_list_name));
            }
        });
        builder.onNegative((dialog, which) -> dialog.dismiss());

        final Dialog dialog = builder.build();

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        edittextForName.setOnEditorActionListener((v, actionId, event) -> {
            if (checkNameAndChangeList(list, edittextForName.getText().toString(), (String) spinnerForCurrency.getSelectedItem(), callback)) {
                dialog.dismiss();
            } else{
                inputLayout.setError(context.getString(R.string.error_enter_list_name));
            }
            return true;
        });
    }

    private static boolean checkNameAndChangeList(ShoppingList list, String name, String currency, CreateEditListCallback callback) {
        if (name.isEmpty()) {
            return false;
        }
        callback.onListEdited(list, name, currency);
        return true;
    }

    public static void showDialogForSendingList(final Context context, final ShoppingList list, ShareListCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(R.string.dialog_title_share_list);
        builder.items(R.array.list_sharing_items);
        builder.itemsCallback((dialog, itemView, which, text) -> {
            switch (which){
                case 0:
                    callback.shareAsSMS(list);
                    break;
                case 1:
                    callback.shareAsEmail(list);
                    break;
                case 2:
                    callback.shareAsText(list);
                    break;
            }
        });
        Dialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showDialogForAlarm(Activity activity, ShoppingList list, AlarmListCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_alarm, null);

        final Calendar alarmDate = Calendar.getInstance();
        if (list.getAlarm() != 0){
            alarmDate.setTimeInMillis(list.getAlarm());
        }

        final TextView buttonDate = (TextView) layout.findViewById(R.id.alarm_date);
        final TextView buttonTime = (TextView) layout.findViewById(R.id.alarm_time);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("ru"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, yyyy", new Locale("ru"));
        buttonDate.setText(dateFormat.format(alarmDate.getTime()));
        buttonTime.setText(timeFormat.format(alarmDate.getTime()));

        DateTimeCallback dateTimeCallback = new DateTimeCallback(){

            @Override
            public void onDateSelected(Calendar date) {
                alarmDate.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
                alarmDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
                alarmDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
                buttonDate.setText(dateFormat.format(alarmDate.getTime()));
            }

            @Override
            public void onTimeSelected(Calendar time) {
                alarmDate.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
                alarmDate.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
                buttonTime.setText(timeFormat.format(alarmDate.getTime()));
            }
        };
        buttonTime.setOnClickListener(v -> showTimePickerDialog(activity, alarmDate, dateTimeCallback));
        buttonDate.setOnClickListener(v -> showDatePickerDialog(activity, alarmDate, dateTimeCallback));

        if (list.getAlarm() != 0) {
            builder.neutralText(R.string.dialog_button_remove);
            builder.onNeutral((dialog, which) -> callback.removeAlarm(list));
        }

        builder.title(R.string.dialog_title_list_alarm);
        builder.customView(layout, true);
        builder.positiveText(R.string.dialog_button_ok);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.onPositive((dialog, which) -> callback.setAlarm(list, alarmDate));
        Dialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void showDatePickerDialog(Activity activity, Calendar date, final DateTimeCallback callback) {
        if (Build.VERSION.SDK_INT > 20) {
            showMaterialDatePickerDialog(activity, date, callback);
            return;
        }
        DatePickerDialog.OnDateSetListener dateListener = (view, year, monthOfYear, dayOfMonth) -> {
            Calendar date1 = Calendar.getInstance();
            date1.set(year, monthOfYear, dayOfMonth);
            callback.onDateSelected(date1);
        };
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                dateListener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setAccentColor(ThemeManager.getInstance().getPrimaryColor());
        dialog.setMaxDate(Calendar.getInstance());
        dialog.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    private static void showMaterialDatePickerDialog(Context activity, Calendar date, final DateTimeCallback callback) {
        android.app.DatePickerDialog.OnDateSetListener dateListener = (view, year, monthOfYear, dayOfMonth) -> {
            Calendar date1 = Calendar.getInstance();
            date1.set(year, monthOfYear, dayOfMonth);
            callback.onDateSelected(date1);
        };
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(activity,
                ThemeManager.getInstance().getDialogThemeRes(),
                dateListener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
        Calendar max = Calendar.getInstance();
        max.set(Calendar.HOUR_OF_DAY, 23);
        max.set(Calendar.MINUTE, 59);
        dialog.getDatePicker().setMaxDate(max.getTimeInMillis());
    }

    public static void showTimePickerDialog(Activity activity, Calendar date, final DateTimeCallback callback) {
        if (Build.VERSION.SDK_INT > 20) {
            showMaterialTimePickerDialog(activity, date, callback);
            return;
        }
        TimePickerDialog.OnTimeSetListener timeListener = (view, hourOfDay, minute, second) -> {
            Calendar date1 = Calendar.getInstance();
            date1.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date1.set(Calendar.MINUTE, minute);
            date1.set(Calendar.SECOND, 0);
            callback.onTimeSelected(date1);
        };
        TimePickerDialog dialog = TimePickerDialog.newInstance(
                timeListener,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE),
                true
        );
        dialog.setAccentColor(ThemeManager.getInstance().getPrimaryColor());
        dialog.setThemeDark(false);
        dialog.show(activity.getFragmentManager(), "Timepickerdialog");
    }

    private static void showMaterialTimePickerDialog(Context activity, Calendar date, final DateTimeCallback callback) {
        android.app.TimePickerDialog dialog = new android.app.TimePickerDialog(activity,
                ThemeManager.getInstance().getDialogThemeRes(),
                (view, hourOfDay, minute) -> {
                    Calendar date1 = Calendar.getInstance();
                    date1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date1.set(Calendar.MINUTE, minute);
                    date1.set(Calendar.SECOND, 0);
                    callback.onTimeSelected(date1);
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);
        dialog.show();
    }
}
