package com.PopCorp.Purchases.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.utils.NavigationBarFactory;
import com.PopCorp.Purchases.presentation.view.fragment.ShoppingListFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialize.MaterializeBuilder;

public class ShoppingListActivity extends MvpAppCompatActivity {

    private static final String CURRENT_LIST = "current_list";
    private static final String CURRENT_DRAWER_ITEM = "current_drawer_item";

    private ShoppingListFragment fragment;


    public static void show(Activity context, long listId) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.CURRENT_LIST, listId);
        context.startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SOME_ACTIVITY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        new MaterializeBuilder()
                .withActivity(this)
                .withStatusBarPadding(true)
                .withStatusBarColorRes(ThemeManager.getInstance().getPrimaryDarkColorRes())
                .build();

        long currentDrawerItem = getIntent().getLongExtra(CURRENT_DRAWER_ITEM, R.string.navigation_drawer_lists);

        Drawer drawer = NavigationBarFactory.createNavigationBar(this, (view, position, drawerItem) -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.SELECTED_DRAWER_ITEM, drawerItem.getIdentifier());
            setResult(Activity.RESULT_OK, result);
            finish();
            return false;
        });
        drawer.setSelection(currentDrawerItem, false);

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