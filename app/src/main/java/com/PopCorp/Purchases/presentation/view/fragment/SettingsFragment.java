package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.DialogRegionsCallback;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.ColorDialog;
import com.PopCorp.Purchases.presentation.common.MvpPreferenceFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.SettingsPresenter;
import com.PopCorp.Purchases.presentation.view.activity.MainActivity;
import com.PopCorp.Purchases.presentation.view.activity.SelectingCityActivity;
import com.PopCorp.Purchases.presentation.view.moxy.SettingsView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SettingsFragment extends MvpPreferenceFragment implements SettingsView {

    @InjectPresenter
    SettingsPresenter presenter;

    private Toolbar toolBar;
    private ListView listView;
    private int color;
    private String selectedCurrency;
    private String selectedUnit;
    private MaterialDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        initializePrefs();
        if (getPreferenceScreen() != null) {
            ArrayList<Preference> preferences = getAllPreferenceScreen(getPreferenceScreen(), new ArrayList<Preference>());
            for (Preference preference : preferences) {
                preferenceToMaterialPreference(preference);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setTitle(R.string.navigation_drawer_settings);
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private ArrayList<Preference> getAllPreferenceScreen(Preference preference, ArrayList<Preference> list) {
        if (preference instanceof PreferenceCategory || preference instanceof PreferenceScreen) {
            PreferenceGroup pGroup = (PreferenceGroup) preference;
            int pCount = pGroup.getPreferenceCount();
            list.add(preference);
            for (int i = 0; i < pCount; i++) {
                getAllPreferenceScreen(pGroup.getPreference(i), list);
            }
        }
        return list;
    }

    private void preferenceToMaterialPreference(Preference preference) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (preference instanceof PreferenceScreen && preference.getLayoutResource() != R.layout.mp_preference_material) {
                preference.setLayoutResource(R.layout.mp_preference_material);
            } else if (preference instanceof PreferenceCategory && preference.getLayoutResource() != R.layout.mp_preference_category) {
                preference.setLayoutResource(R.layout.mp_preference_category);
                PreferenceCategory category = (PreferenceCategory) preference;
                for (int j = 0; j < category.getPreferenceCount(); j++) {
                    Preference basicPreference = category.getPreference(j);
                    if (!(basicPreference instanceof PreferenceCategory || basicPreference instanceof PreferenceScreen)) {
                        if (basicPreference.getLayoutResource() != R.layout.mp_preference_material_widget) {
                            basicPreference.setLayoutResource(R.layout.mp_preference_material_widget);
                        }
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_settings, null);
        View rootView = super.onCreateView(inflater, null, savedInstanceState);
        ((ViewGroup) mainView.findViewById(R.id.layout)).addView(rootView);
        if (rootView != null) {
            listView = (ListView) rootView.findViewById(android.R.id.list);
            listView.setClipToPadding(false);
            listView.setFooterDividersEnabled(false);
            listView.setHeaderDividersEnabled(false);
            if (Build.VERSION.SDK_INT < 21) {
                listView.setSelector(R.drawable.list_selector);
                //listView.setPadding((int) getResources().getDimension(R.dimen.margin_item), 0, (int) getResources().getDimension(R.dimen.margin_item), 0);
            }
        }

        toolBar = (Toolbar) mainView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            toolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
        return mainView;
    }

    private void initializePrefs() {
        final Preference prefSortListItem = findPreference(PreferencesManager.PREFS_SORT_LIST_ITEM);
        if (prefSortListItem != null) {
            prefSortListItem.setSummary(getString(R.string.prefs_default_sort) + " " + PreferencesManager.getInstance().getSortingListItems());
            prefSortListItem.setOnPreferenceChangeListener((preference, newValue) -> {
                prefSortListItem.setSummary(getString(R.string.prefs_default_sort) + " " + newValue);
                return true;
            });
        }

        final Preference prefFontSize = findPreference(PreferencesManager.PREFS_LIST_ITEM_FONT_SIZE);
        if (prefFontSize != null) {
            prefFontSize.setSummary(getString(R.string.prefs_text_size_summary) + " " + PreferencesManager.getInstance().getListItemFontSize());
            prefFontSize.setOnPreferenceChangeListener((preference, newValue) -> {
                prefFontSize.setSummary(getString(R.string.prefs_text_size_summary) + " " + newValue);
                return true;
            });
        }

        final Preference prefFontSizeSmall = findPreference(PreferencesManager.PREFS_LIST_ITEM_FONT_SIZE_SMALL);
        if (prefFontSizeSmall != null) {
            prefFontSizeSmall.setSummary(getString(R.string.prefs_text_size_summary_small) + " " + PreferencesManager.getInstance().getListItemFontSizeSmall());
            prefFontSizeSmall.setOnPreferenceChangeListener((preference, newValue) -> {
                prefFontSizeSmall.setSummary(getString(R.string.prefs_text_size_summary_small) + " " + newValue);
                return true;
            });
        }

        final Preference prefCategories = findPreference(PreferencesManager.PREFS_CATEGORIES);
        if (prefCategories != null) {
            prefCategories.setOnPreferenceClickListener(preference -> {
                presenter.showCategories();
                return true;
            });
        }

        Preference prefCurrency = findPreference(PreferencesManager.PREFS_CURRENCY);
        if (prefCurrency != null) {
            prefCurrency.setSummary(getString(R.string.prefs_default_currency) + " " + PreferencesManager.getInstance().getCurrentCurrency());
            prefCurrency.setOnPreferenceClickListener(preference -> {
                presenter.showCurrencies();
                return true;
            });
        }

        Preference prefUnit = findPreference(PreferencesManager.PREFS_UNIT);
        if (prefUnit != null) {
            prefUnit.setSummary(getString(R.string.prefs_default_unit) + " " + PreferencesManager.getInstance().getDefaultEdizm());
            prefUnit.setOnPreferenceClickListener(preference -> {
                presenter.showUnits();
                return true;
            });
        }

        final Preference prefSortList = findPreference(PreferencesManager.PREFS_SORT_LISTS);
        if (prefSortList != null) {
            prefSortList.setSummary(getString(R.string.prefs_default_sort) + " " + PreferencesManager.getInstance().getCurrentSortingList());
            prefSortList.setOnPreferenceChangeListener((preference, newValue) -> {
                prefSortList.setSummary(getString(R.string.prefs_default_sort) + " " + newValue);
                return true;
            });
        }

        Preference shopes = findPreference(PreferencesManager.PREFS_SHOPES);
        if (shopes != null) {
            shopes.setOnPreferenceClickListener(preference -> {
                showDialogWithShops();
                return true;
            });
        }

        final Preference prefFilter = findPreference(PreferencesManager.PREFS_FILTER_LIST);
        if (prefFilter != null) {
            prefFilter.setSummary(PreferencesManager.getInstance().getCurrentFilteringList());
            prefFilter.setOnPreferenceChangeListener((preference, newValue) -> {
                prefFilter.setSummary((CharSequence) newValue);
                return true;
            });
        }

        Preference prefRegion = findPreference(PreferencesManager.PREFS_CITY);
        if (prefRegion != null) {
            prefRegion.setSummary(presenter.getSelectedRegion());
            prefRegion.setOnPreferenceClickListener(preference -> {
                presenter.loadRegions();
                return true;
            });
        }

        Preference prefCity = findPreference(PreferencesManager.PREFS_SKIDKAONLINE_CITY);
        if (prefCity != null) {
            prefCity.setSummary(presenter.getSelectedCity());
            prefCity.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), SelectingCityActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SELECTING_CITY_ACTIVITY);
                return true;
            });
        }

        Preference prefDisplayNoOff = findPreference(PreferencesManager.PREFS_DISPLAY_NO_OFF);
        if (prefDisplayNoOff != null) {
            prefDisplayNoOff.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (boolean) newValue;
                listView.setKeepScreenOn(value);
                return true;
            });
        }

        Preference about = findPreference(PreferencesManager.PREFS_ABOUT);
        if (about != null) {
            about.setOnPreferenceClickListener(preference -> {
                //showAboutActivity();
                return true;
            });
        }
    }

    public void selectCurrency(String selectedCurrency) {
        Preference prefCurrency = findPreference(PreferencesManager.PREFS_CURRENCY);
        if (prefCurrency != null) {
            prefCurrency.setSummary(getString(R.string.prefs_default_currency) + " " + selectedCurrency);
        }
    }

    public void selectUnit(String selectedUnit) {
        Preference prefUnit = findPreference(PreferencesManager.PREFS_UNIT);
        if (prefUnit != null) {
            prefUnit.setSummary(getString(R.string.prefs_default_unit) + " " + selectedUnit);
        }
    }

    public void selectRegion(String selectedCity) {
        Preference prefCity = findPreference(PreferencesManager.PREFS_CITY);
        if (prefCity != null) {
            prefCity.setSummary(selectedCity);
        }
    }

    public void selectCity(String selectedCity) {
        Preference prefCity = findPreference(PreferencesManager.PREFS_SKIDKAONLINE_CITY);
        if (prefCity != null) {
            prefCity.setSummary(selectedCity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.REQUEST_CODE_FOR_SELECTING_CITY_ACTIVITY) {
                selectCity(presenter.getSelectedCity());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showDialogWithCategories(List<ListItemCategory> categories) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        ArrayList<String> names = new ArrayList<>();
        for (ListItemCategory category : categories) {
            names.add(category.getName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_listitem_category_for_dialog, names) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.item_listitem_category_for_dialog, parent, false);
                }
                ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
                coloredCircle.getPaint().setColor(categories.get(position).getColor());
                view.findViewById(R.id.category_image).setBackgroundDrawable(coloredCircle);
                ((TextView) view.findViewById(R.id.category_name)).setText(categories.get(position).getName());
                return view;
            }
        };
        builder.adapter(adapter, (dialog, itemView, which, text) -> {
            showDialogForCategoryChange(categories.get(which));
            dialog.cancel();
        });
        builder.title(R.string.prefs_categories_of_products);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.positiveText(R.string.dialog_button_add);
        builder.onPositive((dialog, which) -> showDialogForCategoryChange(null));
        Dialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDialogForCategoryChange(final ListItemCategory category) {
        color = ThemeManager.getInstance().getAccentColor();
        if (category != null) {
            color = category.getColor();
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_category_edit, null);
        builder.customView(layout, true);
        builder.title(category == null ? R.string.dialog_title_new_category : R.string.dialog_title_edit_category);

        final EditText editTextCategoryName = (EditText) layout.findViewById(R.id.category_name);
        final TextInputLayout categoryNameLayout = (TextInputLayout) layout.findViewById(R.id.category_name_layout);
        final View categoryColor = layout.findViewById(R.id.category_color);
        final Button selectColor = (Button) layout.findViewById(R.id.select_color);
        selectColor.setOnClickListener(v ->
                new ColorDialog.Builder((AppCompatActivity) getActivity(),
                        (dialog, selectedColor) -> {
                            color = selectedColor;
                            categoryColor.setBackgroundColor(selectedColor);
                        }, R.string.dialog_title_selecting_color)
                        .titleSub(R.string.dialog_title_selecting_color)
                        .allowUserColorInput(false)
                        .accentMode(false)
                        .doneButton(R.string.dialog_button_select)
                        .backButton(R.string.dialog_button_back)
                        .cancelButton(R.string.dialog_button_cancel)
                        .preselect(color)
                        .show());
        if (category != null) {
            editTextCategoryName.setText(category.getName());
        }
        categoryColor.setBackgroundColor(color);

        builder.autoDismiss(false);
        builder.neutralText(R.string.dialog_button_cancel);
        builder.onPositive((dialog, which) -> onCategorySaveClicked(dialog, categoryNameLayout, editTextCategoryName, category));
        builder.onNegative((dialog, which) -> {
            presenter.removeCategory(category);
            dialog.dismiss();
        });
        builder.onNeutral((dialog, which) -> dialog.dismiss());
        if (category != null) {
            builder.negativeText(R.string.dialog_button_remove);
            builder.positiveText(R.string.dialog_button_save);
        } else {
            builder.positiveText(R.string.dialog_button_add);
        }
        builder.dismissListener(dialog -> presenter.showCategories());
        final Dialog dialog = builder.build();
        editTextCategoryName.setOnEditorActionListener((v, actionId, event) -> {
            onCategorySaveClicked(dialog, categoryNameLayout, editTextCategoryName, category);
            return false;
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void onCategorySaveClicked(Dialog dialog, TextInputLayout categoryNameLayout, EditText editTextCategoryName, ListItemCategory category) {
        if (editTextCategoryName.getText().toString().length() > 0) {
            if (presenter.saveCategory(category, editTextCategoryName.getText().toString(), color)) {
                dialog.dismiss();
            } else {
                categoryNameLayout.setError(getString(R.string.error_category_exists));
            }
        } else {
            categoryNameLayout.setError(getString(R.string.error_enter_category_name));
        }
    }


    @Override
    public void showDialogWithCurrencies(ArrayList<String> currencies) {
        final String currentCurrency = PreferencesManager.getInstance().getCurrentCurrency();
        selectedCurrency = currentCurrency;
        currencies.add(0, getString(R.string.add_new_currency));

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.prefs_currency);
        builder.autoDismiss(false);

        builder.items(currencies.toArray(new String[currencies.size()]));
        builder.alwaysCallSingleChoiceCallback();
        builder.itemsCallbackSingleChoice(currencies.indexOf(currentCurrency), (dialog, view, which, text) -> {
            selectedCurrency = currencies.get(which);
            return true;
        });

        builder.positiveText(R.string.dialog_button_select);
        builder.neutralText(R.string.dialog_button_cancel);
        builder.negativeText(R.string.dialog_button_remove);

        builder.onPositive((dialog, which) -> {
            if (selectedCurrency.equals(currencies.get(0))) {
                showDialogForAddCurrency();
                dialog.dismiss();
                return;
            }
            if (!selectedCurrency.equals(currentCurrency)) {
                PreferencesManager.getInstance().putCurrentCurrency(selectedCurrency);
                selectCurrency(selectedCurrency);
            }
            dialog.dismiss();
        });
        builder.onNegative((dialog, which) -> {
            if (selectedCurrency.equals(currencies.get(0)) || currencies.size() < 3 || selectedCurrency.equals(currentCurrency)) {
                return;
            }
            currencies.remove(selectedCurrency);
            currencies.remove(getString(R.string.add_new_currency));
            PreferencesManager.getInstance().putCurrencies(currencies);
            dialog.dismiss();
        });
        builder.onNeutral((dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDialogForAddCurrency() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_input, null);
        final EditText input = (EditText) customView.findViewById(R.id.edittext);
        final TextInputLayout inputLayout = (TextInputLayout) customView.findViewById(R.id.input_layout);
        inputLayout.setHint(getString(R.string.hint_currency_name));
        input.setAllCaps(true);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_new_currency);
        builder.positiveText(R.string.dialog_button_add);
        builder.autoDismiss(false);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.dismissListener(dialog -> presenter.showCurrencies());
        builder.onPositive((dialog, which) -> {
            if (addNewCurrency(input, inputLayout)) {
                dialog.dismiss();
            }
        });
        builder.onNegative((dialog, which) -> dialog.dismiss());
        builder.customView(customView, false);
        final Dialog dialog = builder.build();
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (addNewCurrency(input, inputLayout)) {
                dialog.dismiss();
            }
            return false;
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private boolean addNewCurrency(EditText input, TextInputLayout inputLayout) {
        String newCurrency = input.getText().toString();
        if (newCurrency.isEmpty()) {
            inputLayout.setError(getString(R.string.error_input_currency_name));
            return false;
        }
        if (presenter.addNewCurrency(newCurrency)) {
            return true;
        } else {
            inputLayout.setError(getString(R.string.error_currency_exists));
            return false;
        }
    }


    @Override
    public void showDialogWithUnits(ArrayList<String> units) {
        final String currentUnit = PreferencesManager.getInstance().getDefaultEdizm();
        selectedUnit = currentUnit;
        units.add(0, getString(R.string.add_new_unit));
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.prefs_unit);
        builder.autoDismiss(false);
        builder.onPositive((dialog, which) -> {
            if (selectedUnit.equals(units.get(0))) {
                showDialogForAddUnit();
                dialog.dismiss();
                return;
            }
            if (!selectedUnit.equals(currentUnit)) {
                PreferencesManager.getInstance().putDefaultEdizm(selectedUnit);
                selectUnit(selectedUnit);
            }
            dialog.dismiss();
        });
        builder.onNegative((dialog, which) -> {
            if (selectedUnit.equals(units.get(0)) || units.size() < 3 || selectedUnit.equals(currentUnit)) {
                return;
            }
            units.remove(selectedUnit);
            units.remove(getString(R.string.add_new_unit));
            PreferencesManager.getInstance().putEdizms(units);
            dialog.dismiss();
        });
        builder.onNeutral((dialog, which) -> dialog.dismiss());

        builder.items(units.toArray(new String[units.size()]));
        builder.alwaysCallSingleChoiceCallback();
        builder.itemsCallbackSingleChoice(units.indexOf(currentUnit), (dialog, view, which, text) -> {
            selectedUnit = units.get(which);
            return true;
        });
        builder.positiveText(R.string.dialog_button_select);
        builder.neutralText(R.string.dialog_button_cancel);
        builder.negativeText(R.string.dialog_button_remove);
        Dialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showProgress() {
        progressDialog = new MaterialDialog.Builder(getActivity())
                .content(R.string.dialog_title_loading_regions)
                .progress(true, 0)
                .build();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showSnackBar(int errorResource) {
        Snackbar.make(listView, errorResource, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSelectingRegions(List<Region> regions) {
        DialogController.showDialogWithRegions(getActivity(), regions, new DialogRegionsCallback() {

            @Override
            public void onSelected(Region region) {
                selectRegion(region.getName());
            }

            @Override
            public void onCancel() {
                if (PreferencesManager.getInstance().getRegionId().isEmpty()) {
                    showInfoDialogAboutRegions();
                }
            }
        });
    }

    private void showInfoDialogAboutRegions() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_info_about_regions);
        builder.content(R.string.dialog_content_info_about_regions);
        builder.positiveText(R.string.dialog_button_ok);
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showRegionsEmpty() {
        Snackbar.make(listView, R.string.error_no_shops, Snackbar.LENGTH_SHORT).show();
    }

    private void showDialogForAddUnit() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_input, null);
        final EditText input = (EditText) customView.findViewById(R.id.edittext);
        final TextInputLayout inputLayout = (TextInputLayout) customView.findViewById(R.id.input_layout);
        inputLayout.setHint(getString(R.string.hint_unit_name));

        final Dialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_title_new_unit)
                .positiveText(R.string.dialog_button_add)
                .negativeText(R.string.dialog_button_cancel)
                .autoDismiss(false)
                .customView(customView, false)
                .onPositive((dialog1, which) -> {
                    if (addNewUnit(input, inputLayout)) {
                        dialog1.dismiss();
                    }
                })
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .dismissListener(dialog1 -> presenter.showUnits())
                .build();
        input.setOnEditorActionListener((v, actionId, event) -> {
            addNewUnit(input, inputLayout);
            return false;
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private boolean addNewUnit(EditText input, TextInputLayout inputLayout) {
        String newUnit = input.getText().toString();
        if (newUnit.isEmpty()) {
            inputLayout.setError(getString(R.string.error_input_unit_name));
            return false;
        }
        if (presenter.addNewUnit(newUnit)) {
            return true;
        } else {
            inputLayout.setError(getString(R.string.error_unit_exists));
            return false;
        }
    }

    public void showDialogWithShops() {
        ArrayList<String> shopes = new ArrayList<>(PreferencesManager.getInstance().getShops());
        Dialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.prefs_shops)
                .items(shopes.toArray(new String[shopes.size()]))
                .itemsCallback((dialog1, view, which, text) -> showDialogForNewShop(text.toString()))
                .positiveText(R.string.dialog_button_add)
                .negativeText(R.string.dialog_button_cancel)
                .onPositive((dialog1, which) -> showDialogForNewShop(null))
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDialogForNewShop(String editingShop) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_input, null);
        final EditText input = (EditText) customView.findViewById(R.id.edittext);
        final TextInputLayout inputLayout = (TextInputLayout) customView.findViewById(R.id.input_layout);
        inputLayout.setHint(getString(R.string.hint_shop_name));
        if (editingShop != null){
            input.setText(editingShop);
        }

        final Dialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_title_new_shop)
                .positiveText(editingShop == null ? R.string.dialog_button_add : R.string.dialog_button_edit)
                .negativeText(R.string.dialog_button_cancel)
                .neutralText(editingShop == null ? 0 : R.string.dialog_button_remove)
                .autoDismiss(false)
                .customView(customView, false)
                .dismissListener(dialog1 -> showDialogWithShops())
                .onPositive((dialog1, which) -> {
                    String newShop = input.getText().toString();
                    if (newShop.isEmpty()) {
                        inputLayout.setError(getString(R.string.error_enter_shop_name));
                        return;
                    }
                    ArrayList<String> shopes = new ArrayList<>(PreferencesManager.getInstance().getShops());
                    if (!newShop.equals(editingShop) && shopes.contains(newShop)) {
                        inputLayout.setError(getString(R.string.error_shop_already_exists));
                    } else if (newShop.equals(editingShop)){
                        dialog1.dismiss();
                    } else {
                        shopes.remove(editingShop);
                        shopes.add(newShop);
                        PreferencesManager.getInstance().putShopes(shopes);
                        dialog1.dismiss();
                    }
                })
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .onNeutral((dialog1, which) -> {
                    ArrayList<String> shopes = new ArrayList<>(PreferencesManager.getInstance().getShops());
                    shopes.remove(editingShop);
                    PreferencesManager.getInstance().putShopes(shopes);
                    dialog1.dismiss();
                })
                .build();
        input.setOnEditorActionListener((v, actionId, event) -> {
            String newShop = input.getText().toString();
            if (newShop.isEmpty()) {
                inputLayout.setError(getString(R.string.error_enter_shop_name));
                return false;
            }
            ArrayList<String> shopes = new ArrayList<>(PreferencesManager.getInstance().getShops());
            if (!newShop.equals(editingShop) && shopes.contains(newShop)) {
                inputLayout.setError(getString(R.string.error_shop_already_exists));
            } else if (newShop.equals(editingShop)){
                dialog.dismiss();
            } else {
                shopes.remove(editingShop);
                shopes.add(newShop);
                PreferencesManager.getInstance().putShopes(shopes);
                dialog.dismiss();
            }
            return false;
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
