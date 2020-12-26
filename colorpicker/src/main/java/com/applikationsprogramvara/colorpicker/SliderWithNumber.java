package com.applikationsprogramvara.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SliderWithNumber extends RelativeLayout {
    private SeekBar sb;
    private TextView tv;
    private TextView tvTitle;
    private OnProgressChange onProgressChange;
    private OnLayoutChange onLayoutChange;

    public SliderWithNumber(Context context) {
        super(context);
        init(context, null);
    }

    public SliderWithNumber(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SliderWithNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int[] set = {
                android.R.attr.max,
                android.R.attr.progress
        };

        TypedArray attributes = context.obtainStyledAttributes(attrs, set);
        int max = attributes.getInt(0, 100);    // android.R.attr.max,
        @SuppressLint("ResourceType") int progress = attributes.getInt(1, 0); // android.R.attr.progress
        attributes.recycle();

        attributes = context.obtainStyledAttributes(attrs, R.styleable.SliderWithNumber);
        String suffix = attributes.getString(R.styleable.SliderWithNumber_suffix);
        String title = attributes.getString(R.styleable.SliderWithNumber_title);
        attributes.recycle();

        View view = inflate(getContext(), R.layout.slider, null);
        addView(view);

        sb = view.findViewById(R.id.sb1);
        tv = view.findViewById(R.id.tv1);
        tvTitle = view.findViewById(R.id.tv2);
        tvTitle.setText(title);
        textColor = tvTitle.getCurrentTextColor();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onProgressChange != null)
                    onProgressChange.action(progress, fromUser);

                tv.setText(String.valueOf(progress));
                shiftText(tv, sb);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sb.setMax(max);
        sb.setProgress(progress);
        sb.setSaveEnabled(false); // otherwise progress was always set to 100 on orientation change

        tv.addOnLayoutChangeListener((v, l1, t1, r1, b1, l2, t2, r2, b2) -> {
            int diff = tv.getLeft() - tvTitle.getRight();
            float d = tvTitle.getContext().getResources().getDisplayMetrics().density * 30f;
            float alpha;
            if (diff <= 0)
                alpha = 0;
            else if (diff > d)
                alpha = 1;
            else
                alpha = diff / d;

            tvTitle.setTextColor(Color.argb((int) (alpha * 255f), Color.red(textColor), Color.green(textColor), Color.blue(textColor)));
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            if (onLayoutChange != null)
                onLayoutChange.action(sb);
            shiftText(tv, sb);
        }
    }

    private static void shiftText(TextView tv, SeekBar sb) {
        Rect r = sb.getThumb().getBounds();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
        params.leftMargin = r.left  + r.width() / 2 - tv.getWidth() / 2;
        tv.requestLayout();
    }

    interface  OnProgressChange {
        void action(int progress, boolean fromUser);
    }

    public void setOnProgressChangeListener(OnProgressChange onProgressChange) {
        this.onProgressChange = onProgressChange;
    }

    interface OnLayoutChange {
        void action(SeekBar sb);
    }

    public void setOnLayoutChangeListener(OnLayoutChange onLayoutChange) {
        this.onLayoutChange = onLayoutChange;
    }

    public void setColorFilter(int color) {
        sb.getThumb().setColorFilter(color, PorterDuff.Mode.DARKEN);
    }

    public void setProgress(int progress) {
        sb.setProgress(progress);
    }

    public void changeGradient(int color1, int color2) {
        Rect r = sb.getThumb().getBounds();
        LinearGradient gradient = new LinearGradient(0.f, 0.f, sb.getWidth() - r.width(), sb.getHeight(), //SweepGradient
                new int[] { color1, color2 }, null, Shader.TileMode.CLAMP);

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(gradient);
        sb.setProgressDrawable(shape);
    }

}
