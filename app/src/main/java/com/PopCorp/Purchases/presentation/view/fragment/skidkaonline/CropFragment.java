package com.PopCorp.Purchases.presentation.view.fragment.skidkaonline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatFragment;
import com.PopCorp.Purchases.presentation.presenter.skidkaonline.CropPresenter;
import com.PopCorp.Purchases.presentation.view.activity.skidkaonline.CropActivity;
import com.PopCorp.Purchases.presentation.view.moxy.skidkaonline.CropView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.yalantis.ucrop.view.GestureCropImageView;

import java.io.File;

public class CropFragment extends MvpAppCompatFragment implements CropView {

    @InjectPresenter
    CropPresenter presenter;

    private GestureCropImageView cropView;
    private ImageView image;
    private View progressBar;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crop, container, false);
        setHasOptionsMenu(true);

        Toolbar toolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
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

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> cropImageAndSaveInFile());

        progressBar.setVisibility(View.INVISIBLE);
        try {
            cropView.setImageUri(Uri.fromFile(new File(getArguments().getString(CropActivity.CURRENT_FILE_NAME))));
        } catch (Exception e) {
            getActivity().finish();
        }

        return rootView;
    }

    private void cropImageAndSaveInFile() {
        try{
            String cacheDir;
            if (getActivity().getExternalCacheDir().exists() || getActivity().getExternalCacheDir().mkdirs()){
                cacheDir = getActivity().getExternalCacheDir().getAbsolutePath();
            } else if (getActivity().getCacheDir().exists() || getActivity().getCacheDir().mkdirs()){
                cacheDir = getActivity().getCacheDir().getAbsolutePath();
            } else{
                showError(R.string.error_can_not_crop_image);
                return;
            }

            presenter.cropImage(cropView.cropImage(), cacheDir);
        } catch (Exception e) {
            showError(R.string.error_can_not_crop_image);
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
        Toast.makeText(getActivity(), errorRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorCanNotCropImage() {
        Toast.makeText(getActivity(), R.string.error_can_not_crop_image, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showActionsWithCroppedImage() {
        fab.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
    }
}
