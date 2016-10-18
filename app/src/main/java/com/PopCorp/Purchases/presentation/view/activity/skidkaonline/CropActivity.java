package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.BackPressedCallback;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.CropFragment;
import com.mikepenz.materialize.MaterializeBuilder;

public class CropActivity extends AppCompatActivity {

    private static final String CURRENT_SALE = "current_sale";
    private static final String CURRENT_FILE_PATH = "current_file_path";


    public static void show(Context context, int id, String absolutePath){
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(CURRENT_SALE, id);
        intent.putExtra(CURRENT_FILE_PATH, absolutePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        new MaterializeBuilder()
                .withActivity(this)
                .withTranslucentStatusBarProgrammatically(true)
                .withTransparentStatusBar(true)
                .withStatusBarColorRes(android.R.color.transparent)
                .build();

        Fragment fragment = CropFragment.create(getIntent().getIntExtra(CURRENT_SALE, -1), getIntent().getStringExtra(CURRENT_FILE_PATH));
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
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
