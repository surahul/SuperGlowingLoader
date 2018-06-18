package com.glennio.glowingloaderlib.drawing_helpers.splash;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;

import com.glennio.glowingloaderlib.Constants;
import com.glennio.glowingloaderlib.Utils;
import com.glennio.glowingloaderlib.drawing_helpers.AnimationEndCallback;
import com.glennio.glowingloaderlib.drawing_helpers.BaseDrawerCallback;
import com.glennio.glowingloaderlib.drawing_helpers.particle.Particle;
import com.glennio.glowingloaderlib.drawing_helpers.particle.ParticlePropertiesCallback;
import com.glennio.glowingloaderlib.drawing_helpers.ripple.RippleDrawer;
import com.glennio.glowingloaderlib.drawing_helpers.ripple.RipplePropertiesCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Splash {


    private RipplePropertiesCallback ripplePropertiesCallback;
    private ParticlePropertiesCallback particlePropertiesCallback;
    private BaseDrawerCallback baseDrawerCallback;
    private BlurMaskFilter particleBlurMaskFilter;

    private RippleDrawer smallRipple;
    private RippleDrawer bigRipple;
    private List<Particle> particles = new ArrayList<>();
    private Random random;


    public Splash(Random random, BaseDrawerCallback baseDrawerCallback, RipplePropertiesCallback ripplePropertiesCallback, ParticlePropertiesCallback particlePropertiesCallback) {
        this.random = random;
        this.baseDrawerCallback = baseDrawerCallback;
        this.particlePropertiesCallback = particlePropertiesCallback;
        this.ripplePropertiesCallback = ripplePropertiesCallback;
    }

    public void starRipple() {
        cancel();
        createRipplesAndStart();
        createParticlesAndStart();
    }

    private void createRipplesAndStart() {
        cancelRipples();
        bigRipple = new RippleDrawer(
                ripplePropertiesCallback.getRippleColor(),
                ripplePropertiesCallback.getRippleAlpha(),
                Utils.randomFloat(random, ripplePropertiesCallback.getRippleMinRadius(), ripplePropertiesCallback.getRippleMaxRadius()),
                Utils.randomFloat(random, ripplePropertiesCallback.getRippleMinStrokeWidth(), ripplePropertiesCallback.getRippleMaxStrokeWidth()),
                ripplePropertiesCallback.getRippleDuration(),
                ripplePropertiesCallback,
                baseDrawerCallback,
                rippleEndCallback
        );

        smallRipple = new RippleDrawer(
                ripplePropertiesCallback.getRippleColor(),
                .8f * ripplePropertiesCallback.getRippleAlpha(),
                .6f * Utils.randomFloat(random, ripplePropertiesCallback.getRippleMinRadius(), ripplePropertiesCallback.getRippleMaxRadius()),
                .4f * Utils.randomFloat(random, ripplePropertiesCallback.getRippleMinStrokeWidth(), ripplePropertiesCallback.getRippleMaxStrokeWidth()),
                (long) (.8f * ripplePropertiesCallback.getRippleDuration()),
                ripplePropertiesCallback,
                baseDrawerCallback,
                rippleEndCallback
        );


        bigRipple.start();
        smallRipple.start();
    }

    private void createParticlesAndStart() {
        cancelParticles();
        particles = new ArrayList<>();
        int particlesCount = Utils.randomInt(random, particlePropertiesCallback.getMinParticlesCount(), particlePropertiesCallback.getMaxParticlesCount());
        for (int i = 0; i < particlesCount; i++) {
            Particle particle = new Particle(
                    Utils.randomInt(random, particlePropertiesCallback.getParticleTypes()),
                    Utils.randomInt(random, particlePropertiesCallback.getParticleColors()),
                    Utils.randomFloat(random, particlePropertiesCallback.getMinParticleAlpha(), particlePropertiesCallback.getMaxParticleAlpha()),
                    Utils.randomFloat(random, particlePropertiesCallback.getMinParticleSize(), particlePropertiesCallback.getMaxParticleSize()),
                    Utils.nextNegativeOrPositive(random) * Utils.randomFloat(random, particlePropertiesCallback.getMinParticleRotation(), particlePropertiesCallback.getMaxParticleRotation()),
                    Utils.nextNegativeOrPositive(random) * Utils.randomFloat(random, particlePropertiesCallback.getMinParticleTranslation(), particlePropertiesCallback.getMaxParticleTranslation()),
                    Utils.nextNegativeOrPositive(random) * Utils.randomFloat(random, particlePropertiesCallback.getMinParticleTranslation(), particlePropertiesCallback.getMaxParticleTranslation()),
                    particlePropertiesCallback.getParticlesAnimationDuration(),
                    particlePropertiesCallback, baseDrawerCallback,
                    particleEndCallback
            );
            particle.start();
            particles.add(particle);
        }
    }

    private AnimationEndCallback particleEndCallback = new AnimationEndCallback() {
        @Override
        public void onAnimationEnd(Object o) {
            if (o != null && o instanceof Particle) {
                ((Particle) o).cancel();
                if (particles != null)
                    particles.remove(o);
            }
        }
    };

    private AnimationEndCallback rippleEndCallback = new AnimationEndCallback() {
        @Override
        public void onAnimationEnd(Object o) {
            if (o != null && o instanceof RippleDrawer) {
                ((RippleDrawer) o).cancel();
                if (smallRipple != null && o.equals(smallRipple))
                    smallRipple = null;
                else if (bigRipple != null && o.equals(bigRipple))
                    bigRipple = null;
            }
        }
    };


    public void draw(Canvas canvas, float[] point) {
        if (particleBlurMaskFilter == null) {
            float value = particlePropertiesCallback.getGlowShadowSpread() * Constants.MAX_GLOW_SHADOW_BLUR * .7f;
            if (value > 0)
                particleBlurMaskFilter = new BlurMaskFilter(value, BlurMaskFilter.Blur.NORMAL);
        } if (smallRipple != null)
            smallRipple.draw(canvas, point, particleBlurMaskFilter);
        if (bigRipple != null)
            bigRipple.draw(canvas, point, particleBlurMaskFilter);
        if (particles != null) {
            for (Particle p : particles)
                p.draw(canvas, point, random, particleBlurMaskFilter);
        }
    }

    public void cancel() {
        cancelRipples();
        cancelParticles();
    }

    private void cancelRipples() {
        if (smallRipple != null)
            smallRipple.cancel();
        if (bigRipple != null)
            bigRipple.cancel();
        smallRipple = null;
        bigRipple = null;
    }

    private void cancelParticles() {
        if (particles != null) {
            for (int i = 0; i < particles.size(); i++) {
                particles.get(i).cancel();
            }
            particles.clear();
        }
    }


    public boolean isPlaying() {
        return (bigRipple != null && bigRipple.isPlaying()) || (smallRipple != null && smallRipple.isPlaying());
    }

    public void invalidateBlurMasks() {
        this.particleBlurMaskFilter = null;
    }
}
