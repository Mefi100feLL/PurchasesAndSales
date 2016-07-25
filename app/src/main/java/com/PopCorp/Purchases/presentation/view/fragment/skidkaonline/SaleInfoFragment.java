package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SaleInfoPresenter;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.CropActivity;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleInfoView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class SaleInfoFragment extends MvpAppCompatFragment
        implements Toolbar.OnMenuItemClickListener,
        SaleChildCallback,
        View.OnClickListener,
        SaleInfoView,
        SaleParamsProvider {

    @InjectPresenter(factory = SaleInfoPresenterFactory.class, presenterId = "SaleInfoPresenter")
    SaleInfoPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private CircularProgressView progressView;
    private View progressLayout;
    private SubsamplingScaleImageView image;
    private ImageView comments;
    private ImageView sendToList;
    private ImageView cropImage;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(SaleFragment.CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_skidkaonline_sale_info, container, false);

        Toolbar toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolBar.inflateMenu(R.menu.skidkaonline_sale);
        toolBar.setOnMenuItemClickListener(this);
        toolBar.setNavigationOnClickListener(this);

        image = (SubsamplingScaleImageView) rootView.findViewById(R.id.image);
        progressView = (CircularProgressView) rootView.findViewById(R.id.progress);
        progressLayout = rootView.findViewById(R.id.progress_layout);
        comments = (ImageView) rootView.findViewById(R.id.comments);
        sendToList = (ImageView) rootView.findViewById(R.id.send_to_list);
        cropImage = (ImageView) rootView.findViewById(R.id.crop);

        comments.setOnClickListener(view -> parent.showComments());
        sendToList.setOnClickListener(view -> {});
        cropImage.setOnClickListener(view -> openCropActivity());

        return rootView;
    }

    private void openCropActivity() {
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.putExtra(CropActivity.CURRENT_FILE_NAME, ImageLoader.getInstance().getDiskCache().get(presenter.getSale().getImageBig()).getAbsolutePath());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                shareSale(presenter.getSale());
                break;
            case R.id.action_comments:
                parent.showComments();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return false;
    }

    private void shareSale(Sale sale) {
        /*SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));
        SimpleDateFormat format = new SimpleDateFormat("d MMMM", new Locale("ru"));
        File image = ImageLoader.getInstance().getDiskCache().get(sale.getImageBig());
        if (image == null) {
            Toast.makeText(getActivity(), R.string.toast_no_founded_sale_image, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String string = getString(R.string.string_for_share_sale_skidkaonline);
        string = string.replace("shop", sale.getsh);
        String periodBegin;
        try {
            periodBegin = format.format(parser.parse(sale.getPeriodStart()));
        } catch (ParseException e) {
            e.printStackTrace();
            periodBegin = sale.getPeriodStart();
        }
        String periodFinish;
        try {
            periodFinish = format.format(parser.parse(sale.getPeriodEnd()));
        } catch (ParseException e) {
            e.printStackTrace();
            periodFinish = sale.getPeriodEnd();
        }
        string = string.replace("period", periodBegin.equals(periodFinish) ? periodBegin : "c " + periodBegin + " по " + periodFinish);
        string = string.replace("coast", sale.getCoast());

        shareIntent.putExtra(Intent.EXTRA_TEXT, string);

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.string_send_sale_with_app)));
        } catch (Exception e){
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_sale, Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void setParent(SaleMainCallback parent) {
        this.parent = parent;
    }

    @Override
    public String getSaleId(String presenterId) {
        return String.valueOf(saleId);
    }

    private void loadBigImage(Sale sale) {
        imageLoader.loadImage(sale.getImageBig(), null, options, new ImageLoadingListener() {
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
                File file = imageLoader.getDiskCache().get(sale.getImageBig());
                if (file != null) {
                    image.setImage(ImageSource.uri(file.getAbsolutePath()));
                }
                progressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, (s, view, progress, size) -> progressView.setProgress(500 + progress * 500 / size));
    }

    @Override
    public void showInfo(Sale sale) {
        image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        image.setMaxScale(getResources().getDimension(R.dimen.image_maximum_scale));

        File smallFile = imageLoader.getDiskCache().get(sale.getImageSmall());
        if (smallFile != null) {
            image.setImage(ImageSource.uri(smallFile.getAbsolutePath()));
            loadBigImage(sale);
        } else {
            imageLoader.loadImage(sale.getImageSmall(), null, options, new ImageLoadingListener() {
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
                    File smallFile = imageLoader.getDiskCache().get(sale.getImageSmall());
                    if (smallFile != null) {
                        image.setImage(ImageSource.uri(smallFile.getAbsolutePath()));
                    }
                    bitmap.recycle();
                    loadBigImage(sale);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            }, (s, view, progress, size) -> progressView.setProgress(progress * 500 / size));
        }
    }
}
