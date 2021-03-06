package com.PopCorp.Purchases.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.fragment.SaleFragment;
import com.mikepenz.materialize.MaterializeBuilder;

public class SameSaleActivity extends MvpAppCompatActivity {

    private static final String CURRENT_SALE = "current_sale";
    private static final String EDIT_MODE = "edit_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_sale);

        MaterializeBuilder builder = new MaterializeBuilder()
                .withActivity(this)
                .withTranslucentStatusBarProgrammatically(true)
                .withTransparentStatusBar(true)
                .withStatusBarPadding(false)
                .withTransparentNavigationBar(!WindowUtils.isLandscape(this));

        if (WindowUtils.isLandscape(this)){
            builder.withStatusBarColorRes(R.color.bars_color);
        } else {
            builder.withStatusBarColorRes(android.R.color.transparent);
        }
        builder.build();

        Fragment fragment = SaleFragment.create(Integer.valueOf(getIntent().getStringExtra(CURRENT_SALE)), true, getIntent().getBooleanExtra(EDIT_MODE, false));
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag != null && frag.isVisible()) {
                if (((BackPressedCallback) frag).onBackPressed()){
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public static void show(Context context, long saleId, boolean editMode) {
        Intent intent = new Intent(context, SameSaleActivity.class);
        intent.putExtra(CURRENT_SALE, String.valueOf(saleId));
        intent.putExtra(EDIT_MODE, editMode);
        context.startActivity(intent);
    }
}
