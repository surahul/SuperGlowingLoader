package com.glennio.glowingloaderlib.drawing_helpers.line;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.glennio.glowingloaderlib.Constants;
import com.glennio.glowingloaderlib.drawing_helpers.BaseDrawerCallback;
import com.glennio.glowingloaderlib.drawing_helpers.particle.ParticlePropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.ripple.RipplePropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.splash.Splash;

import java.util.Random;


public class LineDrawer {

    float[] pointA = new float[2];
    float[] pointB = new float[2];
    float[] pointC = new float[2];
    float[] pointD = new float[2];

    private AnimatorSet animatorSet;
    private Animator alphaAnimator;
    private LineSpec lineSpec;
    private BaseDrawerCallback baseDrawerCallback;
    private LinePropertiesCallback linePropertiesCallback;
    private RipplePropertiesCallback ripplePropertiesCallback;
    private ParticlePropertiesCallback particlePropertiesCallback;
    private Handler handler;
    private Paint paint;
    private Random random;


    // animated values
    private float length;
    private float movement;
    private float fadeAlpha = 1;
    private boolean reverse;
    private boolean hidden;
    private boolean canForcePlayRippleANow;
    private boolean canForcePlayRippleBNow;


    private boolean showSplash;
    private Splash splashA;
    private Splash splashB;

    //objects for drawing
    private Path path = new Path();
    private PathMeasure pathMeasure = new PathMeasure(path, false);
    private BlurMaskFilter blurMaskFilter;


