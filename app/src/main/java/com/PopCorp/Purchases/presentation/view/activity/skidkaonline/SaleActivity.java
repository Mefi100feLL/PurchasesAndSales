package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.ZoomOutPageTransformer;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.SaleActivityPresenter;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.SaleFragment;
import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialize.MaterializeBuilder;

public class SaleActivity extends MvpAppCompatActivity implements SaleActivityView {

    private static final String CURRENT_SALE = "current_sale";
    private static final String ARRAY_SALES = "array_sales";
    private static final String EDIT_MODE = "edit_mode";

    @InjectPresenter
    SaleActivityPresenter presenter;

    private ViewPager viewPager;

    public static void show(Context context, int currentSaleId, String[] salesIds, boolean editMode){
        Intent intent = new Intent(context, SaleActivity.class);
        intent.putExtra(CURRENT_SALE, String.valueOf(currentSaleId));
        intent.putExtra(ARRAY_SALES, salesIds);
        intent.putExtra(EDIT_MODE, editMode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        new MaterializeBuilder()
                .withActivity(this)
                .withTranslucentStatusBarProgrammatically(true)
                .withTransparentStatusBar(true)
                .withStatusBarColorRes(android.R.color.transparent)
                .build();

        presenter.setCurrentId(Integer.valueOf(getIntent().getStringExtra(CURRENT_SALE)));
        presenter.setSalesIds(getIntent().getStringArrayExtra(ARRAY_SALES));
        presenter.setEditMode(getIntent().getBooleanExtra(EDIT_MODE, false));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            viewPager.addOnPageChangeListener(adapter);
            viewPager.setCurrentItem(presenter.getCurrentSalePosition());
        }
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return presenter.getSalesIds().size();
        }

        @Override
        public Fragment getItem(int position) {
            return SaleFragment.create(presenter.getSalesIds().get(position), viewPager.getCurrentItem() == position, presenter.isEditMode());
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag != null && frag.isVisible() && frag.isMenuVisible()) {
                if (((BackPressedCallback) frag).onBackPressed()){
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}