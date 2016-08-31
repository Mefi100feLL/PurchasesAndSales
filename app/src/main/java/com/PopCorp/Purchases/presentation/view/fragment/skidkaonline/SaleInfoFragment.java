package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SaleInfoPresenter;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.CropActivity;
import com.PopCorp.Purchases.presentation.view.fragment.InputListItemFragment;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleInfoView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleInfoFragment extends MvpAppCompatFragment
        implements Toolbar.OnMenuItemClickListener,
        SaleChildCallback,
        View.OnClickListener,
        SaleInfoView,
        SaleParamsProvider {

    public static final String CURRENT_SALE = "current_sale";

    private static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter(factory = SaleInfoPresenterFactory.class, presenterId = "SaleInfoPresenter")
    SaleInfoPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private CircularProgressView progressView;
    private View progressLayout;
    private SubsamplingScaleImageView image;

    private Toolbar toolBar;


    public static Fragment create(SaleMainCallback parent, int saleId) {
        SaleInfoFragment result = new SaleInfoFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_SALE, saleId);
        result.setArguments(args);
        result.setParent(parent);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        saleId = getArguments().getInt(SaleFragment.CURRENT_SALE);
        super.onCreate(savedInstanceState);
        presenter.setSale(saleId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_skidkaonline_sale_info, container, false);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolBar.inflateMenu(R.menu.skidkaonline_sale);
        toolBar.setOnMenuItemClickListener(this);
        toolBar.setNavigationOnClickListener(this);

        image = (SubsamplingScaleImageView) rootView.findViewById(R.id.image);
        progressView = (CircularProgressView) rootView.findViewById(R.id.progress);
        progressLayout = rootView.findViewById(R.id.progress_layout);
        ImageView comments = (ImageView) rootView.findViewById(R.id.comments);
        ImageView sendToList = (ImageView) rootView.findViewById(R.id.send_to_list);
        ImageView cropImage = (ImageView) rootView.findViewById(R.id.crop);

        comments.setOnClickListener(view -> parent.showComments());
        sendToList.setOnClickListener(view -> presenter.loadShoppingLists());
        cropImage.setOnClickListener(view -> openCropActivity());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(image.getWindowToken(), 0);
    }

    private void openCropActivity() {
        Intent intent = new Intent(getActivity(), CropActivity.class);
        intent.putExtra(CropActivity.CURRENT_SALE, presenter.getSale().getId());
        intent.putExtra(CropActivity.CURRENT_FILE_PATH, ImageLoader.getInstance().getDiskCache().get(presenter.getSale().getImageBig()).getAbsolutePath());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
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
        SimpleDateFormat format = new SimpleDateFormat("d MMMM", new Locale("ru"));
        File image = ImageLoader.getInstance().getDiskCache().get(sale.getImageBig());
        if (image == null) {
            Toast.makeText(getActivity(), R.string.toast_no_founded_sale_image, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String string = getString(R.string.string_for_share_sale_skidkaonline);
        string = string.replace("shop", presenter.getShopNameForUrl(sale.getShopUrl()));
        String periodBegin = format.format(new Date(sale.getPeriodStart()));
        String periodFinish = format.format(new Date(sale.getPeriodEnd()));
        string = string.replace("period", periodBegin.equals(periodFinish) ? periodBegin : "c " + periodBegin + " по " + periodFinish);

        shareIntent.putExtra(Intent.EXTRA_TEXT, string);

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.string_send_sale_with_app)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.notification_no_apps_for_share_sale, Toast.LENGTH_SHORT).show();
        }
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
        ImageLoader.getInstance().loadImage(sale.getImageBig(), null, UIL.getScaleImageOptions(), new ImageLoadingListener() {
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
                File file = ImageLoader.getInstance().getDiskCache().get(sale.getImageBig());
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

        File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImageSmall());
        if (smallFile != null) {
            image.setImage(ImageSource.uri(smallFile.getAbsolutePath()));
            loadBigImage(sale);
        } else {
            ImageLoader.getInstance().loadImage(sale.getImageSmall(), null, UIL.getScaleImageOptions(), new ImageLoadingListener() {
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
                    File smallFile = ImageLoader.getInstance().getDiskCache().get(sale.getImageSmall());
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


    @Override
    public void showEmptyLists() {
        Snackbar.make(image, R.string.empty_no_shopping_lists_short, Snackbar.LENGTH_LONG)
                .setAction(R.string.button_create, view -> {
                    DialogController.showDialogForNewList(getActivity(), presenter);
                })
                .show();
    }

    @Override
    public void showListsSelecting(List<ShoppingList> shoppingLists) {
        ArrayList<String> items = new ArrayList<>();
        for (ShoppingList list : shoppingLists) {
            items.add(list.getName());
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_selecting_lists);
        builder.items(items);
        builder.positiveText(R.string.dialog_button_send);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.autoDismiss(false);
        builder.itemsCallbackMultiChoice(null, (dialog, which, text) -> {
            if (which.length > 0) {
                presenter.listsSelected(which);
                dialog.dismiss();
            } else {
                showToast(R.string.error_select_lists);
            }
            return false;
        });
        builder.onNegative((dialog, which) -> dialog.dismiss());
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void openInputListItemFragment(ListItem item, long[] listsIds) {
        Intent intent = new Intent(getActivity(), InputListItemActivity.class);
        intent.putExtra(InputListItemFragment.CURRENT_LISTS, listsIds);
        intent.putExtra(InputListItemFragment.CURRENT_LISTITEM, item);
        startActivityForResult(intent, REQUEST_CODE_FOR_INPUT_LISTITEM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_INPUT_LISTITEM) {
                ListItem item = data.getParcelableExtra(InputListItemFragment.CURRENT_LISTITEM);
                if (item != null) {
                    presenter.onItemsRerurned(item);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(int error_select_lists) {
        Toast.makeText(getActivity(), error_select_lists, Toast.LENGTH_SHORT).show();
    }
}
