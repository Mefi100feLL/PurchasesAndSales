package com.PopCorp.Purchases.presentation.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.MvpAppCompatActivity;
import com.PopCorp.Purchases.presentation.view.fragment.InputListItemFragment;

public class InputListItemActivity extends MvpAppCompatActivity {

    public static final String CURRENT_LISTS = "current_list";
    public static final String CURRENT_LISTITEM = "current_listitem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_listitem);
        ThemeManager.getInstance().setStatusBarColor(this);

        Fragment fragment = InputListItemFragment.create(
                getIntent().getParcelableExtra(InputListItemFragment.CURRENT_LISTITEM),
                getIntent().getLongArrayExtra(InputListItemFragment.CURRENT_LISTS)
        );

        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
