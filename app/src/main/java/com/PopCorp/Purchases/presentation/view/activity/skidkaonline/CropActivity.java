package com.PopCorp.Purchases.presentation.view.activity.skidkaonline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.presentation.view.fragment.skidkaonline.CropFragment;
import com.mikepenz.materialize.MaterializeBuilder;

public class CropActivity extends AppCompatActivity {

    public static final String CURRENT_SALE = "current_sale";
    public static final String CURRENT_FILE_PATH = "current_file_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

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
}
