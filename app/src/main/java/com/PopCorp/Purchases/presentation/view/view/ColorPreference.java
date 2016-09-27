package com.PopCorp.Purchases.presentation.view.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.preference.Preference;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.utils.ThemeManager;
import com.PopCorp.Purchases.presentation.common.ColorDialog;

/**
 * Created by Apopsuenko on 26.09.2016.
 */

public class ColorPreference extends Preference implements ColorDialog.ColorCallback{

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWidgetLayoutResource(R.layout.mp_preference_color);
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.mp_preference_color);
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.mp_preference_color);
    }

    public ColorPreference(Context context) {
        super(context);
        setWidgetLayoutResource(R.layout.mp_preference_color);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ImageView image = (ImageView) view.findViewById(R.id.color_image);
        ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(ThemeManager.getInstance().getColor(getKey()));
        image.setBackgroundDrawable(coloredCircle);
    }

    @Override
    protected void onClick() {
        super.onClick();
        new ColorDialog.Builder((AppCompatActivity) getContext(), this, R.string.dialog_title_selecting_color)
                .titleSub(R.string.dialog_title_selecting_color)
                .allowUserColorInput(false)
                .accentMode(false)
                .customColors(R.array.primary_colors, null)
                .doneButton(R.string.dialog_button_select)
                .backButton(R.string.dialog_button_back)
                .cancelButton(R.string.dialog_button_cancel)
                .preselect(ThemeManager.getInstance().getColor(getKey()))
                .show();
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        /*if (restorePersistedValue) {
            // Restore existing state
            mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }*/
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, ThemeManager.getInstance().getColor(getKey()));
    }

    @Override
    public void onColorSelection(@NonNull ColorDialog dialog, @ColorInt int selectedColor, int selectedPosition) {
        if (ThemeManager.getInstance().putColor(getKey(), selectedPosition) && getOnPreferenceChangeListener() != null){
            getOnPreferenceChangeListener().onPreferenceChange(this, selectedPosition);
        }
    }
}
