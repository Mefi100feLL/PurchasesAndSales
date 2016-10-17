package com.PopCorp.Purchases.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.utils.NavigationBarFactory;
import com.PopCorp.Purchases.presentation.view.fragment.SalesInCategoryFragment;
import com.PopCorp.Purchases.presentation.view.fragment.SalesInShopFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialize.MaterializeBuilder;

public class SalesActivity extends MvpAppCompatActivity {

    private static final String CURRENT_SHOP = "current_shop";
    private static final String CURRENT_CATEGORY = "current_category";
    private static final String BUNDLE = "bundle";
    private static final String CURRENT_DRAWER_ITEM = "current_drawer_item";


    public static void show(Activity context, Shop shop) {
        Intent intent = new Intent(context, SalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_SHOP, shop);
        intent.putExtra(BUNDLE, bundle);
        context.startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SOME_ACTIVITY);
    }

    public static void show(Activity context, Category category) {
        Intent intent = new Intent(context, SalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_CATEGORY, category);
        intent.putExtra(BUNDLE, bundle);
        context.startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SOME_ACTIVITY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        new MaterializeBuilder()
                .withActivity(this)
                .withStatusBarPadding(true)
                .withStatusBarColorRes(ThemeManager.getInstance().getPrimaryDarkColorRes())
                .build();

        long currentDrawerItem;
        Fragment fragment;
        if (getIntent().getBundleExtra(BUNDLE) != null) {
            Bundle extras = getIntent().getBundleExtra(BUNDLE);
            if (extras.getParcelable(CURRENT_SHOP) != null) {
                currentDrawerItem = R.string.navigation_drawer_shops;
                fragment = SalesInShopFragment.create(extras.getParcelable(CURRENT_SHOP));
            } else if (extras.getParcelable(CURRENT_CATEGORY) != null) {
                currentDrawerItem = R.string.navigation_drawer_categories;
                fragment = SalesInCategoryFragment.create(extras.getParcelable(CURRENT_CATEGORY));
            } else {
                finish();
                return;
            }
            currentDrawerItem = extras.getLong(CURRENT_DRAWER_ITEM, currentDrawerItem);
        } else {
            return;
        }

        Drawer drawer = NavigationBarFactory.createNavigationBar(this, (view, position, drawerItem) -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.SELECTED_DRAWER_ITEM, drawerItem.getIdentifier());
            setResult(Activity.RESULT_OK, result);
            finish();
            return false;
        });
        drawer.setSelection(currentDrawerItem, false);

        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
