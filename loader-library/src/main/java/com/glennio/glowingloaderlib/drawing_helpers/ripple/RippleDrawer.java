package com.glennio.glowingloaderlib.drawing_helpers.ripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;

import com.glennio.glowingloaderlib.drawing_helpers.AnimationEndCallback;
import com.glennio.glowingloaderlib.drawing_helpers.BaseDrawerCallback;
import com.glennio.glowingloaderlib.drawing_helpers.CommonPropertiesCallback;

public class RippleDrawer {

    private AnimationEndCallback animationEndCallback;
    private BaseDrawerCallback drawerCallback;

    private float maxRadius;
    private float maxStroke;
    private float maxAlpha;


    private int color;
    private float alpha;
    private float radius;
    private float stroke;
    private long duration;
    private Paint paint;

    private CommonPropertiesCallback commonPropertiesCallback;

    private AnimatorSet animatorSet;


    public RippleDrawer(int color, float maxAlpha, float maxRadius, float maxStroke, long duration, CommonPropertiesCallback commonPropertiesCallback, BaseDrawerCallback drawerCallback, AnimationEndCallback animationEndCallback) {
        this.color = color;
        this.maxRadius = maxRadius;
        this.maxAlpha = maxAlpha;
        this.maxStroke = maxStroke;
        this.duration = duration;
        this.paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        this.drawerCallback = drawerCallback;
        this.animationEndCallback = animationEndCallback;
        this.commonPropertiesCallback = commonPropertiesCallback;
    }


    // for object animators
    void setAlpha(float alpha) {
        this.alpha = alpha;
        drawerCallback.invalidate();
    }

    void setRadius(float radius) {
        this.radius = radius;
        drawerCallback.invalidate();
    }

    void setStroke(float stroke) {
        this.stroke = stroke;
        drawerCallback.invalidate();
    }

    public void cancel() {
        if (this.animatorSet != null) {
            this.animatorSet.cancel();
        }
        this.animatorSet = null;
    }

    public void start() {
        cancel();

        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0, maxRadius);
        radiusAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        radiusAnimator.setDuration(duration);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", maxAlpha, 0);
        alphaAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        alphaAnimator.setDuration(duration);

        ObjectAnimator strokeAnimator = ObjectAnimator.ofFloat(this, "stroke", maxStroke, 0);
        strokeAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        strokeAnimator.setDuration(duration);


        animatorSet = new AnimatorSet();
        animatorSet.play(radiusAnimator)
                .with(alphaAnimator)
                .with(strokeAnimator);

        animatorSet.addListener(animatorListener);

        animatorSet.start();

    }

    private AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            animationEndCallback.onAnimationEnd(RippleDrawer.this);
        }
    };

    public void draw(Canvas canvas, float[] point, BlurMaskFilter blurMaskFilter) {
        paint.setStrokeWidth(stroke);
        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
        paint.setMaskFilter(null);
        canvas.drawCircle(point[0], point[1], radius, paint);

        if (commonPropertiesCallback.showRippleGlowShadow()) {
            paint.setColor(color);
            paint.setAlpha((int) (255 * alpha * 2.5f * commonPropertiesCallback.getGlowShadowAlpha()));
            paint.setMaskFilter(blurMaskFilter);
            canvas.drawCircle(point[0] + commonPropertiesCallback.getGlowShadowDx(), point[1] + commonPropertiesCallback.getGlowShadowDy(), radius * 1.3f, paint);
        }

    }

    public boolean isPlaying() {
        return animatorSet != null && animatorSet.isRunning();
    }
}
