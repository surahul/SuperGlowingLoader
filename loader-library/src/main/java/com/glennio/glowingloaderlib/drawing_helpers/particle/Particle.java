package com.glennio.glowingloaderlib.drawing_helpers.particle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.IntDef;
import android.view.animation.DecelerateInterpolator;

import com.glennio.glowingloaderlib.drawing_helpers.AnimationEndCallback;
import com.glennio.glowingloaderlib.drawing_helpers.BaseDrawerCallback;
import com.glennio.glowingloaderlib.drawing_helpers.CommonPropertiesCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

public class Particle {


    public static final int TYPE_CIRCLE = 1;
    public static final int TYPE_TRIANGLE = 2;
    public static final int TYPE_QUADRILATERAL = 3;


    @IntDef({TYPE_CIRCLE, TYPE_TRIANGLE, TYPE_QUADRILATERAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }


    @Type
    private int type;
    private float maxAlpha;
    private float maxRadius;
    private float maxRotation;
    private float maxTranslationX;
    private float maxTranslationY;

    // for Object Animators
    private float size;
    private float halfSize;
    private float oneAndHalfSize;
    private int color;
    private float alpha;
    private float rotation;
    private float translationX;
    private float translationY;


    // for drawing
    Path path = new Path();
    PointF[] points = new PointF[]{new PointF(), new PointF(), new PointF(), new PointF()};

    private AnimationEndCallback animationEndCallback;
    private BaseDrawerCallback baseDrawerCallback;
    private CommonPropertiesCallback commonPropertiesCallback;


    private long duration;
    private Paint paint;
    private AnimatorSet animatorSet;
    private AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            animationEndCallback.onAnimationEnd(Particle.this);
        }
    };

    public Particle(@Type int type, int color, float maxAlpha, float maxRadius, float maxRotation, float maxTranslationX, float maxTranslationY, long duration, CommonPropertiesCallback commonPropertiesCallback, BaseDrawerCallback baseDrawerCallback, AnimationEndCallback animationEndCallback) {
        this.type = type;
        this.color = color;
        this.maxRadius = maxRadius;
        this.maxAlpha = maxAlpha;
        this.duration = duration;
        this.maxRotation = maxRotation;
        this.maxTranslationX = maxTranslationX;
        this.maxTranslationY = maxTranslationY;
        this.paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        this.baseDrawerCallback = baseDrawerCallback;
        this.animationEndCallback = animationEndCallback;
        this.commonPropertiesCallback = commonPropertiesCallback;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        baseDrawerCallback.invalidate();
    }

    public void setSize(float size) {
        this.size = size;
        this.halfSize = size * .5f;
        this.oneAndHalfSize = size * 1.5f;
        baseDrawerCallback.invalidate();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        baseDrawerCallback.invalidate();
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
        baseDrawerCallback.invalidate();
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
        baseDrawerCallback.invalidate();
    }

    public void cancel() {
        if (this.animatorSet != null) {
            this.animatorSet.cancel();
        }
        this.animatorSet = null;
    }

    public void start() {
        cancel();

        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "size", 0, maxRadius);
        radiusAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        radiusAnimator.setDuration(duration);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", maxAlpha, 0);
        alphaAnimator.setInterpolator(new DecelerateInterpolator(2f));
        alphaAnimator.setDuration(duration);


        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, maxRotation);
        rotationAnimator.setInterpolator(new DecelerateInterpolator(2f));
        rotationAnimator.setDuration(duration);

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(this, "translationX", 0, maxTranslationX);
        translationXAnimator.setInterpolator(new DecelerateInterpolator(2f));
        translationXAnimator.setDuration(duration);

        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, maxTranslationY);
        translationYAnimator.setInterpolator(new DecelerateInterpolator(2f));
        translationYAnimator.setDuration(duration);

        animatorSet = new AnimatorSet();
        animatorSet.play(radiusAnimator)
                .with(alphaAnimator)
                .with(rotationAnimator)
                .with(translationXAnimator)
                .with(translationYAnimator);

        animatorSet.addListener(animatorListener);

        animatorSet.start();
    }

    public void draw(Canvas canvas, float[] point, Random random, BlurMaskFilter blurMaskFilter) {

        switch (type) {
            case TYPE_CIRCLE:
                drawCircle(canvas, point, blurMaskFilter);
                break;
            case TYPE_TRIANGLE:
                drawTriangle(canvas, point, random, blurMaskFilter);
                break;
            case TYPE_QUADRILATERAL:
                drawQuadrilateral(canvas, point, random, blurMaskFilter);
                break;
        }


    }

    private void drawCircle(Canvas canvas, float[] point, BlurMaskFilter blurMaskFilter) {
        canvas.save();
        canvas.translate(point[0] + translationX, point[1] + translationY);
        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
        paint.setMaskFilter(null);
        canvas.drawCircle(0, 0, halfSize, paint);
        canvas.restore();
        if (commonPropertiesCallback.showParticleGlowShadow()) {
            canvas.save();
            preparePaintForBlurDrawing(canvas, point, blurMaskFilter);
            canvas.drawCircle(0, 0, 3f * halfSize, paint);
            canvas.restore();
        }


    }

    private void drawTriangle(Canvas canvas, float[] point, Random random, BlurMaskFilter blurMaskFilter) {
        canvas.save();
        canvas.translate(point[0] + translationX, point[1] + translationY);
        canvas.rotate(rotation);


        points[0].set(-halfSize, halfSize);
        points[1].set(halfSize, halfSize);
        points[2].set(0, -halfSize);

        path.reset();
        path.moveTo(points[0].x, points[0].y);
        path.lineTo(points[1].x, points[1].y);
        path.lineTo(points[2].x, points[2].y);
        path.lineTo(points[0].x, points[0].y);
        path.close();

        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
        paint.setMaskFilter(null);
        canvas.drawPath(path, paint);

        canvas.restore();

        if (commonPropertiesCallback.showParticleGlowShadow()) {

            points[0].set((-oneAndHalfSize), oneAndHalfSize);
            points[1].set(oneAndHalfSize, oneAndHalfSize);
            points[2].set(0, -oneAndHalfSize);

            path.reset();
            path.moveTo(points[0].x, points[0].y);
            path.lineTo(points[1].x, points[1].y);
            path.lineTo(points[2].x, points[2].y);
            path.lineTo(points[0].x, points[0].y);
            path.close();

            canvas.save();
            preparePaintForBlurDrawing(canvas, point, blurMaskFilter);
            canvas.drawPath(path, paint);
            canvas.restore();
        }


    }

    private void drawQuadrilateral(Canvas canvas, float[] point, Random random, BlurMaskFilter blurMaskFilter) {
        canvas.save();
        canvas.translate(point[0] + translationX, point[1] + translationY);
        canvas.rotate(rotation);

        points[0].set(-halfSize, halfSize);
        points[1].set(halfSize, halfSize);
        points[2].set(halfSize, -halfSize);
        points[3].set(-halfSize, -halfSize);

        path.reset();
        path.moveTo(points[0].x, points[0].y);
        path.lineTo(points[1].x, points[1].y);
        path.lineTo(points[2].x, points[2].y);
        path.lineTo(points[3].x, points[3].y);
        path.lineTo(points[0].x, points[0].y);
        path.close();
        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
        paint.setMaskFilter(null);
        canvas.drawPath(path, paint);

        canvas.restore();

        if (commonPropertiesCallback.showParticleGlowShadow()) {

            points[0].set(-oneAndHalfSize, oneAndHalfSize);
            points[1].set(oneAndHalfSize, oneAndHalfSize);
            points[2].set(oneAndHalfSize, -oneAndHalfSize);
            points[3].set(-oneAndHalfSize, -oneAndHalfSize);

            path.reset();
            path.moveTo(points[0].x, points[0].y);
            path.lineTo(points[1].x, points[1].y);
            path.lineTo(points[2].x, points[2].y);
            path.lineTo(points[3].x, points[3].y);
            path.lineTo(points[0].x, points[0].y);
            path.close();

            canvas.save();
            preparePaintForBlurDrawing(canvas, point, blurMaskFilter);
            canvas.drawPath(path, paint);
            canvas.restore();
        }

    }

    private void preparePaintForBlurDrawing(Canvas canvas, float[] point, BlurMaskFilter blurMaskFilter) {
        canvas.translate(point[0] + translationX + (commonPropertiesCallback.getGlowShadowDx() * .5f), point[1] + translationY + (commonPropertiesCallback.getGlowShadowDy() * .5f));
        canvas.rotate(rotation);
        paint.setColor(color);
        paint.setAlpha(Math.min(200, (int) (255 * alpha * 2.5f * commonPropertiesCallback.getGlowShadowAlpha())));
        paint.setMaskFilter(blurMaskFilter);
    }


}
