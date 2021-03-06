package com.PopCorp.Purchases.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.InputListItemPresenter;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.activity.ListItemSaleActivity;
import com.PopCorp.Purchases.presentation.view.adapter.ListItemCategoriesAdapter;
import com.PopCorp.Purchases.presentation.view.moxy.InputListItemView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InputListItemFragment extends MvpAppCompatFragment implements InputListItemView {

    public static final String CURRENT_LISTS = "current_list";
    public static final String CURRENT_LISTITEM = "current_listitem";

    @InjectPresenter
    InputListItemPresenter presenter;

    private Toolbar toolBar;
    private EditText count;
    private AutoCompleteTextView name;
    private TextInputLayout nameLayout;
    private Spinner unitSpinner;
    private EditText coast;
    private TextInputLayout coastLayout;
    private CheckBox important;
    private Spinner shopSpinner;
    private Spinner categorySpinner;
    private EditText comment;

    private ArrayList<String> units;
    private ArrayList<String> shops;

    private ArrayAdapter<String> adapterUnits;
    private ArrayAdapter<String> productsAdapter;
    private ArrayAdapter<String> adapterShop;

    private View saleLayout;
    private ImageView saleImage;
    private TextView salePeriod;

    public static InputListItemFragment create(ListItem item, long[] listIds){
        InputListItemFragment result = new InputListItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(InputListItemFragment.CURRENT_LISTITEM, item);
        args.putLongArray(InputListItemFragment.CURRENT_LISTS, listIds);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setItem(getArguments().getParcelable(CURRENT_LISTITEM));
        presenter.setListsIds(getArguments().getLongArray(CURRENT_LISTS));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listitem_input, container, false);
        setHasOptionsMenu(true);
        
        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolBar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        ThemeManager.getInstance().putPrimaryColor(appBarLayout);
        ThemeManager.getInstance().putPrimaryColor(toolBar);

        ImageView minus = (ImageView) rootView.findViewById(R.id.listitem_count_minus);
        ImageView plus = (ImageView) rootView.findViewById(R.id.listitem_count_plus);
        count = (EditText) rootView.findViewById(R.id.listitem_count);
        name = (AutoCompleteTextView) rootView.findViewById(R.id.listitem_name);
        nameLayout = (TextInputLayout) rootView.findViewById(R.id.listitem_name_layout);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        unitSpinner = (Spinner) rootView.findViewById(R.id.listitem_unit);
        coast = (EditText) rootView.findViewById(R.id.listitem_coast);
        coastLayout = (TextInputLayout) rootView.findViewById(R.id.listitem_coast_layout);
        important = (CheckBox) rootView.findViewById(R.id.listitem_important);
        shopSpinner = (Spinner) rootView.findViewById(R.id.listitem_shop);
        categorySpinner = (Spinner) rootView.findViewById(R.id.listitem_category);
        comment = (EditText) rootView.findViewById(R.id.listitem_comment);
        saleLayout = rootView.findViewById(R.id.sale_layout);
        saleImage = (ImageView) rootView.findViewById(R.id.sale_image);
        salePeriod = (TextView) rootView.findViewById(R.id.sale_period);

        minus.setOnClickListener(v -> {
            if (count.getText().toString().isEmpty()){
                count.setText("1");
            }
            BigDecimal value = new BigDecimal(count.getText().toString());
            if (value.doubleValue() >= 1) {
                value = value.subtract(new BigDecimal("1"));
                count.setText(value.toString());
            }
        });
        plus.setOnClickListener(v -> {
            if (count.getText().toString().isEmpty()){
                count.setText("1");
            }
            BigDecimal value = new BigDecimal(count.getText().toString());
            value = value.add(new BigDecimal("1"));
            count.setText(value.toString());
        });
        count.setText("1");
        initUnitSpinner();
        initShopsSpinner();
        fab.setOnClickListener(v -> onFabClicked());
        setFields();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    private void onFabClicked() {
        String shop = (String) shopSpinner.getSelectedItem();
        if (shopSpinner.getSelectedItemPosition() == 0){
            shop = "";
        }
        presenter.buildItem(name.getText().toString(),
                count.getText().toString(),
                (String) unitSpinner.getSelectedItem(),
                coast.getText().toString(),
                important.isChecked(),
                shop,
                categorySpinner.getSelectedItemPosition(),
                comment.getText().toString()
        );
    }

    @Override
    public void showCategoriesSpinner(List<ListItemCategory> categories) {
        ListItemCategoriesAdapter adapterCategories = new ListItemCategoriesAdapter(getActivity(), categories);
        adapterCategories.setDropDownViewResource(R.layout.item_listitem_category);
        categorySpinner.setAdapter(adapterCategories);
        if (presenter.getItem() == null) {
            categorySpinner.setSelection(adapterCategories.getCount() - 1);
        } else {
            categorySpinner.setSelection(categories.indexOf(presenter.getItem().getCategory()));
        }
    }

    @Override
    public void showNameEmpty() {
        nameLayout.setErrorEnabled(true);
        nameLayout.setError(getString(R.string.error_listitem_name_empty));
        name.requestFocus();
    }

    @Override
    public void showListItemWithNameExists() {
        nameLayout.setErrorEnabled(true);
        nameLayout.setError(getString(R.string.error_listitem_with_name_exists));
    }

    @Override
    public void hideNameError() {
        nameLayout.setError(null);
        nameLayout.setErrorEnabled(false);
    }

    @Override
    public void returnItemAndClose(ListItem item) {
        Intent result = new Intent();
        result.putExtra(InputListItemActivity.CURRENT_LISTITEM, item);
        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
    }

    @Override
    public void setNameAdapter(List<String> products) {
        productsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, products);
        name.setAdapter(productsAdapter);
        name.setOnItemClickListener((parent, view, position, id) -> presenter.onProductSelected(productsAdapter.getItem(position)));
    }

    private void initShopsSpinner() {
        shops = new ArrayList<>(PreferencesManager.getInstance().getShops());
        shops.add(0, getString(R.string.string_no_shop));

        adapterShop = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, shops);
        adapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopSpinner.setAdapter(adapterShop);
        shopSpinner.setSelection(shops.size() - 1);
    }

    private void initUnitSpinner() {
        units = new ArrayList<>(PreferencesManager.getInstance().getEdizms());

        adapterUnits = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, units);
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapterUnits);
        unitSpinner.setSelection(getPositionForEdizm(PreferencesManager.getInstance().getDefaultEdizm()));

        unitSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coastLayout.setHint(getString(R.string.hint_listitem_coast).replace("edizm", (String) unitSpinner.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private int getPositionForEdizm(String edizm) {
        if (edizm == null) {
            return adapterUnits.getCount() - 1;
        }
        if (!units.contains(edizm)) {
            if (edizm.equals("")) {
                return adapterUnits.getCount() - 1;
            }
            units.add(0, edizm);
            addNewEdizmToPrefs(edizm);
        }
        return adapterUnits.getPosition(edizm);
    }

    private int getPositionForShop(String shop){
        if (shop == null) {
            return 0;
        }
        if (!shops.contains(shop)) {
            if (shop.equals("")) {
                return 0;
            }
            shops.add(shop);
            addNewShopToPrefs(shop);
        }
        return adapterShop.getPosition(shop);
    }

    private void addNewShopToPrefs(String shop) {
        ArrayList<String> list = new ArrayList<>(PreferencesManager.getInstance().getShops());
        list.add(shop);
        PreferencesManager.getInstance().putShopes(list);
    }

    private void addNewEdizmToPrefs(final String newEdizm) {
        List<String> list = new ArrayList<>(PreferencesManager.getInstance().getEdizms());
        list.add(newEdizm);
        PreferencesManager.getInstance().putEdizms(list);
    }

    private void setFields() {
        if (presenter.getItem() != null){
            ListItem item = presenter.getItem();
            name.setText(item.getName());
            count.setText(item.getCountString());
            unitSpinner.setSelection(getPositionForEdizm(item.getEdizm()));
            coast.setText(item.getCoastString());
            important.setChecked(item.isImportant());
            shopSpinner.setSelection(getPositionForShop(item.getShop()));
            if (presenter.getCategories().contains(item.getCategory())) {
                categorySpinner.setSelection(presenter.getCategories().indexOf(item.getCategory()));
            }
            comment.setText(item.getComment());
            if (item.getSale() != null){
                saleLayout.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.getSale().getImage(), saleImage, UIL.getImageOptions());
                salePeriod.setText(item.getSale().getPeriodStart() + " - " + item.getSale().getPeriodEnd());
                saleImage.setOnClickListener(v -> openListItemSale(v, item.getSale()));
            } else {
                saleLayout.setVisibility(View.GONE);
            }
        }
    }

    private void openListItemSale(View v, ListItemSale sale) {
        Intent intent = new Intent(getActivity(), ListItemSaleActivity.class);
        intent.putExtra(ListItemSaleActivity.CURRENT_LISTITEM_SALE, sale);
        startActivity(intent);
    }

    @Override
    public void setFields(Product product) {
        name.setText(product.getName());
        count.setText(product.getCountString());
        unitSpinner.setSelection(getPositionForEdizm(product.getEdizm()));
        coast.setText(product.getCoastString());
        shopSpinner.setSelection(getPositionForShop(product.getShop()));
        if (presenter.getCategories().contains(product.getCategory())) {
            categorySpinner.setSelection(presenter.getCategories().indexOf(product.getCategory()));
        }
        comment.setText(product.getComment());
    }
}