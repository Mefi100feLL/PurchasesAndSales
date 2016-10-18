package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.utils.NavigationBarFactory;
import com.PopCorp.Purchases.presentation.view.activity.MainActivity;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.SalesFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialize.MaterializeBuilder;

public class SalesActivity extends MvpAppCompatActivity {

    private static final String CURRENT_SHOP = "current_shop";
    private static final String CURRENT_DRAWER_ITEM = "current_drawer_item";


    public static void show(Activity context, Shop shop) {
        Intent intent = new Intent(context, SalesActivity.class);
        intent.putExtra(SalesActivity.CURRENT_SHOP, shop);
        context.startActivityForResult(intent, MainActivity.REQUEST_CODE_FOR_SOME_ACTIVITY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        new MaterializeBuilder()
                .withActivity(this)
                .withStatusBarPadding(true)
                .withStatusBarColorRes(ThemeManager.getInstance().getPrimaryDarkColorRes())
                .build();

        long currentDrawerItem = getIntent().getLongExtra(CURRENT_DRAWER_ITEM, R.string.navigation_drawer_skidkaonline_sales);

        Drawer drawer = NavigationBarFactory.createNavigationBar(this, (view, position, drawerItem) -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.SELECTED_DRAWER_ITEM, drawerItem.getIdentifier());
            setResult(Activity.RESULT_OK, result);
            finish();
            return false;
        });
        drawer.setSelection(currentDrawerItem, false);

        Fragment fragment = SalesFragment.create(getIntent().getParcelableExtra(CURRENT_SHOP));
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
