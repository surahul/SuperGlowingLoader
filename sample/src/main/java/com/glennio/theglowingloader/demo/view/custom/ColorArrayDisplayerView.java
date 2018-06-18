package com.glennio.theglowingloader.demo.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.glennio.theglowingloader.Utils;

public class ColorArrayDisplayerView extends View {


    public ColorArrayDisplayerView(Context context) {
        super(context);
        init();
    }

    public ColorArrayDisplayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorArrayDisplayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorArrayDisplayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private int[] colors;
    private int pad = Utils.dpToPx(10);
    private int maxRadius = Utils.dpToPx(40);
    private int ringColor = 0Xff0a091d;
    private int strokeWidth = Utils.dpToPx(3);
    private Paint paint;

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colors != null && colors.length > 0) {
            float totalWidth = getMeasuredWidth();
            float colorCircleRadius = (Math.min((((totalWidth / colors.length) - pad)), maxRadius))*.5f;
            for (int i = 0; i < colors.length; i++) {
                float x = (colorCircleRadius * 2 * i) + pad/2f + colorCircleRadius;
                float y = getMeasuredHeight() / 2f;
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(colors[i]);
                canvas.drawCircle(x, y, colorCircleRadius*.8f, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                paint.setColor(ringColor);
                canvas.drawCircle(x, y, colorCircleRadius*.8f, paint);
            }
        }
    }
}
