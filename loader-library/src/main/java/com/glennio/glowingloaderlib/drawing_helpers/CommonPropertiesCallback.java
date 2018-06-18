package com.glennio.glowingloaderlib.drawing_helpers;

public interface CommonPropertiesCallback {
    boolean showLineGlowShadow();
    boolean showRippleGlowShadow();
    boolean showParticleGlowShadow();
    float getGlowShadowDx();
    float getGlowShadowDy();
    float getGlowShadowSpread();
    float getGlowShadowSize();
    float getGlowShadowAlpha();
}
