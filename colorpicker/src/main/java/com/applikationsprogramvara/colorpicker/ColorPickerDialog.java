package com.applikationsprogramvara.colorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

public class ColorPickerDialog {

    private final AppCompatActivity activity;
    private int initialColor;
    private boolean transparentColorsAvailable;

    private String title;
    private int titleID;
    private String positiveButtonText;
    private int positiveButtonID;
    private String negativeButtonText;
    private int negativeButtonID;

    private ColorSelectionListener listenerPositive;
    private ColorSelectionListener listenerNegative;

    public ColorPickerDialog(Context context) {
        activity = scanForActivity(context);
    }

    public ColorPickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ColorPickerDialog setTitle(int titleID) {
        this.titleID = titleID;
        return this;
    }

    public ColorPickerDialog setPositiveButton(String text, ColorSelectionListener listener) {
        this.positiveButtonText = text;
        this.listenerPositive = listener;
        return this;
    }

    public ColorPickerDialog setPositiveButton(int textID, ColorSelectionListener listener) {
        this.positiveButtonID = textID;
        this.listenerPositive = listener;
        return this;
    }

    public ColorPickerDialog setNegativeButton(String text, ColorSelectionListener listener) {
        this.negativeButtonText = text;
        this.listenerNegative = listener;
        return this;
    }

    public ColorPickerDialog setNegativeButton(int textID, ColorSelectionListener listener) {
        this.negativeButtonID = textID;
        this.listenerNegative = listener;
        return this;
    }

    public ColorPickerDialog setInitialColor(int color) {
        initialColor = color;
        return this;
    }

    public ColorPickerDialog setInitialParameters(int initialColor, boolean transparentColorsAvailable) {
        this.initialColor = initialColor;
        this.transparentColorsAvailable = transparentColorsAvailable;
        return this;
    }

    public void show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null)
            builder.setTitle(title);
        if (titleID != 0)
            builder.setTitle(titleID);

        ColorPickerSlidersView view = new ColorPickerSlidersView(activity);
        view.setInitialParameters(initialColor == 0 ? Color.RED : initialColor, transparentColorsAvailable);
        builder.setView(view);

        if (positiveButtonText != null)
            builder.setPositiveButton(positiveButtonText, (d, w) -> {
                if (listenerPositive != null)
                    listenerPositive.onColorSelected(view.getColor());
            });
        if (positiveButtonID != 0)
            builder.setPositiveButton(positiveButtonID, (d, w) -> {
                if (listenerPositive != null)
                    listenerPositive.onColorSelected(view.getColor());
            });
        if (negativeButtonText != null)
            builder.setNegativeButton(negativeButtonText, (d, w) -> {
                if (listenerNegative != null)
                    listenerNegative.onColorSelected(view.getColor());
            });
        if (negativeButtonID != 0)
            builder.setNegativeButton(negativeButtonID, (d, w) -> {
                if (listenerNegative != null)
                    listenerNegative.onColorSelected(view.getColor());
            });

        builder.show();

    }

    public interface ColorSelectionListener {
        void onColorSelected(int color);
    }

    private static AppCompatActivity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof AppCompatActivity)
            return (AppCompatActivity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }

}
