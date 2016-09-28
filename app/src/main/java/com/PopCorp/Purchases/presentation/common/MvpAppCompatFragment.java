package com.PopCorp.Purchases.presentation.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.PopCorp.Purchases.AnalyticsTrackers;
import com.arellomobile.mvp.MvpDelegate;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MvpAppCompatFragment extends Fragment {

    private MvpDelegate<? extends MvpAppCompatFragment> mMvpDelegate;

    protected Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);

        tracker = AnalyticsTrackers.getInstance().getDefault();
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
    public void onResume() {
        super.onResume();
        tracker.setScreenName(getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
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