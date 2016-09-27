package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.SalesFragment;
import com.mikepenz.materialize.MaterializeBuilder;

public class SalesActivity extends MvpAppCompatActivity {

    public static final String CURRENT_SHOP = "current_shop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ThemeManager.getInstance().setStatusBarColor(this);

        new MaterializeBuilder()
                .withActivity(this)
                .withStatusBarPadding(!WindowUtils.isLandscape(this))
                .withTransparentNavigationBar(!WindowUtils.isLandscape(this))
                .build();

        Fragment fragment = SalesFragment.create(getIntent().getParcelableExtra(CURRENT_SHOP));
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
