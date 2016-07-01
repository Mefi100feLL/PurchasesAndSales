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
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Fragment fragment;
        Bundle args = new Bundle();
        if (getIntent().getParcelableExtra(CURRENT_SHOP) != null){
            fragment = new SalesInShopFragment();
            args.putParcelable(CURRENT_SHOP, getIntent().getParcelableExtra(CURRENT_SHOP));
        } else {
            fragment = new SalesInCategoryFragment();
            args.putParcelable(CURRENT_CATEGORY, getIntent().getParcelableExtra(CURRENT_CATEGORY));
        }
        fragment.setArguments(args);
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