    private ValueAnimator.AnimatorUpdateListener movementUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            checkAndStartRipples();
        }
    };
    private Runnable hiddenFalseRunnable = new Runnable() {
        @Override
        public void run() {
            hidden = false;
        }
    };
    private AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            reverse = !reverse;
            hidden = true;
            handler.postDelayed(hiddenFalseRunnable, lineSpec.getStartDelay());

            startFadeOut();
            canForcePlayRippleANow = true;
            canForcePlayRippleBNow = true;
            if (animatorSet != null)
                animatorSet.start();

        }
    };

    private void startFadeOut() {
        cancelAlphaAnimator();
        alphaAnimator = ObjectAnimator.ofFloat(LineDrawer.this, "fadeAlpha", 1, 0);
        alphaAnimator.setDuration(Constants.LINE_FADE_IN_OUT_DURATION);
        alphaAnimator.start();
        handler.postDelayed(startFadeInRunnable, lineSpec.getStartDelay() - Constants.LINE_FADE_IN_OUT_DURATION);
    }

    private Runnable startFadeInRunnable = new Runnable() {
        @Override
        public void run() {
            cancelAlphaAnimator();
            alphaAnimator = ObjectAnimator.ofFloat(LineDrawer.this, "fadeAlpha", 0, 1);
            alphaAnimator.setDuration(Constants.LINE_FADE_IN_OUT_DURATION);
            alphaAnimator.start();
        }
    };


    //for object animators
    void setLength(float length) {
        this.length = length;
        baseDrawerCallback.invalidate();
    }

    void setMovement(float movement) {
        this.movement = movement;
        baseDrawerCallback.invalidate();
    }

    public void setFadeAlpha(float fadeAlpha) {
        this.fadeAlpha = fadeAlpha;
        baseDrawerCallback.invalidate();
    }

    public LineDrawer(LineSpec lineSpec, boolean showSplash, Random random, BaseDrawerCallback baseDrawerCallback, LinePropertiesCallback linePropertiesCallback, ParticlePropertiesCallback particlePropertiesCallback, RipplePropertiesCallback ripplePropertiesCallback) {
        this.lineSpec = lineSpec;
        this.paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        this.handler = new Handler(Looper.getMainLooper());
        this.showSplash = showSplash;
        this.random = random;
        this.baseDrawerCallback = baseDrawerCallback;
        this.linePropertiesCallback = linePropertiesCallback;
        this.ripplePropertiesCallback = ripplePropertiesCallback;
        this.particlePropertiesCallback = particlePropertiesCallback;
    }

    public void cancel() {
        if (this.animatorSet != null)
            this.animatorSet.cancel();
        if (this.alphaAnimator != null)
            alphaAnimator.cancel();
        this.animatorSet = null;
        this.alphaAnimator = null;
        this.animatorSet = null;
        if (splashA != null) {
            this.splashA.cancel();
        }
        if (splashB != null)
            this.splashB.cancel();
        this.handler.removeCallbacks(startFadeInRunnable);
    }

    private Splash constructRipple() {
        return new Splash(
                random, baseDrawerCallback, ripplePropertiesCallback, particlePropertiesCallback
        );
    }

    public void start() {
        if (animatorSet != null)
            animatorSet.cancel();

        ObjectAnimator lengthAnimator = ObjectAnimator.ofFloat(this, "length", lineSpec.getMinLength(), lineSpec.getMaxLength(), lineSpec.getMinLength());
        lengthAnimator.setInterpolator(lineSpec.getInterpolator());
        lengthAnimator.setDuration(lineSpec.getDuration(linePropertiesCallback.getTotalDuration()));
        lengthAnimator.setStartDelay(lineSpec.getStartDelay());


        ObjectAnimator movementAnimator = ObjectAnimator.ofFloat(this, "movement", 0, 1);
        movementAnimator.setInterpolator(lineSpec.getInterpolator());
        movementAnimator.setDuration(lineSpec.getDuration(linePropertiesCallback.getTotalDuration()));
        movementAnimator.setStartDelay(lineSpec.getStartDelay());
        movementAnimator.addUpdateListener(movementUpdateListener);


        animatorSet = new AnimatorSet();
        animatorSet.play(lengthAnimator).with(movementAnimator);
        animatorSet.addListener(animatorListener);
        animatorSet.start();


    }

    private synchronized void checkAndStartRipples() {
        if (showSplash) {
            if (movement > .25f && movement < .35f) {
                if (reverse) {
                    if (splashB == null)
                        splashB = constructRipple();
                    startRipple(splashB);
                } else {
                    if (splashA == null)
                        splashA = constructRipple();
                    startRipple(splashA);
                }
            }

            if (movement > .75f && movement < .9f) {
                if (reverse) {
                    if (splashA == null)
                        splashA = constructRipple();
                    startRipple(splashA);
                } else {
                    if (splashB == null)
                        splashB = constructRipple();
                    startRipple(splashB);
                }
            }
        }

    }

    private void startRipple(Splash splash) {
        if (!splash.isPlaying() || (splash.equals(splashA) && canForcePlayRippleANow) || (splash.equals(splashB) && canForcePlayRippleBNow)) {
            if (splash.equals(splashA))
                canForcePlayRippleANow = false;
            if (splash.equals(splashB))
                canForcePlayRippleBNow = false;
            splash.starRipple();
        }
    }

    public void draw(Canvas canvas, View view) {
        if (blurMaskFilter == null) {
            float value = linePropertiesCallback.getGlowShadowSpread() * Constants.MAX_GLOW_SHADOW_BLUR;
            if (value > 0)
                blurMaskFilter = new BlurMaskFilter(value, BlurMaskFilter.Blur.NORMAL);
        }
        if (this.animatorSet != null) {


            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();
            float viewHalfWidth = viewWidth / 2f;
            float viewHalfHeight = viewHeight / 2f;


            float x;
            float y;

            // first point
            x = viewHalfWidth - (linePropertiesCallback.getLineWidth() * .5f);
            y = (viewHalfHeight - (linePropertiesCallback.getLineHeight() * .5f)) + .33f * linePropertiesCallback.getLineHeight();
            pointA[0] = x;
            pointA[1] = viewHeight - y;

            // second point
            x = (viewHalfWidth - (linePropertiesCallback.getLineWidth() * .5f)) + .28f * linePropertiesCallback.getLineWidth();
            y = viewHalfHeight + (linePropertiesCallback.getLineHeight() * .5f);
            pointB[0] = x;
            pointB[1] = viewHeight - y;

            // third point
            x = (viewHalfWidth + (linePropertiesCallback.getLineWidth() * .5f)) - .28f * linePropertiesCallback.getLineWidth();
            y = viewHalfHeight - (linePropertiesCallback.getLineHeight() * .5f);
            pointC[0] = x;
            pointC[1] = viewHeight - y;

            // fourth point
            x = viewHalfWidth + (linePropertiesCallback.getLineWidth() * .5f);
            y = (viewHalfHeight + (linePropertiesCallback.getLineHeight() * .5f)) - .33f * linePropertiesCallback.getLineHeight();
            pointD[0] = x;
            pointD[1] = viewHeight - y;


            path.reset();

            if (reverse) {
                path.moveTo(pointA[0], pointA[1]);
                path.lineTo(pointB[0], pointB[1]);
                path.lineTo(pointC[0], pointC[1]);
                path.lineTo(pointD[0], pointD[1]);

            } else {
                path.moveTo(pointD[0], pointD[1]);
                path.lineTo(pointC[0], pointC[1]);
                path.lineTo(pointB[0], pointB[1]);
                path.lineTo(pointA[0], pointA[1]);
            }


            pathMeasure.setPath(path, false);
            float pathLength = pathMeasure.getLength();
            float phase = pathLength * movement;
            DashPathEffect effect = new DashPathEffect(new float[]{length, (pathLength - length)}, Math.max(phase, linePropertiesCallback.getLineStrokeWidth() / 4f));
            paint.setPathEffect(effect);


            paint.setStrokeWidth(linePropertiesCallback.getLineStrokeWidth());
            paint.setColor(lineSpec.getColor());
            paint.setAlpha((int) (255 * fadeAlpha));
            paint.setMaskFilter(null);
            canvas.drawPath(path, paint);

            if (linePropertiesCallback.showLineGlowShadow()) {
                paint.setStrokeWidth(linePropertiesCallback.getLineStrokeWidth() * Constants.MAX_GLOW_SHADOW_SIZE_MULTIPLIER * linePropertiesCallback.getGlowShadowSize());
                paint.setColor(lineSpec.getColor());
                paint.setAlpha((int) (255 * linePropertiesCallback.getGlowShadowAlpha() * fadeAlpha));
                paint.setMaskFilter(blurMaskFilter);
                path.offset(linePropertiesCallback.getGlowShadowDx(), linePropertiesCallback.getGlowShadowDy());
                canvas.drawPath(path, paint);
            }
        }


        if (splashA != null)
            splashA.draw(canvas, pointB);
        if (splashB != null)
            splashB.draw(canvas, pointC);


    }


    private void cancelAlphaAnimator() {
        if (this.alphaAnimator != null)
            this.alphaAnimator.cancel();
        this.alphaAnimator = null;
    }

    public void invalidateBlurMasks() {
        this.blurMaskFilter = null;
        if (splashA != null)
            splashA.invalidateBlurMasks();
        if (splashB != null)
            splashB.invalidateBlurMasks();
    }
}