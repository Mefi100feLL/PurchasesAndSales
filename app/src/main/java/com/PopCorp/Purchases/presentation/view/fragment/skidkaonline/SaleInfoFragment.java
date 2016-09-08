package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.SaleChildCallback;
import com.PopCorp.Purchases.data.callback.SaleMainCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.factory.skidkaonline.SaleInfoPresenterFactory;
import com.PopCorp.Purchases.presentation.presenter.params.provider.SaleParamsProvider;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.SaleInfoPresenter;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.CropActivity;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.SaleInfoView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleInfoFragment extends MvpAppCompatFragment
        implements
        Toolbar.OnMenuItemClickListener,
        SaleChildCallback,
        View.OnClickListener,
        SaleInfoView,
        SaleParamsProvider {

    private static final String CURRENT_SALE = "current_sale";

    private static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter(factory = SaleInfoPresenterFactory.class, presenterId = SaleInfoPresenter.PRESENTER_ID)
    SaleInfoPresenter presenter;

    private int saleId;

    private SaleMainCallback parent;

    private CircularProgressView progressView;
    private View progressLayout;
    private SubsamplingScaleImageView image;
    private CoordinatorLayout snackbarlayout;

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
        saleId = getArguments().getInt(CURRENT_SALE);
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
        snackbarlayout = (CoordinatorLayout) rootView.findViewById(R.id.snackbar_layout);
        ImageView comments = (ImageView) rootView.findViewById(R.id.comments);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        ImageView cropImage = (ImageView) rootView.findViewById(R.id.crop);

        LinearLayout buttonslayout = (LinearLayout) rootView.findViewById(R.id.buttons_layout);
        if (!WindowUtils.isLandscape(getActivity()) || WindowUtils.isTablet(getActivity())) {
            buttonslayout.setPadding(0, 0, 0, WindowUtils.getNavigationBarHeight(getActivity()));
        } else {
            toolBar.setPadding(0, 0, WindowUtils.getNavigationBarHeight(getActivity()), 0);
        }

        image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        image.setMaxScale(getResources().getDimension(R.dimen.image_maximum_scale));

        comments.setOnClickListener(view -> parent.showComments());
        fab.setOnClickListener(view -> presenter.loadShoppingLists());
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

    @Override
    public void showInfo(Sale sale) {
        SimpleDateFormat format = new SimpleDateFormat("d MMMM ", new Locale("ru"));
        String title = format.format(new Date(sale.getPeriodStart())) + " - " + format.format(new Date(sale.getPeriodEnd()));
        toolBar.setTitle(title);
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showSaleEmpty() {

    }

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }


    @Override
    public void showEmptyLists() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_no_shopping_lists);
        builder.content(R.string.dialog_content_no_shopping_lists_create_new);
        builder.positiveText(R.string.dialog_button_create);
        builder.negativeText(R.string.dialog_button_cancel);
        builder.onPositive((dialog, which) -> DialogController.showDialogForNewList(getActivity(), presenter));
        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        intent.putExtra(InputListItemActivity.CURRENT_LISTS, listsIds);
        intent.putExtra(InputListItemActivity.CURRENT_LISTITEM, item);
        startActivityForResult(intent, REQUEST_CODE_FOR_INPUT_LISTITEM);
    }

    @Override
    public void showErrorLoadingLists(Throwable e) {
        showToast(R.string.error_can_not_load_lists);
    }

    @Override
    public void showItemAdded() {
        showToast(R.string.notification_sale_sended_in_lists);
    }

    @Override
    public void showImage(ImageSource uri) {
        image.setImage(uri);
    }

    @Override
    public void setProgress(int progress) {
        progressView.setProgress(progress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_INPUT_LISTITEM) {
                ListItem item = data.getParcelableExtra(InputListItemActivity.CURRENT_LISTITEM);
                if (item != null) {
                    presenter.onItemsRerurned(item);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(int textRes) {
        Toast.makeText(getActivity(), textRes, Toast.LENGTH_SHORT).show();
    }
}
