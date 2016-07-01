package com.PopCorp.Purchases.presentation.common;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;
import com.github.machinarius.preferencefragment.PreferenceFragment;

public class MvpPreferenceFragment extends PreferenceFragment {

    private MvpDelegate<? extends PreferenceFragment> mMvpDelegate;

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