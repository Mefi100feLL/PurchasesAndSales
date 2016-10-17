package com.PopCorp.Purchases.presentation.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Apopsuenko on 17.10.2016.
 */

public class NavigationBarFactory {

    public static Drawer createNavigationBar(Activity context, Drawer.OnDrawerItemClickListener listener){
        Drawer result;
        result = new DrawerBuilder()
                .withActivity(context)
                .withHeader(R.layout.content_header)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.navigation_drawer_mestoskidki).withDivider(false),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_shops).withIdentifier(R.string.navigation_drawer_shops).withIcon(R.drawable.ic_store_mall_directory_grey600_24dp).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_categories).withIdentifier(R.string.navigation_drawer_categories).withIcon(R.drawable.tag_grey600_24dp).withIconTintingEnabled(true),
                        new SectionDrawerItem().withName(R.string.navigation_drawer_skidkaonline).withDivider(true),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_skidkaonline_sales).withIdentifier(R.string.navigation_drawer_skidkaonline_sales).withIcon(R.drawable.sale).withIconTintingEnabled(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_lists).withIdentifier(R.string.navigation_drawer_lists).withIcon(R.drawable.ic_dashboard_grey600_24dp).withIconTintingEnabled(true),
                        //new PrimaryDrawerItem().withName(R.string.navigation_drawer_all_products).withIdentifier(R.string.navigation_drawer_all_products).withIcon(R.drawable.ic_list_grey600_24dp).withIconTintingEnabled(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_settings).withIdentifier(R.string.navigation_drawer_settings).withIcon(R.drawable.ic_settings_grey600_24dp).withIconTintingEnabled(true)
                )
                .withOnDrawerItemClickListener(listener)
                .build();
        ImageLoader.getInstance().displayImage("drawable://" + ThemeManager.getInstance().getHeaderRes(), (ImageView) result.getHeader().findViewById(R.id.content_header_image), UIL.getImageOptions());
        return result;
    }
}
