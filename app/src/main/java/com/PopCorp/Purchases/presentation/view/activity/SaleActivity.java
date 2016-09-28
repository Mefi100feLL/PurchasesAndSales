package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.ZoomOutPageTransformer;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.SaleActivityPresenter;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.fragment.SaleFragment;
import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialize.MaterializeBuilder;

public class SaleActivity extends MvpAppCompatActivity implements SaleActivityView {

    public static final String CURRENT_SALE = "current_sale";
    public static final String ARRAY_SALES = "array_sales";

    public static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter
    SaleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        MaterializeBuilder builder = new MaterializeBuilder()
                .withActivity(this)
                .withTranslucentStatusBarProgrammatically(true)
                .withTransparentStatusBar(true)
                .withTransparentNavigationBar(!WindowUtils.isLandscape(this));

        if (WindowUtils.isLandscape(this)){
            builder.withStatusBarColorRes(R.color.bars_color);
        } else {
            builder.withStatusBarColorRes(android.R.color.transparent);
        }
        builder.build();

        presenter.setCurrentId(Integer.valueOf(getIntent().getStringExtra(CURRENT_SALE)));
        presenter.setSalesIds(getIntent().getStringArrayExtra(ARRAY_SALES));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
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
            return SaleFragment.create(presenter.getSalesIds().get(position));
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
