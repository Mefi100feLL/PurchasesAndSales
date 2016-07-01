package com.PopCorp.Purchases.data.utils;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;

public class EmptyView {

    private TextView emptyText;
    private ImageView emptyImage;
    private Button emptyButton;
    private View emptyLayout;
    private View progressBar;
    private View recyclerView;

    public EmptyView(View rootView) {
        emptyText = (TextView) rootView.findViewById(R.id.empty_text);
        emptyImage = (ImageView) rootView.findViewById(R.id.empty_image);
        emptyButton = (Button) rootView.findViewById(R.id.empty_button);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = rootView.findViewById(R.id.recycler);
    }

    public void hide() {
        emptyLayout.setVisibility(View.GONE);
    }

    public void show() {
        emptyLayout.setVisibility(View.VISIBLE);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void showEmpty(String text, int drawableRes) {
        showEmpty(text, drawableRes, 0, null);
    }

    public void showEmpty(int textRes, int drawableRes) {
        showEmpty(textRes, drawableRes, 0, null);
    }

    public void showEmpty(String text, int drawableRes, int textButtonRes, View.OnClickListener listener) {
        emptyText.setText(text);
        showImageAndButton(drawableRes, textButtonRes, listener);
    }

    public void showEmpty(int textRes, int drawableRes, int textButtonRes, View.OnClickListener listener) {
        emptyText.setText(textRes);
        showImageAndButton(drawableRes, textButtonRes, listener);
    }

    private void showImageAndButton(int drawableRes, int textButtonRes, View.OnClickListener listener) {
        emptyImage.setImageResource(drawableRes);
        if (listener == null) {
            emptyButton.setVisibility(View.GONE);
        } else {
            emptyButton.setVisibility(View.VISIBLE);
            emptyButton.setText(textButtonRes);
            emptyButton.setOnClickListener(listener);
        }
        show();
    }
}