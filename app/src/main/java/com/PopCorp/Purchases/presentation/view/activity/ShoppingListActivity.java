package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.view.fragment.ShoppingListFragment;

public class ShoppingListActivity extends MvpAppCompatActivity {

    public static final String CURRENT_LIST = "current_list";

    private ShoppingListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ThemeManager.getInstance().setStatusBarColor(this);

        fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putLong(ShoppingListFragment.CURRENT_LIST, getIntent().getLongExtra(CURRENT_LIST, -1));
        fragment.setArguments(args);
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }

    @Override
    public void onBackPressed(){
        if (fragment != null && fragment.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }
}