package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.CropPresenter;
import com.PopCorp.Purchases.presentation.utils.TapTargetManager;
import com.PopCorp.Purchases.presentation.utils.WindowUtils;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.CropView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CropFragment extends MvpAppCompatFragment implements CropView, BackPressedCallback {

    private static final String CURRENT_SALE = "current_sale";
    private static final String CURRENT_FILE_PATH = "current_file_path";

    private static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter
    CropPresenter presenter;

    private GestureCropImageView cropView;
    private ImageView image;
    private View progress;
    private FloatingActionButton fab;
    private Toolbar toolBar;
    private View rotateSkip;
    private View scaleSkip;

    private Menu menu;


    public static Fragment create(int saleId, String filePath) {
        CropFragment result = new CropFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_SALE, saleId);
        args.putString(CURRENT_FILE_PATH, filePath);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setSaleId(getArguments().getInt(CURRENT_SALE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crop, container, false);
        setHasOptionsMenu(true);

        toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolBar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        image = (ImageView) rootView.findViewById(R.id.cropped_image);
        UCropView uCropView = (UCropView) rootView.findViewById(R.id.ucrop);
        progress = rootView.findViewById(R.id.progress_layout);
        rotateSkip = rootView.findViewById(R.id.rotate_skip);
        scaleSkip = rootView.findViewById(R.id.scale_skip);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        cropView = uCropView.getCropImageView();

        LinearLayout buttonslayout = (LinearLayout) rootView.findViewById(R.id.buttons_layout);
        if (!WindowUtils.isLandscape(getActivity())) {
            buttonslayout.setPadding(0, 0, 0, WindowUtils.getNavigationBarHeight(getActivity()));
        } else {
            buttonslayout.setPadding(0, 0, WindowUtils.getNavigationBarHeight(getActivity()), 0);
            if (WindowUtils.isTablet(getActivity())){
                uCropView.getOverlayView().setPadding(0, WindowUtils.getNavigationBarHeight(getActivity())/2, 0, WindowUtils.getNavigationBarHeight(getActivity()));
            } else {
                toolBar.setPadding(0, 0, WindowUtils.getNavigationBarHeight(getActivity()), 0);
            }
        }


        cropView.setTargetAspectRatio(1);
        rotateSkip.setOnClickListener(view -> {
            cropView.postRotate(-cropView.getCurrentAngle());
            cropView.setImageToWrapCropBounds();
        });

        scaleSkip.setOnClickListener(view -> {
            cropView.zoomOutImage(cropView.getMinScale());
            cropView.setImageToWrapCropBounds();
        });

        fab.setOnClickListener(v -> cropImageAndSaveInFile());

        try {
            cropView.setImageUri(Uri.fromFile(new File(getArguments().getString(CURRENT_FILE_PATH))));
            cropView.setTransformImageListener(presenter);
        } catch (Exception e) {
            e.printStackTrace();
            showToast(R.string.error_when_openning_image_file);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolBar.setKeepScreenOn(PreferencesManager.getInstance().isDisplayNoOff());
    }

    private void cropImageAndSaveInFile() {
        try {
            presenter.saveCroppedImage(cropView.cropImage());
        } catch (Exception e) {
            showToast(R.string.error_when_cropping_image);
            e.printStackTrace();
        }
    }

    @Override
    public void showImage(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
        cropView.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.INVISIBLE);
    }

    public void showToast(int errorRes) {
        Toast.makeText(getActivity(), errorRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorCanNotCropImage() {
        Toast.makeText(getActivity(), R.string.error_can_not_crop_image, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showItemAdded() {
        showToast(R.string.notification_sale_sended_in_lists);
    }

    @Override
    public void showErrorLoadingLists(Throwable e) {
        showToast(R.string.error_can_not_load_lists);
    }

    private TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        @Override
        public void onTargetClick(TapTargetView view) {
            super.onTargetClick(view);
            presenter.showTapTarget();
        }
    };

    @Override
    public void showTapTargetForRotateSkip() {
        View view = rotateSkip;
        if (view != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_rotate_skip, R.string.tap_target_content_rotate_skip))
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void showTapTargetForCrop() {
        View view = fab;
        if (view != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_crop, R.string.tap_target_content_crop)
                                    .tintTarget(false))
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void showTapTargetForScaleSkip() {
        View view = scaleSkip;
        if (view != null) {
            new TapTargetManager(getActivity())
                    .tapTarget(
                            TapTargetManager.forView(getActivity(), view, R.string.tap_target_title_scale_skip, R.string.tap_target_content_sacle_skip))
                    .listener(tapTargetListener)
                    .show();
        }
    }

    @Override
    public void hideSkips() {
        rotateSkip.setVisibility(View.GONE);
        scaleSkip.setVisibility(View.GONE);
    }

    @Override
    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    @Override
    public void showSkips() {
        rotateSkip.setVisibility(View.VISIBLE);
        scaleSkip.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSendingButton() {
        fab.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(view -> presenter.loadShoppingLists());
        fab.post(() -> menu.findItem(R.id.action_share).setVisible(true));
    }

    @Override
    public void showImage(String imageUri) {
        ImageLoader.getInstance().displayImage(imageUri, image, UIL.getImageOptions());
        cropView.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(Throwable e) {

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

    @Override
    public boolean onBackPressed() {
        presenter.clearImage();
        return false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.crop_sale, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.findItem(R.id.action_share).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
