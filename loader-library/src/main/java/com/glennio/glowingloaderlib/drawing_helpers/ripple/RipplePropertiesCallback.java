package com.glennio.glowingloaderlib.drawing_helpers.ripple;

import android.support.annotation.ColorInt;

import com.glennio.glowingloaderlib.drawing_helpers.CommonPropertiesCallback;

public interface RipplePropertiesCallback extends CommonPropertiesCallback {
    @ColorInt
    int getRippleColor();

    float getRippleAlpha();

    long getRippleDuration();

    float getRippleMinRadius();

    float getRippleMaxRadius();

    float getRippleMinStrokeWidth();

    float getRippleMaxStrokeWidth();
}
