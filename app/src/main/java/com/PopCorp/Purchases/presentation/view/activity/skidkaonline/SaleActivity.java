package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ZoomOutPageTransformer;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.SaleActivityPresenter;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.SaleFragment;
import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialize.MaterializeBuilder;

public class SaleActivity extends MvpAppCompatActivity implements SaleActivityView {

    public static final String CURRENT_SALE = "current_sale";
    public static final String ARRAY_SALES = "array_sales";

    @InjectPresenter
    SaleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(ThemeManager.getInstance().getTranslucentThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        new MaterializeBuilder()
                .withActivity(this)
                .withTranslucentStatusBarProgrammatically(true)
                .withTransparentStatusBar(true)
                .withStatusBarColorRes(android.R.color.transparent)
                .build();

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
            SaleFragment fragment = new SaleFragment();
            Bundle args = new Bundle();
            args.putInt(SaleFragment.CURRENT_SALE, presenter.getSalesIds().get(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            /*if (Build.VERSION.SDK_INT >= 21) {
                image.setTransitionName(String.valueOf(presenter.getSalesIds().get(position)));
                ImageLoader.getInstance().displayImage(sales.get(position).getImageUrl(), image, UIL.getScaleImageOptions());
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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