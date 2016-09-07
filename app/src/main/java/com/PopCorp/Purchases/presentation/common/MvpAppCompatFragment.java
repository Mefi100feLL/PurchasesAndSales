package com.PopCorp.Purchases.presentation.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.PopCorp.Purchases.AnalyticsTrackers;
import com.PopCorp.Purchases.data.utils.AnalitycsManager;
import com.arellomobile.mvp.MvpDelegate;

public class MvpAppCompatFragment extends Fragment {

    private MvpDelegate<? extends MvpAppCompatFragment> mMvpDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getMvpDelegate().onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getMvpDelegate().onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onStop();
    }

    /**
     * @return The {@link MvpDelegate} being used by this Fragment.
     */
    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }
        return mMvpDelegate;
    }
}