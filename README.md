# ColorPicker
[![](https://jitpack.io/v/applikationsprogramvara/ColorPicker.svg)](https://jitpack.io/#applikationsprogramvara/ColorPicker/1.0.1)

Color picker view library provides selection of a color via human-perceivable form [HSB](https://en.wikipedia.org/wiki/HSL_and_HSV) using three selectors: hue, saturation and brightness.
The code is very simple and small (on August 2020).

![Library icon](https://github.com/applikationsprogramvara/ColorPicker/blob/master/app/src/main/res/mipmap-xxhdpi/ic_launcher.png?raw=true "Library icon")

## Concept and screenshots

Concept of color picker dialog with some hints:
* Initial and current colors are placed side-by-side, so the difference can be clearly visible
* Numbers behind each component provide the ability to "recreate" the color later
* No puzzles which color you get with [HSB](https://en.wikipedia.org/wiki/HSL_and_HSV) model

![Color picker dialog concept](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/slider_dialog_concept.png?raw=true "Color picker dialog concept")

Live screenshots:

Dark theme             |  Light theme with transparency
:-------------------------:|:-------------------------:
![Screenshot dark theme](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/screenshot_dark-theme.png?raw=true "Screenshot dark theme") | ![Screenshot light theme with transparency](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/screenshot_light-theme_w_transparency.png?raw=true "Screenshot light theme with transparency")

Action demo:

Slider stripes and manipulator dots adapt their color on moving other sliders.

![Color picker dialog concept](https://github.com/applikationsprogramvara/ColorPicker/blob/master/imgs/action_demo.gif?raw=true "Color picker dialog concept")

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
	        implementation 'com.github.applikationsprogramvara:ColorPicker:1.0.1'
	}
```

## Usage

Color picker is ready to use out of the box with several lines added to your code.

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
* Preference
* Get rid of jumping color patches due to extra digit

