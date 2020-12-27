package com.applikationsprogramvara.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;


public class ColorPickerPreference extends Preference {
    private PreferenceViewHolder holder;
    private Integer defaultValue;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.colorpicker_preference);

    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        this.holder = holder;
        updatePreview(holder);

    }

    private void updatePreview(PreferenceViewHolder holder) {
        View iv = holder.findViewById(R.id.ivPreview);
        iv.setBackgroundColor(getPersistedInt(defaultValue));
    }

    @Override
    protected void onClick() {
        super.onClick();
        //Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();

        new ColorPickerDialog(getContext()) // ((ContextWrapper) getContext()).getBaseContext())
                .setInitialColor(getPersistedInt(defaultValue))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", color -> {
                    persistInt(color);
                    updatePreview(holder);
                })
                .show();
    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return convertToColorInt(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        this.defaultValue = (Integer) (defaultValue == null ? 0 : defaultValue); // if value is set by user, defaultValue is always null
        super.onSetInitialValue(defaultValue);
    }

    @SuppressLint("Range")
    public static int convertToColorInt(String argb) throws NumberFormatException {

        if (argb.startsWith("#")) {
            argb = argb.replace("#", "");
        }

        int alpha = -1, red = -1, green = -1, blue = -1;

        if (argb.length() == 8) {
            alpha = Integer.parseInt(argb.substring(0, 2), 16);
            red   = Integer.parseInt(argb.substring(2, 4), 16);
            green = Integer.parseInt(argb.substring(4, 6), 16);
            blue  = Integer.parseInt(argb.substring(6, 8), 16);
        } else if (argb.length() == 6) {
            alpha = 255;
            red   = Integer.parseInt(argb.substring(0, 2), 16);
            green = Integer.parseInt(argb.substring(2, 4), 16);
            blue  = Integer.parseInt(argb.substring(4, 6), 16);
        }

        return Color.argb(alpha, red, green, blue);
    }


}
