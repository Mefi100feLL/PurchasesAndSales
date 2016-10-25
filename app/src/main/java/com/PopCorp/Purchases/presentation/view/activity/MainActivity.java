package com.PopCorp.Purchases.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.MainPresenter;
import com.PopCorp.Purchases.presentation.utils.NavigationBarFactory;
import com.PopCorp.Purchases.presentation.view.fragment.CategoriesFragment;
import com.PopCorp.Purchases.presentation.view.fragment.SettingsFragment;
import com.PopCorp.Purchases.presentation.view.fragment.ShoppingListsFragment;
import com.PopCorp.Purchases.presentation.view.fragment.ShopsFragment;
import com.PopCorp.Purchases.presentation.view.moxy.MainView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialize.MaterializeBuilder;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    public static final int REQUEST_CODE_FOR_SELECTING_CITY_ACTIVITY = 1;
    public static final int REQUEST_CODE_FOR_SELECTING_PRODUCTS = 2;

    public static final int REQUEST_CODE_FOR_SOME_ACTIVITY = 3;

    public static final String SELECTED_DRAWER_ITEM = "selected_drawer_item";

    @InjectPresenter
    MainPresenter presenter;

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MaterializeBuilder()
                .withActivity(this)
                .withStatusBarPadding(true)
                .withStatusBarColorRes(ThemeManager.getInstance().getPrimaryDarkColorRes())
                .build();

        int selectedDrawerItem;
        TypedArray ar = getResources().obtainTypedArray(R.array.navigation_drawers);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        selectedDrawerItem = resIds[PreferencesManager.getInstance().getDefaultDrawerItemPosition()];

        presenter.setSelectedDrawerItem(getIntent().getIntExtra(SELECTED_DRAWER_ITEM, selectedDrawerItem));
        getIntent().putExtra(SELECTED_DRAWER_ITEM, -1);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (drawer.getCurrentSelection() != presenter.getSelectedDrawerItem()) {
            drawer.setSelection(presenter.getSelectedDrawerItem(), true);
        }
    }

    @Override
    public void onBackPressed() {
        if (!drawer.isDrawerOpen()) {
            drawer.openDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            String tag = fragment.getClass().getName();
            if (manager.findFragmentByTag(tag) == null) {
                transaction.replace(R.id.content, fragment, tag).commit();
            }
        }
    }

    @Override
    public void createDrawer() {
        drawer = NavigationBarFactory.createNavigationBar(this, (view, position, drawerItem) -> {
            presenter.setSelectedDrawerItem(drawerItem.getIdentifier());
            Fragment fragment = null;
            switch ((int) drawerItem.getIdentifier()) {
                case R.string.navigation_drawer_shops:
                    fragment = new ShopsFragment();
                    break;
                case R.string.navigation_drawer_categories:
                    fragment = new CategoriesFragment();
                    break;
                case R.string.navigation_drawer_skidkaonline_sales:
                    fragment = new com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.ShopsFragment();
                    break;
                case R.string.navigation_drawer_lists:
                    fragment = new ShoppingListsFragment();
                    break;
                case R.string.navigation_drawer_settings:
                    fragment = new SettingsFragment();
                    break;
            }
            selectFragment(fragment);
            return false;
        });
        drawer.setSelection(presenter.getSelectedDrawerItem(), true);
    }

    public static void show(Context context, int selectedDrawerItem) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(SELECTED_DRAWER_ITEM, selectedDrawerItem);
        context.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_SOME_ACTIVITY) {
                if (data != null) {
                    presenter.setSelectedDrawerItem(data.getLongExtra(SELECTED_DRAWER_ITEM, presenter.getSelectedDrawerItem()));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
