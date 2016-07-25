package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.CropFragment;

public class CropActivity extends AppCompatActivity {

    public static final String CURRENT_FILE_NAME = "current_file_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        CropFragment fragment = new CropFragment();
        String tag = CropFragment.class.getSimpleName();
        Bundle args = new Bundle();
        args.putString(CURRENT_FILE_NAME, getIntent().getStringExtra(CURRENT_FILE_NAME));
        fragment.setArguments(args);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
