package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.data.utils.ZoomOutPageTransformer;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.presenter.SaleActivityPresenter;
import com.PopCorp.Purchases.presentation.view.fragment.SaleFragment;
import com.PopCorp.Purchases.presentation.view.moxy.SaleActivityView;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class SaleActivity extends MvpAppCompatActivity implements SaleActivityView {

    public static final String CURRENT_SALE = "current_sale";
    public static final String ARRAY_SALES = "array_sales";

    @InjectPresenter
    SaleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getTranslucentThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

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

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

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
        /*FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }*/
        super.onBackPressed();
        /*hideFab(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewPager.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                intent.putExtra(CURRENT_SALE, String.valueOf(sales.get(viewPager.getCurrentItem()).getId()));
                intent.putExtra(ARRAY_SALES, getIntent().getStringArrayListExtra(ARRAY_SALES));
                setResult(RESULT_OK, intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        super.onBackPressed();*/
    }
}
