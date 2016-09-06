package com.PopCorp.Purchases.presentation.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.ListItemSalePresenter;
import com.PopCorp.Purchases.presentation.view.moxy.ListItemSaleView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class ListItemSaleFragment extends MvpAppCompatFragment implements View.OnClickListener, ListItemSaleView {

    private static final String CURRENT_LISTITEM_SALE = "current_listitem_sale";

    @InjectPresenter
    ListItemSalePresenter presenter;

    private CircularProgressView progressView;
    private View progressLayout;
    private SubsamplingScaleImageView image;
    private Toolbar toolBar;
    private TextView period;


    public static ListItemSaleFragment create(ListItemSale sale) {
        ListItemSaleFragment result = new ListItemSaleFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_LISTITEM_SALE, sale);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setSale(getArguments().getParcelable(CURRENT_LISTITEM_SALE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listitem_sale, container, false);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolBar.setNavigationOnClickListener(this);

        image = (SubsamplingScaleImageView) rootView.findViewById(R.id.image);
        progressView = (CircularProgressView) rootView.findViewById(R.id.progress);
        progressLayout = rootView.findViewById(R.id.progress_layout);
        period = (TextView) rootView.findViewById(R.id.sale_period);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    @Override
    public void showInfo(ListItemSale sale) {
        period.setText(sale.getPeriodStart() + " - " + sale.getPeriodEnd());
        image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        image.setMaxScale(getResources().getDimension(R.dimen.image_maximum_scale));

        File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImage());
        if (smallFile != null) {
            image.setImage(ImageSource.uri(smallFile.getAbsolutePath()));
        } else {
            ImageLoader.getInstance().loadImage(sale.getImage(), null, UIL.getScaleImageOptions(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    progressLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    progressLayout.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    progressLayout.setVisibility(View.GONE);
                    File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImage());
                    if (smallFile != null) {
                        image.setImage(ImageSource.uri(smallFile.getAbsolutePath()));
                    }
                    bitmap.recycle();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    progressLayout.setVisibility(View.GONE);
                }
            }, (s, view, progress, size) -> progressView.setProgress(progress * 500 / size));
        }
    }
}
