package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.PreferencesManager;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.controller.DialogController;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.CropPresenter;
import com.PopCorp.Purchases.presentation.view.activity.InputListItemActivity;
import com.PopCorp.Purchases.presentation.view.fragment.InputListItemFragment;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.CropView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yalantis.ucrop.view.GestureCropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CropFragment extends MvpAppCompatFragment implements CropView {

    private static final String CURRENT_SALE = "current_sale";
    private static final String CURRENT_FILE_PATH = "current_file_path";

    private static final int REQUEST_CODE_FOR_INPUT_LISTITEM = 1;

    @InjectPresenter
    CropPresenter presenter;

    private GestureCropImageView cropView;
    private ImageView image;
    private View progressBar;
    private FloatingActionButton fab;
    private Toolbar toolBar;
    private View rotateSkip;
    private View scaleSkip;


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
        cropView = (GestureCropImageView) rootView.findViewById(R.id.crop_view);
        progressBar = rootView.findViewById(R.id.progress);

        cropView.setTargetAspectRatio(1);
        rotateSkip = rootView.findViewById(R.id.rotate_skip);
        scaleSkip = rootView.findViewById(R.id.scale_skip);

        rotateSkip.setOnClickListener(view -> {
            cropView.postRotate(-cropView.getCurrentAngle());
            cropView.setImageToWrapCropBounds();
        });

        scaleSkip.setOnClickListener(view -> {
            cropView.zoomOutImage(cropView.getMinScale());
            cropView.setImageToWrapCropBounds();
        });

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> cropImageAndSaveInFile());

        try {
            cropView.setImageUri(Uri.fromFile(new File(getArguments().getString(CURRENT_FILE_PATH))));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showImage(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
        cropView.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(int errorRes) {
        showToast(errorRes);
    }

    public void showToast(int errorRes) {
        Toast.makeText(getActivity(), errorRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorCanNotCropImage() {
        Toast.makeText(getActivity(), R.string.error_can_not_crop_image, Toast.LENGTH_SHORT).show();
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
        fab.setOnClickListener(view -> {
            presenter.loadShoppingLists();
        });
    }

    @Override
    public void showImage(String imageUri) {
        ImageLoader.getInstance().displayImage(imageUri, image, UIL.getImageOptions());
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
}
