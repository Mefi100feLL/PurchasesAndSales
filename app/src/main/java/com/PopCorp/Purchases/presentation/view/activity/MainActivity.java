package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.MainPresenter;
import com.PopCorp.Purchases.presentation.view.fragment.CategoriesFragment;
import com.PopCorp.Purchases.presentation.view.fragment.SettingsFragment;
import com.PopCorp.Purchases.presentation.view.fragment.ShoppingListsFragment;
import com.PopCorp.Purchases.presentation.view.fragment.ShopsFragment;
import com.PopCorp.Purchases.presentation.view.moxy.MainView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter presenter;

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void selectFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            String tag = fragment.getClass().getSimpleName();
            if (manager.findFragmentByTag(tag) == null) {
                transaction.replace(R.id.content, fragment, tag).commit();
            }
        }
    }

    @Override
    public void createDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.content_header)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.navigation_drawer_mestoskidki).withDivider(false),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_shops).withIdentifier(R.id.navigation_shops).withIcon(R.drawable.ic_store_mall_directory_grey600_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_categories).withIdentifier(R.id.navigation_categories).withIcon(R.drawable.tag).withIconTintingEnabled(true),
                        new SectionDrawerItem().withName(R.string.navigation_drawer_skidkaonline).withDivider(true),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_skidkaonline_sales).withIdentifier(R.id.navigation_skidkaonline).withIcon(R.drawable.sale).withIconTintingEnabled(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_lists).withIdentifier(R.id.navigation_lists).withIcon(R.drawable.ic_dashboard_grey600_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_all_products).withIdentifier(R.id.navigation_all_products).withIcon(R.drawable.ic_list_grey600_24dp).withIconTintingEnabled(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_settings).withIdentifier(R.id.navigation_settings).withIcon(R.drawable.ic_settings_grey600_24dp).withIconTintingEnabled(true)
                )
                .withOnDrawerItemClickListener((view, position, iDrawerItem) -> {
                    presenter.setSelectedDrawerItem(iDrawerItem.getIdentifier());
                    Fragment fragment = null;
                    switch (iDrawerItem.getIdentifier()){
                        case R.id.navigation_shops:
                            fragment = new ShopsFragment();
                            break;
                        case R.id.navigation_categories:
                            fragment = new CategoriesFragment();
                            break;
                        case R.id.navigation_lists:
                            fragment = new ShoppingListsFragment();
                            break;
                        case R.id.navigation_settings:
                            fragment = new SettingsFragment();
                            break;
                    }
                    selectFragment(fragment);
                    return false;
                })
                .build();
        drawer.setSelection(presenter.getSelectedDrawerItem(), true);
        ImageLoader.getInstance().displayImage("drawable://" + ThemeManager.getInstance().getHeaderRes(), (ImageView) drawer.getHeader().findViewById(R.id.content_header_image), UIL.getImageOptions());
    }
}
