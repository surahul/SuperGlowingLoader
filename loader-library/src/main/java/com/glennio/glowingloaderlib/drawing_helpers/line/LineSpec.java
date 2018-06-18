package com.glennio.glowingloaderlib.drawing_helpers.line;

import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;

public class LineSpec {

    public static final Interpolator DEFAULT_INTERPOLATOR_SHORT_LINES = PathInterpolatorCompat.create(.8f, 0, .3f, 1);
    public static final Interpolator DEFAULT_INTERPOLATOR_LONG_LINES = PathInterpolatorCompat.create(.9f, 0, .2f, 1);

    private int minLength;
    private int maxLength;
    private int color;
    private long startDelay;
    private Interpolator interpolator;


    public LineSpec(int length, int color, int startDelay) {
        this(0, length, color, startDelay);
    }


    public LineSpec(int minLength, int maxLength, int color, int startDelay) {
        this(minLength, maxLength, color, startDelay, DEFAULT_INTERPOLATOR_SHORT_LINES);
    }


    public LineSpec(int minLength, int maxLength, int color, int startDelay, Interpolator interpolator) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.color = color;
        this.startDelay = startDelay;
        this.interpolator = interpolator;
    }


    public int getColor() {
        return color;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public long getDuration(long loaderDuration) {
        return loaderDuration - startDelay;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }
}
