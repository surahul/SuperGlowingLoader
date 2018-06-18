package com.glennio.glowingloaderlib.drawing_helpers.line;

import com.glennio.glowingloaderlib.drawing_helpers.CommonPropertiesCallback;

public interface LinePropertiesCallback extends CommonPropertiesCallback {
    long getTotalDuration();

    float getLineWidth();

    float getLineHeight();

    float getLineStrokeWidth();
}
