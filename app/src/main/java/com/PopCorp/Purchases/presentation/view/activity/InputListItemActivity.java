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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getInstance().getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_listitem);

        Fragment fragment = new InputListItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(InputListItemFragment.CURRENT_LISTITEM, getIntent().getParcelableExtra(InputListItemFragment.CURRENT_LISTITEM));
        args.putLong(InputListItemFragment.CURRENT_LIST, getIntent().getLongExtra(InputListItemFragment.CURRENT_LIST, -1));
        args.putString(InputListItemFragment.CURRENT_CURRENCY, getIntent().getStringExtra(InputListItemFragment.CURRENT_CURRENCY));
        fragment.setArguments(args);
        String tag = fragment.getClass().getSimpleName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.content, fragment, tag).commit();
        }
    }
}
