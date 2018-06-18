package com.glennio.glowingloaderlib;

import android.graphics.Color;

import com.glennio.glowingloaderlib.drawing_helpers.line.LineSpec;
import com.glennio.glowingloaderlib.drawing_helpers.particle.Particle;

public interface Constants {


    // global default properties

    LineSpec[] DEFAULT_LINE_SPECS = new LineSpec[]{
            new LineSpec(
                    0,
                    Utils.dpToPx(150),
                    Color.WHITE,
                    200
            ),
            new LineSpec(
                    0,
                    Utils.dpToPx(70),
                    0XFFEA4E5A,
                    650
            )
    };

    boolean DEFAULT_SHOW_LINE_GLOW_SHADOW = true;
    boolean DEFAULT_SHOW_RIPPLE_GLOW_SHADOW = true;
    boolean DEFAULT_SHOW_PARTICLE_GLOW_SHADOW = false;
    int DEFAULT_GLOW_SHADOW_DX = 0;
    int DEFAULT_GLOW_SHADOW_DY = Utils.dpToPx(40);
    float DEFAULT_GLOW_SHADOW_SIZE = .75f;
    float DEFAULT_GLOW_SHADOW_SPREAD = .6f;
    float DEFAULT_GLOW_SHADOW_ALPHA = .14f;


    // line default properties
    long DEFAULT_DURATION = 1400;
    float DEFAULT_ASPECT = 2.5f;
    int DEFAULT_LINE_STROKE_WIDTH = Utils.dpToPx(10);


    //particles default properties
    int[] DEFAULT_PARTICLE_COLORS = new int[]{ Color.WHITE, 0XffFFD75E, 0XFF00BEFF,Color.WHITE, 0XC56FF6};

    int DEFAULT_MIN_PARTICLE_SIZE = Utils.dpToPx(16);
    int DEFAULT_MAX_PARTICLE_SIZE = Utils.dpToPx(22);

    float DEFAULT_MIN_PARTICLE_ALPHA = .6f;
    float DEFAULT_MAX_PARTICLE_ALPHA = .9f;

    @Particle.Type
    int[] DEFAULT_PARTICLE_TYPES = new int[]{
            Particle.TYPE_QUADRILATERAL,
            Particle.TYPE_TRIANGLE,
            Particle.TYPE_QUADRILATERAL,
            Particle.TYPE_TRIANGLE,
            Particle.TYPE_TRIANGLE,
            Particle.TYPE_CIRCLE
    };

    int DEFAULT_MIN_PARTICLE_COUNT = 3;
    int DEFAULT_MAX_PARTICLE_COUNT = 6;

    int DEFAULT_MIN_PARTICLE_ROTATION = 60;
    int DEFAULT_MAX_PARTICLE_ROTATION = 720;

    int DEFAULT_MIN_PARTICLE_TRANSLATION = Utils.dpToPx(50);
    int DEFAULT_MAX_PARTICLE_TRANSLATION = Utils.dpToPx(70);


    // ripple default properties
    long DEFAULT_RIPPLE_DURATION = 1100;
    int DEFAULT_MIN_RIPPLE_RADIUS = Utils.dpToPx(40);
    int DEFAULT_MAX_RIPPLE_RADIUS = Utils.dpToPx(60);
    int DEFAULT_MIN_RIPPLE_STROKE_WIDTH = Utils.dpToPx(9);
    int DEFAULT_MAX_RIPPLE_STROKE_WIDTH = Utils.dpToPx(12);
    int DEFAULT_RIPPLE_COLOR = Color.WHITE;
    float DEFAULT_RIPPLE_ALPHA = .6f;


    int MAX_GLOW_SHADOW_BLUR = 130;
    int MAX_GLOW_SHADOW_SIZE_MULTIPLIER = 4;
    long LINE_FADE_IN_OUT_DURATION = 70;
}
