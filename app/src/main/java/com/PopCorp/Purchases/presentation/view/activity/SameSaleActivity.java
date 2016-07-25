package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.SaleActivityPresenter;
import com.PopCorp.Purchases.presentation.presenter.SameSaleActivityPresenter;
import com.PopCorp.Purchases.presentation.view.fragment.SaleFragment;
import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.PopCorp.Purchases.presentation.view.moxy.SameSaleActivityView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SameSaleActivity extends MvpAppCompatActivity implements SameSaleActivityView {

    public static final String CURRENT_SALE = "current_sale";

    @InjectPresenter
    SameSaleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getTranslucentThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_sale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            /*window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            //window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        Fragment fragment = new SaleFragment();
        Bundle args = new Bundle();
        args.putInt(SaleFragment.CURRENT_SALE, Integer.valueOf(getIntent().getStringExtra(CURRENT_SALE)));
        fragment.setArguments(args);
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
}
