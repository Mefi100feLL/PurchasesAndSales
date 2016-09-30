package com.PopCorp.Purchases.presentation.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.getkeepsafe.taptargetview.ViewTapTarget;

public class TapTargetManager {

    public static class Builder extends ViewTapTarget {

        private Activity activity;
        private TapTargetView.Listener listener;

        protected Builder(Activity activity, View view, CharSequence title, @Nullable CharSequence description) {
            super(view, title, description);
            init(activity);
        }

        public Builder(Activity activity, View view, int titleRes, int contentRes){
            super(view, activity.getString(titleRes), contentRes != 0 ? activity.getString(contentRes) : null);
            init(activity);
        }

        private void init(Activity activity) {
            this.activity = activity;
            listener = new TapTargetView.Listener() {
                @Override
                public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);
                }
            };
            outerCircleColor(ThemeManager.getInstance().getPrimaryColorRes());
            targetCircleColor(R.color.md_white_1000);
            textColor(R.color.md_white_1000);
            dimColor(R.color.md_black_1000);
            drawShadow(true);
            cancelable(false);
            tintTarget(true);
        }

        public Builder listener(TapTargetView.Listener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public Builder tintTarget(boolean tint) {
            super.tintTarget(tint);
            return this;
        }

        @Override
        public Builder outerCircleColor(@ColorRes int color) {
            super.outerCircleColor(color);
            return this;
        }

        public void show() {
            try {
                TapTargetView.showFor(activity, this, listener);
            } catch (Exception e){
                ErrorManager.printStackTrace(e);
                AnalyticsTrackers.getInstance().sendError(e);
            }
        }
    }
}
