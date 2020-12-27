package com.applikationsprogramvara.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SliderWithNumber extends RelativeLayout {
    private SeekBar sb;
    private TextView tv;
    private TextView tvTitle;
    private OnProgressChange onProgressChange;
    private OnLayoutChange onLayoutChange;
    private int textColor;

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

                tv.setText(String.valueOf(progress) + suffix);
                shiftText();
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
            shiftText();
        }
    }

    private void shiftText() {
        tv.measure(0, 0);
        Rect thumbRect = sb.getThumb().getBounds();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
        params.leftMargin = Math.min(
                thumbRect.left + thumbRect.width() / 2 - tv.getMeasuredWidth() / 2,
                sb.getWidth() - tv.getMeasuredWidth()
        );
        tv.requestLayout();
    }

    public float normalize(int progress) {
        return (float) progress / sb.getMax();
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

    static RoundRectShape getRect(SeekBar sb) {
        float r = sb.getHeight() / 4f;
        return new RoundRectShape(new float[]{r, r, r, r, r, r, r, r}, null, null);
    }

    public void setColorFilter(int color) {
        // option 1
//        sb.getThumb().setColorFilter(color, PorterDuff.Mode.DARKEN);
        sb.getThumb().clearColorFilter();

        Rect thumbRect = sb.getThumb().getBounds();
        if (thumbRect.width() == 0 || thumbRect.height() == 0) return;

        float radius = thumbRect.width() / 2f;

        // create bitmap
        Bitmap bitmap = Bitmap.createBitmap(thumbRect.width(), thumbRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // draw checker if the color is transparent
        if (Color.alpha(color) < 0xFF) {
            TypedValue a = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT)
                canvas.drawColor(a.data);

            Drawable checkerboard = ContextCompat.getDrawable(getContext(), R.drawable.checkerboard_background);
            checkerboard.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            checkerboard.draw(canvas);
        }

        // draw the color and the outline
        canvas.drawColor(color);
        canvas.drawCircle(radius, radius, radius, getPaintOutline(getContext()));

        // mask the thumb by circle
        Bitmap maskBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);
        maskCanvas.drawCircle(radius, radius, radius, new Paint());

        // mask the final bitmap and assign to the thumb
        canvas.drawBitmap(maskBitmap, 0, 0, new Paint() {{ setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); }});
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        sb.setThumb(drawable);
    }

    public void setProgress(float progress) {
        sb.setProgress((int) (progress * sb.getMax()));
    }

    public void changeGradient(int color1, int color2) {
        if (sb.getWidth() == 0 || sb.getHeight() == 0) return;
        Rect r = sb.getThumb().getBounds();
        LinearGradient gradient = new LinearGradient(0.f, 0.f, sb.getWidth() - r.width(), sb.getHeight(), //SweepGradient
                new int[] { color1, color2 }, null, Shader.TileMode.CLAMP);

        // option 1
//        ShapeDrawable shape = new ShapeDrawable(SliderWithNumber.getRect(sb));
//        shape.getPaint().setShader(gradient);
//
//        Drawable bbb = ContextCompat.getDrawable(getContext(), R.drawable.checkerboard_background);
//        LayerDrawable a = new LayerDrawable(new Drawable[] { bbb, shape} );

        // create bitmap
        Bitmap bitmap = Bitmap.createBitmap(sb.getWidth() - r.width(), sb.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // draw checker if one color is transparent
        if (color1 == Color.TRANSPARENT) {
            Drawable checkerboard = ContextCompat.getDrawable(getContext(), R.drawable.checkerboard_background);
            checkerboard.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            checkerboard.draw(canvas);
        }

        canvas.drawPaint(new Paint() {{ setShader(gradient); }});

        Bitmap maskBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);

        // prepare a shape for outline an mask
        float r2 = sb.getHeight() / 4f;
        RoundRectShape maskShape = new RoundRectShape(new float[] {r2, r2, r2, r2, r2, r2, r2, r2}, null, null);
//        maskShape.resize(bitmap.getWidth(), bitmap.getHeight() / 2f);

        ShapeDrawable shapeDrawable = new ShapeDrawable(maskShape);
        int verticalOffset = (int) (sb.getHeight() * .2f);
        shapeDrawable.setBounds(0, verticalOffset, bitmap.getWidth(), bitmap.getHeight() - verticalOffset);
        shapeDrawable.draw(maskCanvas);

        // draw outline
        shapeDrawable.getPaint().set(getPaintOutline(getContext()));
        shapeDrawable.draw(canvas);

        // mask the final bitmap and assign to the seekbar
        canvas.drawBitmap(maskBitmap, 0, 0, new Paint() {{ setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); }});
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        sb.setProgressDrawable(drawable);
    }


    static Paint getPaintOutline(final Context context) {

        // select outline color bases on window color
        TypedValue a = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        int color;
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {

            if (a.data < 0xFF888888) // dark mode
                color = 0xFF444444;
            else // light mode
                color = 0xFFCCCCCC;
        } else {
            color = 0xFF888888;
        }


        return new Paint() {{
            setStyle(Style.STROKE);
            setStrokeWidth(context.getResources().getDisplayMetrics().density * 2);
            setColor(color);
        }};
    }

}
