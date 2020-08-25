# ColorPicker
[![](https://jitpack.io/v/applikationsprogramvara/ColorPicker.svg)](https://jitpack.io/#applikationsprogramvara/ColorPicker)

Color picker view library provides selection of a color via human-perceivable form HSB using three selectors: hue, saturation and brightness.

![Library icon](https://github.com/applikationsprogramvara/ColorPicker/blob/master/app/src/main/res/mipmap-xxhdpi/ic_launcher.png?raw=true "Library icon")

## Concept

Concept of color picker dialog with some explanations:

![Color picker dialog concept](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/slider_dialog_concept.png?raw=true "Color picker dialog concept")

## Add color picker to your project

Add it in your root build.gradle at the end of repositories:
``` gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
``` gradle
	dependencies {
	        implementation 'com.github.applikationsprogramvara:ColorPicker:1.0.0'
	}
```

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

