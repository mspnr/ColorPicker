# ColorPicker
Color picker view library provides selection of a color via human-perceivable form HSB using three selectors: hue, saturation and brightness.

## Concept

Concept of color picker dialog with some explanations:

![Color picker dialog concept](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/slider_dialog_concept.png?raw=true "Color picker dialog concept")

Library icon:

![Library icon](https://github.com/applikationsprogramvara/ColorPicker/blob/master/app/src/main/res/mipmap-xxhdpi/ic_launcher.png?raw=true "Library icon")

## Usage

As a dialog:

``` java
    new ColorPickerDialog(this)
            .setTitle("Color picker dialog")
            .setInitialParameters( initalColor, false)
            .setPositiveButton("OK", color -> {
                // process the new color
            })
            .setNegativeButton("Cancel", null)
            .show();
```

As a view in XML:

``` xml
    <com.applikationsprogramvara.colorpicker.ColorPickerSlidersView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:transparency="false"
        app:initialColor="#9B3232"
        />
```

## Things to do

* Manupulator theming and contrast on low brightness
* Alternative one-tap palette with all possible colors
* Landscape mode
* Digital input and copy/paste HTML-colors

