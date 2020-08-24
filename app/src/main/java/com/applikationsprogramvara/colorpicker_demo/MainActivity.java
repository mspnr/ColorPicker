package com.applikationsprogramvara.colorpicker_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.applikationsprogramvara.colorpicker.ColorPickerDialog;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeSolid(View view) {

        View colorPatch = findViewById(R.id.colorPatch);
        new ColorPickerDialog(this)
                .setTitle("Color picker dialog")
                .setInitialParameters(((ColorDrawable) colorPatch.getBackground()).getColor(), false)
                .setPositiveButton("OK", color -> colorPatch.setBackgroundColor(color))
                .setNegativeButton("Cancel", null)
                .show();

    }

    public void changeTransparent(View view) {

        View colorPatch = findViewById(R.id.colorPatch);
        new ColorPickerDialog(this)
                .setTitle("Color picker dialog")
                .setInitialParameters(((ColorDrawable) colorPatch.getBackground()).getColor(), true)
                .setPositiveButton("OK", color -> colorPatch.setBackgroundColor(color))
                .setNegativeButton("Cancel", null)
                .show();

    }
}