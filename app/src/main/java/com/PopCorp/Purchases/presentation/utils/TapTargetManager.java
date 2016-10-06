package com.PopCorp.Purchases.presentation.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.analytics.AnalyticsTrackers;
import com.PopCorp.Purchases.data.utils.ErrorManager;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

public class TapTargetManager {

    private Activity activity;
    private TapTarget tapTarget;
    private TapTargetView.Listener listener;

    public TapTargetManager(Activity activity){
        this.activity = activity;
        listener = new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
            }
        };
    }

    public static TapTarget forView(Activity activity, View view, int titleRes, int contentRes){
        return init(TapTarget.forView(view, activity.getString(titleRes), contentRes != 0 ? activity.getString(contentRes) : null));
    }

    public static TapTarget forToolbarMenuItem(Activity activity, Toolbar toolbar, @IdRes int menuItemId, int titleRes, int contentRes){
        return init(TapTarget.forToolbarMenuItem(toolbar, menuItemId, activity.getString(titleRes), contentRes != 0 ? activity.getString(contentRes) : null));
    }

    public static TapTarget forToolbarNavigationIcon(Activity activity, Toolbar toolbar, int titleRes, int contentRes){
        return init(TapTarget.forToolbarNavigationIcon(toolbar, activity.getString(titleRes), contentRes != 0 ? activity.getString(contentRes) : null));
    }

    public static TapTarget forToolbarOverflow(Activity activity, Toolbar toolbar, int titleRes, int contentRes){
        return init(TapTarget.forToolbarOverflow(toolbar, activity.getString(titleRes), contentRes != 0 ? activity.getString(contentRes) : null));
    }

    private static TapTarget init(TapTarget tapTarget) {
        tapTarget.outerCircleColor(ThemeManager.getInstance().getPrimaryColorRes());
        tapTarget.targetCircleColor(R.color.md_white_1000);
        tapTarget.textColor(R.color.md_white_1000);
        tapTarget.dimColor(R.color.md_black_1000);
        tapTarget.drawShadow(true);
        tapTarget.cancelable(false);
        tapTarget.tintTarget(true);
        return tapTarget;
    }

    public void show(){
        try {
            TapTargetView.showFor(activity, tapTarget, listener);
        } catch (Exception e){
            ErrorManager.printStackTrace(e);
            AnalyticsTrackers.getInstance().sendError(e);
        }
    }

    public TapTargetManager tapTarget(TapTarget tapTarget){
        this.tapTarget = tapTarget;
        return this;
    }

    public TapTargetManager listener(TapTargetView.Listener listener) {
        this.listener = listener;
        return this;
    }
}
