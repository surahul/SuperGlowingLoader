package com.glennio.glowingloaderlib;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.glennio.glowingloaderlib.drawing_helpers.BaseDrawerCallback;
import com.glennio.glowingloaderlib.drawing_helpers.line.LineDrawer;
import com.glennio.glowingloaderlib.drawing_helpers.line.LinePropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.line.LineSpec;
import com.glennio.glowingloaderlib.drawing_helpers.particle.Particle;
import com.glennio.glowingloaderlib.drawing_helpers.particle.ParticlePropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.ripple.RipplePropertiesCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlowingLoaderView extends View implements BaseDrawerCallback, LinePropertiesCallback, RipplePropertiesCallback, ParticlePropertiesCallback {


    // properties
    private LineSpec[] lineSpecs;
    private boolean showLineGlowShadow;
    private boolean showRippleGlowShadow;
    private boolean showParticleGlowShadow;
    private float glowShadowDx;
    private float glowShadowDy;
    private float glowShadowSize;
    private float glowShadowSpread;
    private float glowShadowAlpha;
    private long duration;
    private float aspect;
    private float lineStrokeWidth;

    private int[] particleColors;
    private float minParticleSize;
    private float maxParticleSize;
    private float minParticleAlpha;
    private float maxParticleAlpha;
    @Particle.Type
    private int[] particleTypes;

    private int minParticleCount;
    private int maxParticleCount;
    private float minParticleRotation;
    private float maxParticleRotation;
    private float minParticleTranslation;
    private float maxParticleTranslation;


    private long rippleDuration;
    private float minRippleSize;
    private float maxRippleSize;
    private float minRippleStrokeWidth;
    private float maxRippleStrokeWidth;
    private float rippleAlpha;
    private int rippleColor;


    private Random random;
    private List<LineDrawer> lineDrawers;


    public GlowingLoaderView(Context context) {
        super(context);
        init(null);
    }

    public GlowingLoaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GlowingLoaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GlowingLoaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(@Nullable AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        this.random = new Random();

        this.lineSpecs = Constants.DEFAULT_LINE_SPECS;
        this.showLineGlowShadow = Constants.DEFAULT_SHOW_LINE_GLOW_SHADOW;
        this.showRippleGlowShadow = Constants.DEFAULT_SHOW_RIPPLE_GLOW_SHADOW;
        this.showParticleGlowShadow = Constants.DEFAULT_SHOW_PARTICLE_GLOW_SHADOW;
        this.glowShadowDx = Constants.DEFAULT_GLOW_SHADOW_DX;
        this.glowShadowDy = Constants.DEFAULT_GLOW_SHADOW_DY;
        this.glowShadowSize = Constants.DEFAULT_GLOW_SHADOW_SIZE;
        this.glowShadowSpread = Constants.DEFAULT_GLOW_SHADOW_SPREAD;
        this.glowShadowAlpha = Constants.DEFAULT_GLOW_SHADOW_ALPHA;
        this.duration = Constants.DEFAULT_DURATION;
        this.aspect = Constants.DEFAULT_ASPECT;
        this.lineStrokeWidth = Constants.DEFAULT_LINE_STROKE_WIDTH;
        this.particleColors = Constants.DEFAULT_PARTICLE_COLORS;
        this.minParticleSize = Constants.DEFAULT_MIN_PARTICLE_SIZE;
        this.maxParticleSize = Constants.DEFAULT_MAX_PARTICLE_SIZE;
        this.minParticleAlpha = Constants.DEFAULT_MIN_PARTICLE_ALPHA;
        this.maxParticleAlpha = Constants.DEFAULT_MAX_PARTICLE_ALPHA;
        this.particleTypes = Constants.DEFAULT_PARTICLE_TYPES;
        this.minParticleCount = Constants.DEFAULT_MIN_PARTICLE_COUNT;
        this.maxParticleCount = Constants.DEFAULT_MAX_PARTICLE_COUNT;
        this.minParticleRotation = Constants.DEFAULT_MIN_PARTICLE_ROTATION;
        this.maxParticleRotation = Constants.DEFAULT_MAX_PARTICLE_ROTATION;
        this.minParticleTranslation = Constants.DEFAULT_MIN_PARTICLE_TRANSLATION;
        this.maxParticleTranslation = Constants.DEFAULT_MAX_PARTICLE_TRANSLATION;
        this.rippleDuration = Constants.DEFAULT_RIPPLE_DURATION;
        this.minRippleSize = Constants.DEFAULT_MIN_RIPPLE_RADIUS;
        this.maxRippleSize = Constants.DEFAULT_MAX_RIPPLE_RADIUS;
        this.minRippleStrokeWidth = Constants.DEFAULT_MIN_RIPPLE_STROKE_WIDTH;
        this.maxRippleStrokeWidth = Constants.DEFAULT_MAX_RIPPLE_STROKE_WIDTH;
        this.rippleAlpha = Constants.DEFAULT_RIPPLE_ALPHA;
        this.rippleColor = Constants.DEFAULT_RIPPLE_COLOR;

        createAndStart();
    }

    public void createAndStart() {
        checkForDurationMinValueEqualToLongestSpecDelay();
        createDrawers();
        start();
    }

    private void checkForDurationMinValueEqualToLongestSpecDelay() {
        if(lineSpecs!=null){
            for (LineSpec lineSpec : lineSpecs) {
                if (lineSpec.getStartDelay() >= duration) {
                    duration = lineSpec.getStartDelay() + 100;
                }
            }
        }
    }

    private void createDrawers() {
        cancel();
        lineDrawers = new ArrayList<>();
        boolean splashLineAdded = false;
        for (LineSpec lineSpec : lineSpecs) {
            LineDrawer lineDrawer = new LineDrawer(
                    lineSpec,
                    !splashLineAdded,
                    random,
                    this,
                    this,
                    this,
                    this
            );
            splashLineAdded = true;
            lineDrawers.add(lineDrawer);
        }
    }

    public void start() {
        for (LineDrawer lineDrawer : lineDrawers) {
            lineDrawer.start();
        }
    }

    public void cancel() {
        if (lineDrawers != null) {
            for (LineDrawer lineDrawer : lineDrawers)
                lineDrawer.cancel();
            lineDrawers.clear();
        }
        lineDrawers = null;
        invalidateBlurMasks();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineDrawers != null) {
            for (LineDrawer lineDrawer : lineDrawers) {
                lineDrawer.draw(canvas, this);
            }
        }
    }


    @Override
    public long getTotalDuration() {
        return duration;
    }

    @Override
    public int getMinParticlesCount() {
        return minParticleCount;
    }

    @Override
    public int getMaxParticlesCount() {
        return maxParticleCount;
    }

    @Override
    public int[] getParticleTypes() {
        return particleTypes;
    }

    @Override
    public int[] getParticleColors() {
        return particleColors;
    }

    @Override
    public float getMaxParticleAlpha() {
        return maxParticleAlpha;
    }

    @Override
    public float getMinParticleAlpha() {
        return minParticleAlpha;
    }

    @Override
    public float getMinParticleSize() {
        return minParticleSize;
    }

    @Override
    public float getMaxParticleSize() {
        return maxParticleSize;
    }

    @Override
    public float getMinParticleRotation() {
        return minParticleRotation;
    }

    @Override
    public float getMaxParticleRotation() {
        return maxParticleRotation;
    }

    @Override
    public float getMinParticleTranslation() {
        return minParticleTranslation;
    }

    @Override
    public float getMaxParticleTranslation() {
        return maxParticleTranslation;
    }

    @Override
    public long getParticlesAnimationDuration() {
        return rippleDuration;
    }

    @Override
    public int getRippleColor() {
        return rippleColor;
    }

    @Override
    public float getRippleAlpha() {
        return rippleAlpha;
    }

    @Override
    public long getRippleDuration() {
        return rippleDuration;
    }

    @Override
    public float getRippleMinRadius() {
        return minRippleSize;
    }

    @Override
    public float getRippleMaxRadius() {
        return maxRippleSize;
    }

    @Override
    public float getRippleMinStrokeWidth() {
        return minRippleStrokeWidth;
    }

    @Override
    public float getRippleMaxStrokeWidth() {
        return maxRippleStrokeWidth;
    }

    @Override
    public boolean showLineGlowShadow() {
        return showLineGlowShadow;
    }

    @Override
    public boolean showRippleGlowShadow() {
        return showRippleGlowShadow;
    }

    @Override
    public boolean showParticleGlowShadow() {
        return showParticleGlowShadow;
    }

    @Override
    public float getGlowShadowDx() {
        return glowShadowDx;
    }

    @Override
    public float getGlowShadowDy() {
        return glowShadowDy;
    }

    @Override
    public float getGlowShadowSize() {
        return glowShadowSize;
    }

    @Override
    public float getGlowShadowSpread() {
        return glowShadowSpread;
    }

    @Override
    public float getGlowShadowAlpha() {
        return glowShadowAlpha;
    }

    @Override
    public float getLineStrokeWidth() {
        return lineStrokeWidth;
    }

    @Override
    public float getLineHeight() {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        float accessableWidth = viewWidth - (maxParticleTranslation + spaceRequiredByBlur(true));
        float accessableHeight = viewHeight - (maxParticleTranslation + spaceRequiredByBlur(false));
        if (accessableHeight > 0 && accessableWidth > 0) {
            if (accessableWidth / accessableHeight >= aspect) {
                // landscape kinda view
                return accessableHeight;
            } else {
                return accessableWidth / aspect;
            }
        }
        return 0;
    }

    @Override
    public float getLineWidth() {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        float accessableWidth = viewWidth - (maxParticleTranslation + spaceRequiredByBlur(true));
        float accessableHeight = viewHeight - (maxParticleTranslation + spaceRequiredByBlur(false));
        if (accessableHeight > 0 && accessableWidth > 0) {
            if (accessableWidth / accessableHeight >= aspect) {
                // landscape kinda view
                return accessableHeight * aspect;
            } else {
                return accessableWidth;
            }
        }
        return 0;
    }

    private float spaceRequiredByBlur(boolean forX) {
        if (showLineGlowShadow) {
            return (forX ? glowShadowDx : glowShadowDy);
        }
        return 0;
    }

    public void setShowLineGlowShadow(boolean showLineGlowShadow) {
        this.showLineGlowShadow = showLineGlowShadow;
        invalidate();
    }

    public void setShowRippleGlowShadow(boolean showRippleGlowShadow) {
        this.showRippleGlowShadow = showRippleGlowShadow;
        invalidate();
    }

    public void setShowParticleGlowShadow(boolean showParticleGlowShadow) {
        this.showParticleGlowShadow = showParticleGlowShadow;
        invalidate();
    }

    public void setGlowShadowDy(float glowShadowDy) {
        this.glowShadowDy = glowShadowDy;
        invalidate();
    }

    public void setGlowShadowDx(float glowShadowDx) {
        this.glowShadowDx = glowShadowDx;
        invalidate();
    }

    public void setGlowShadowSize(float glowShadowSize) {
        this.glowShadowSize = glowShadowSize;
        invalidateBlurMasks();
        invalidate();
    }

    public void setGlowShadowSpread(float glowShadowSpread) {
        this.glowShadowSpread = glowShadowSpread;
        invalidateBlurMasks();
        invalidate();
    }

    public void setGlowShadowAlpha(float glowShadowAlpha) {
        this.glowShadowAlpha = glowShadowAlpha;
        invalidate();
    }


    public void setDuration(long duration) {
        cancel();
        this.duration = duration;
        createAndStart();
    }

    private void invalidateBlurMasks() {
        if (lineDrawers != null) {
            for (LineDrawer lineDrawer : lineDrawers) {
                lineDrawer.invalidateBlurMasks();
            }
        }
    }


    public void setAspectRatio(float aspectRatio) {
        this.aspect = aspectRatio;
        invalidate();
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
        invalidate();
    }

    public void setParticleColors(int[] particleColors) {
        this.particleColors = particleColors;
        invalidate();
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        invalidate();
    }

    public void setParticlesAlphaMinMax(float min, float max) {
        this.minParticleAlpha = min;
        this.maxParticleAlpha = max;
        invalidate();
    }

    public void setParticleSizeMinMax(float min, float max) {
        this.minParticleSize = min;
        this.maxParticleSize = max;
        invalidate();
    }

    public void setParticleCountMinMax(int min, int max) {
        this.minParticleCount = min;
        this.maxParticleCount = max;
        invalidate();
    }

    public void setRippleAlpha(float rippleAlpha) {
        this.rippleAlpha = rippleAlpha;
        invalidate();
    }

    public void setRippleStrokeMinMax(float min, float max) {
        this.minRippleStrokeWidth = min;
        this.maxRippleStrokeWidth = max;
        invalidate();
    }

    public void setRippleSizeMinMax(float min, float max) {
        this.minRippleSize = min;
        this.maxRippleSize = max;
        invalidate();
    }

    public void setRippleDuration(long rippleDuration) {
        this.rippleDuration = rippleDuration;
        invalidate();
    }

    public void setParticleTranslationMinMax(float min, float max) {
        this.minParticleTranslation = min;
        this.maxParticleTranslation = max;
        invalidate();

    }

    public void setParticleRotationMinMax(float min, float max) {
        this.minParticleRotation = min;
        this.maxParticleRotation = max;
        invalidate();

    }

    public void setLineSpecs(LineSpec[] lineSpecs) {
        cancel();
        this.lineSpecs = lineSpecs;
        createAndStart();
    }

    public void setParticleTypes(int[] particleTypes) {
        this.particleTypes = particleTypes;
        invalidateBlurMasks();
    }

}
