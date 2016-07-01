package com.PopCorp.Purchases.presentation.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class ToolbarBackgroundAlphaBehavior extends AppBarLayout.ScrollingViewBehavior {

    public ToolbarBackgroundAlphaBehavior() {}

    public ToolbarBackgroundAlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}