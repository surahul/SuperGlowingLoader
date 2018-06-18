package com.glennio.glowingloaderlib.drawing_helpers.particle;

import android.support.annotation.ColorInt;

import com.glennio.glowingloaderlib.drawing_helpers.CommonPropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.particle.Particle;

public interface ParticlePropertiesCallback extends CommonPropertiesCallback {


    int getMinParticlesCount();

    int getMaxParticlesCount();

    @Particle.Type
    int[] getParticleTypes();

    @ColorInt
    int[] getParticleColors();

    float getMaxParticleAlpha();

    float getMinParticleAlpha();

    float getMinParticleSize();

    float getMaxParticleSize();

    float getMinParticleRotation();

    float getMaxParticleRotation();

    float getMinParticleTranslation();

    float getMaxParticleTranslation();

    long getParticlesAnimationDuration();
}
