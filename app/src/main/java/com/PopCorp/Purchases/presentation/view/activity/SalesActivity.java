package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.view.fragment.SalesInCategoryFragment;
import com.PopCorp.Purchases.presentation.view.fragment.SalesInShopFragment;

public class SalesActivity extends MvpAppCompatActivity {

    public static final String CURRENT_SHOP = "current_shop";
    public static final String CURRENT_CATEGORY = "current_category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        ThemeManager.getInstance().setStatusBarColor(this);

        Fragment fragment;
        if (getIntent().getParcelableExtra(CURRENT_SHOP) != null) {
            fragment = SalesInShopFragment.create(getIntent().getParcelableExtra(CURRENT_SHOP));
        } else if (getIntent().getParcelableExtra(CURRENT_CATEGORY) != null) {
            fragment = SalesInCategoryFragment.create(getIntent().getParcelableExtra(CURRENT_CATEGORY));
        } else {
            finish();
            return;
        }
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
