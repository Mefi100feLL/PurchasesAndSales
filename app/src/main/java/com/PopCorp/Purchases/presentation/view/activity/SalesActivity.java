package com.PopCorp.Purchases.presentation.view.activity;

import android.content.Context;
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
import com.PopCorp.Purchases.presentation.view.fragment.SalesInCategoryFragment;
import com.PopCorp.Purchases.presentation.view.fragment.SalesInShopFragment;

public class SalesActivity extends MvpAppCompatActivity {

    private static final String CURRENT_SHOP = "current_shop";
    private static final String CURRENT_CATEGORY = "current_category";
    private static final String BUNDLE = "bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        ThemeManager.getInstance().setStatusBarColor(this);

        Fragment fragment;
        if (getIntent().getBundleExtra(BUNDLE) != null) {
            Bundle extras = getIntent().getBundleExtra(BUNDLE);
            if (extras.getParcelable(CURRENT_SHOP) != null) {
                fragment = SalesInShopFragment.create(extras.getParcelable(CURRENT_SHOP));
            } else if (extras.getParcelable(CURRENT_CATEGORY) != null) {
                fragment = SalesInCategoryFragment.create(extras.getParcelable(CURRENT_CATEGORY));
            } else {
                finish();
                return;
            }
        } else {
            return;
        }
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }

    public static void show(Context context, Shop shop) {
        Intent intent = new Intent(context, SalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_SHOP, shop);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }

    public static void show(Context context, Category category) {
        Intent intent = new Intent(context, SalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CURRENT_CATEGORY, category);
        intent.putExtra(BUNDLE, bundle);
        context.startActivity(intent);
    }
}
