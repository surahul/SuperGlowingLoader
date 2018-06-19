package com.glennio.theglowingloader.demo.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.glennio.glowingloaderlib.Constants;
import com.glennio.glowingloaderlib.drawing_helpers.line.LineSpec;
import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.Utils;
import com.glennio.theglowingloader.demo.model.AdapterDataItem;
import com.glennio.theglowingloader.demo.model.CheckBoxControlViewModel;
import com.glennio.theglowingloader.demo.model.ColorArrayPickerViewModel;
import com.glennio.theglowingloader.demo.model.SimpleSeekBarControlViewModel;
import com.glennio.theglowingloader.demo.presenter.adapter.AdapterDataProvider;
import com.glennio.theglowingloader.demo.presenter.adapter.AdapterDataProviderImpl;
import com.glennio.theglowingloader.demo.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PresenterImpl implements Presenter {

    private static final int ID_SHOW_LINE_GLOW_SHADOW = 1;
    private static final int ID_SHOW_RIPPLE_GLOW_SHADOW = 6;
    private static final int ID_SHOW_PARTICLE_GLOW_SHADOW = 7;
    private static final int ID_GLOW_DX_VALUE = 2;
    private static final int ID_GLOW_DY_VALUE = 3;
    private static final int ID_GLOW_SHADOW_SIZE = 4;
    private static final int ID_GLOW_SHADOW_SPREAD = 5;
    private static final int ID_GLOW_SHADOW_ALPHA = 8;
    private static final int ID_ANIMATION_DURATION = 9;
    private static final int ID_ASPECT_RATIO = 10;
    private static final int ID_STROKE_WIDTH = 11;
    private static final int ID_PARTICLE_COLORS = 12;
    private static final int ID_RIPPLE_COLOR = 13;
    private static final int ID_PARTICLES_ALPHA = 14;
    private static final int ID_PARTICLES_SIZE = 15;
    private static final int ID_PARTICLES_COUNT = 16;
    private static final int ID_RIPPLE_ALPHA = 17;
    private static final int ID_RIPPLE_STROKE_WIDTH = 18;
    private static final int ID_RIPPLE_SIZE = 19;
    private static final int ID_RIPPLE_DURATION = 20;
    private static final int ID_PARTICLE_TRANSLATION = 21;
    private static final int ID_PARTICLE_ROTATION = 22;
    private static final int ID_LINES_CUSTOMIZATION = 22;
    private static final int ID_BACKGROUND_COLOR = 23;


    private int[] colorsForRandomPicking = new int[]{
            0XFFEF5350,
            0XFFEC407A,
            0XFFAB47BC,
            Color.WHITE,
            0XFF9575CD,
            0XFF7986CB,
            0XFF7986CB,
            0XFF29B6F6,
            Color.WHITE,
            0XFF26C6DA,
            0XFF26A69A,
            0XFF81C784,
            0XFFAED581,
            0XFFDCE775,
            Color.WHITE,
            0XFFFFF176,
            0XFFFFCA28,
            0XFF8D6E63,
            Color.WHITE,
            0XFFBDBDBD,
            0XFF90A4AE,
            0XFF7e3ff2,

    };

    private int[] colorForBackground = new int[]{
            Color.WHITE,
            0XFFFFEBEE,
            Color.BLACK,
            Color.WHITE,
            0XFF3E2723,
            0XFFF3E5F5,
            0XFFa091d,
            0XFF212121,
            0XFFa091d,
            0XFFE0F2F1,
            0XFFa091d,
            0XFF263238,
    };

    private float[] lineLengths = new float[]{
            com.glennio.glowingloaderlib.Utils.dpToPx(150),
            com.glennio.glowingloaderlib.Utils.dpToPx(70),
            com.glennio.glowingloaderlib.Utils.dpToPx(60),
            com.glennio.glowingloaderlib.Utils.dpToPx(50),
            com.glennio.glowingloaderlib.Utils.dpToPx(40)
    };
    private long[] lineLDelays = new long[]{
            200,
            650,
            850,
            1000,
            1200
    };


    @Nullable
    private ViewHelper viewHelper;
    private AdapterDataProvider dataProvider;
    private Handler handler;
    private Random random;

    public PresenterImpl(Context context) {
        dataProvider = new AdapterDataProviderImpl();
        this.handler = new Handler(Looper.getMainLooper());
        this.random = new Random();
        createItems(context);
    }

    @Override
    public void setViewHelper(@Nullable ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
        if (viewHelper != null) {
            dataProvider.setViewHelper(viewHelper.getAdapterViewHelper());
        } else {
            dataProvider.setViewHelper(null);
        }
    }

    @Override
    public AdapterDataProvider getAdapterDataProvider() {
        return dataProvider;
    }

    private void createItems(Context context) {
        List<AdapterDataItem> adapterDataItems = new ArrayList<>();


        int[] defaultLineColors = new int[Constants.DEFAULT_LINE_SPECS.length];
        for (int i = 0; i < Constants.DEFAULT_LINE_SPECS.length; i++) {
            defaultLineColors[i] = Constants.DEFAULT_LINE_SPECS[i].getColor();
        }


        // line controls
        adapterDataItems.add(new AdapterDataItem(new ColorArrayPickerViewModel(ID_LINES_CUSTOMIZATION, getString(context, R.string.lines_customizations), defaultLineColors)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_STROKE_WIDTH, getString(context, R.string.stroke_width), Utils.dpToPx(3f), Utils.dpToPx(30f), Constants.DEFAULT_LINE_STROKE_WIDTH, "%s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_ASPECT_RATIO, getString(context, R.string.aspect_ratio), .25f, 6f, Constants.DEFAULT_ASPECT)));

        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_GLOW_SHADOW_SIZE, getString(context, R.string.glow_shadow_size), 0f, 1f, Constants.DEFAULT_GLOW_SHADOW_SIZE)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_GLOW_SHADOW_SPREAD, getString(context, R.string.glow_shadow_spread), 0f, 1f, Constants.DEFAULT_GLOW_SHADOW_SPREAD)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_GLOW_SHADOW_ALPHA, getString(context, R.string.glow_shadow_opacity), 0f, .7f, Constants.DEFAULT_GLOW_SHADOW_ALPHA)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_GLOW_DY_VALUE, getString(context, R.string.glow_shadow_delta_y), Utils.dpToPx(0), Utils.dpToPx(100), Constants.DEFAULT_GLOW_SHADOW_DY, "%s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_GLOW_DX_VALUE, getString(context, R.string.glow_shadow_delta_x), Utils.dpToPx(0), Utils.dpToPx(100), Constants.DEFAULT_GLOW_SHADOW_DX, "%s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new CheckBoxControlViewModel(ID_SHOW_LINE_GLOW_SHADOW, getString(context, R.string.show_line_glow_shadow), Constants.DEFAULT_SHOW_LINE_GLOW_SHADOW)));
        adapterDataItems.add(new AdapterDataItem(new CheckBoxControlViewModel(ID_SHOW_RIPPLE_GLOW_SHADOW, getString(context, R.string.show_ripple_glow_shadow), Constants.DEFAULT_SHOW_RIPPLE_GLOW_SHADOW)));
        adapterDataItems.add(new AdapterDataItem(new CheckBoxControlViewModel(ID_SHOW_PARTICLE_GLOW_SHADOW, getString(context, R.string.show_particle_glow_shadow), Constants.DEFAULT_SHOW_PARTICLE_GLOW_SHADOW)));

        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_ANIMATION_DURATION, getString(context, R.string.animation_duration), 800, 2000, (int) Constants.DEFAULT_DURATION, "%s milli sec")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_RIPPLE_DURATION, getString(context, R.string.ripple_duration), 300, 2000, (int) Constants.DEFAULT_RIPPLE_DURATION, "%s milli sec")));


        adapterDataItems.add(new AdapterDataItem(new ColorArrayPickerViewModel(ID_PARTICLE_COLORS, getString(context, R.string.particle_colors), Constants.DEFAULT_PARTICLE_COLORS)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_PARTICLES_SIZE, getString(context, R.string.particles_size), Utils.dpToPx(8), Utils.dpToPx(60), Constants.DEFAULT_MAX_PARTICLE_SIZE, "~ %s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_PARTICLES_ALPHA, getString(context, R.string.particles_opacity), .2f, 1f, Constants.DEFAULT_MAX_PARTICLE_ALPHA)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_PARTICLES_COUNT, getString(context, R.string.particles_count), 4, 10, Constants.DEFAULT_MAX_PARTICLE_COUNT, "~ %s Particles")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_PARTICLE_TRANSLATION, getString(context, R.string.particles_scattering_distance), Utils.dpToPx(20), Utils.dpToPx(200), Constants.DEFAULT_MAX_PARTICLE_TRANSLATION, "~ %s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_PARTICLE_ROTATION, getString(context, R.string.particles_rotation), 50, 2100, Constants.DEFAULT_MAX_PARTICLE_ROTATION, "~ %s Degrees")));


        ColorArrayPickerViewModel refreshRippleColorViewModel = new ColorArrayPickerViewModel(ID_RIPPLE_COLOR, getString(context, R.string.ripple_color), Constants.DEFAULT_RIPPLE_COLOR);
        refreshRippleColorViewModel.setShowRightIcon(false);
        refreshRippleColorViewModel.setLeftIconResource(R.drawable.ic_refresh);
        refreshRippleColorViewModel.setShowValueText(false);
        adapterDataItems.add(new AdapterDataItem(refreshRippleColorViewModel));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_RIPPLE_ALPHA, getString(context, R.string.ripple_opacity), .2f, 1f, Constants.DEFAULT_RIPPLE_ALPHA)));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_RIPPLE_SIZE, getString(context, R.string.ripple_size), Utils.dpToPx(30), Utils.dpToPx(120), Constants.DEFAULT_MAX_RIPPLE_RADIUS, "~ %s Pixels")));
        adapterDataItems.add(new AdapterDataItem(new SimpleSeekBarControlViewModel(ID_RIPPLE_STROKE_WIDTH, getString(context, R.string.ripple_stroke_width), Utils.dpToPx(2), Utils.dpToPx(26), Constants.DEFAULT_MAX_RIPPLE_STROKE_WIDTH, "~ %s Pixels")));


        ColorArrayPickerViewModel refreshBackgroundColor = new ColorArrayPickerViewModel(ID_BACKGROUND_COLOR, getString(context, R.string.backgroundColor), context.getResources().getColor(R.color.colorPrimary));
        refreshBackgroundColor.setShowRightIcon(false);
        refreshBackgroundColor.setLeftIconResource(R.drawable.ic_refresh);
        refreshBackgroundColor.setShowValueText(false);
        adapterDataItems.add(new AdapterDataItem(refreshBackgroundColor));


        dataProvider.addItems(adapterDataItems);


    }

    private String getString(Context context, @StringRes int id) {
        if (context != null)
            return context.getString(id);
        return "";
    }

    @Override
    public void onCheckControlChanged(int adapterPosition) {
        AdapterDataItem adapterDataItem = dataProvider.getItem(adapterPosition);
        switch (adapterDataItem.getCheckBoxControlViewModel().getId()) {
            case ID_SHOW_LINE_GLOW_SHADOW:
                adapterDataItem.getCheckBoxControlViewModel().setChecked(!adapterDataItem.getCheckBoxControlViewModel().isChecked());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setShowLineGlowShadow(adapterDataItem.getCheckBoxControlViewModel().isChecked());
                break;
            case ID_SHOW_RIPPLE_GLOW_SHADOW:
                adapterDataItem.getCheckBoxControlViewModel().setChecked(!adapterDataItem.getCheckBoxControlViewModel().isChecked());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setShowRippleGlowShadow(adapterDataItem.getCheckBoxControlViewModel().isChecked());
                break;
            case ID_SHOW_PARTICLE_GLOW_SHADOW:
                adapterDataItem.getCheckBoxControlViewModel().setChecked(!adapterDataItem.getCheckBoxControlViewModel().isChecked());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setShowParticleGlowShadow(adapterDataItem.getCheckBoxControlViewModel().isChecked());
                break;
        }
        dataProvider.setItem(adapterPosition, adapterDataItem);
    }

    @Override
    public void onSimpleRangeBarSeekChanged(int adapterPosition, float progress, boolean trackingTouch) {
        AdapterDataItem adapterDataItem = dataProvider.getItem(adapterPosition);
        switch (adapterDataItem.getSeekBarControlViewModel().getId()) {
            case ID_GLOW_DX_VALUE:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setGlowShadowDx(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_GLOW_DY_VALUE:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setGlowShadowDy(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_GLOW_SHADOW_SIZE:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setGlowShadowSize(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_GLOW_SHADOW_SPREAD:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setGlowShadowSpread(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_GLOW_SHADOW_ALPHA:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setGlowShadowAlpha(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_PARTICLES_ALPHA:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticlesAlphaMinMax(.7f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_ASPECT_RATIO:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setAspectRatio(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_STROKE_WIDTH:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setLineStrokeWidth(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_ANIMATION_DURATION:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null) {
                    handler.removeCallbacks(startAnimationRunnable);
                    handler.postDelayed(startAnimationRunnable, 300);
                    viewHelper.getGlowLoaderView().setDuration((long) adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                }
                break;
            case ID_PARTICLES_SIZE:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleSizeMinMax(.77f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_PARTICLES_COUNT:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleCountMinMax((int) (.5f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue()), ((int) adapterDataItem.getSeekBarControlViewModel().getCurrentValue()));
                break;
            case ID_RIPPLE_ALPHA:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setRippleAlpha(adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_RIPPLE_STROKE_WIDTH:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setRippleStrokeMinMax(.77f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_RIPPLE_SIZE:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setRippleSizeMinMax(.77f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;

            case ID_RIPPLE_DURATION:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setRippleDuration((long) adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;
            case ID_PARTICLE_TRANSLATION:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleTranslationMinMax(.77f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;

            case ID_PARTICLE_ROTATION:
                adapterDataItem.getSeekBarControlViewModel().setCurrentValue(progress);
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleRotationMinMax(.60f * adapterDataItem.getSeekBarControlViewModel().getCurrentValue(), adapterDataItem.getSeekBarControlViewModel().getCurrentValue());
                break;

        }

        dataProvider.setItem(adapterPosition, adapterDataItem);
    }

    @Override
    public void onAddColorClicked(int adapterPosition) {
        AdapterDataItem adapterDataItem = dataProvider.getItem(adapterPosition);
        switch (adapterDataItem.getColorArrayPickerViewModel().getId()) {
            case ID_PARTICLE_COLORS:
                adapterDataItem.getColorArrayPickerViewModel().addColor(getNextColor());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleColors(adapterDataItem.getColorArrayPickerViewModel().getColors());
                break;
            case ID_RIPPLE_COLOR:
                adapterDataItem.getColorArrayPickerViewModel().removeColor();
                adapterDataItem.getColorArrayPickerViewModel().addColor(getNextColor());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setRippleColor(adapterDataItem.getColorArrayPickerViewModel().getColors()[0]);
                break;
            case ID_BACKGROUND_COLOR:
                adapterDataItem.getColorArrayPickerViewModel().removeColor();
                adapterDataItem.getColorArrayPickerViewModel().addColor(getNextColor(colorForBackground));
                if (viewHelper != null)
                    viewHelper.setBackgroundColor(adapterDataItem.getColorArrayPickerViewModel().getColors()[0]);
                break;
            case ID_LINES_CUSTOMIZATION:
                int nextColor = getNextColor();
                ColorArrayPickerViewModel colorArrayPickerViewModel = adapterDataItem.getColorArrayPickerViewModel();
                int[] currentColors = colorArrayPickerViewModel.getColors();
                if (currentColors.length >= lineLengths.length) {
                    currentColors = Utils.rotateArray(currentColors, 1);
                    currentColors[0] = nextColor;
                    colorArrayPickerViewModel.setColors(currentColors);
                } else {
                    colorArrayPickerViewModel.addColor(nextColor);
                }
                if (viewHelper != null) {
                    handler.removeCallbacks(startAnimationRunnable);
                    handler.postDelayed(startAnimationRunnable, 300);
                }
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setLineSpecs(createLineSpecs(colorArrayPickerViewModel.getColors()));
                break;
        }
        dataProvider.setItem(adapterPosition, adapterDataItem);
    }

    private LineSpec[] createLineSpecs(int[] colors) {
        LineSpec[] lineSpecs = new LineSpec[colors.length];
        for (int i = 0; i < colors.length; i++) {
            lineSpecs[i] = new LineSpec(
                    0,
                    (int) lineLengths[i],
                    colors[i],
                    (int) lineLDelays[i],
                    i == 0 ? LineSpec.DEFAULT_INTERPOLATOR_LONG_LINES : LineSpec.DEFAULT_INTERPOLATOR_SHORT_LINES
            );
        }
        return lineSpecs;
    }

    private int getNextColor() {
        return getNextColor(colorsForRandomPicking);
    }

    private int getNextColor(int[] colors) {
        return com.glennio.glowingloaderlib.Utils.randomInt(random, colors);
    }

    @Override
    public void onRemoveColorClicked(int adapterPosition) {
        AdapterDataItem adapterDataItem = dataProvider.getItem(adapterPosition);
        switch (adapterDataItem.getColorArrayPickerViewModel().getId()) {
            case ID_PARTICLE_COLORS:
                adapterDataItem.getColorArrayPickerViewModel().removeColor();
                if (adapterDataItem.getColorArrayPickerViewModel().getColors().length == 0)
                    adapterDataItem.getColorArrayPickerViewModel().addColor(getNextColor());
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setParticleColors(adapterDataItem.getColorArrayPickerViewModel().getColors());
                break;
            case ID_LINES_CUSTOMIZATION:
                adapterDataItem.getColorArrayPickerViewModel().removeColor();
                if (adapterDataItem.getColorArrayPickerViewModel().getColors().length == 0)
                    adapterDataItem.getColorArrayPickerViewModel().addColor(getNextColor());
                if (viewHelper != null) {
                    handler.removeCallbacks(startAnimationRunnable);
                    handler.postDelayed(startAnimationRunnable, 300);
                }
                if (viewHelper != null)
                    viewHelper.getGlowLoaderView().setLineSpecs(createLineSpecs(adapterDataItem.getColorArrayPickerViewModel().getColors()));
                break;
        }
        dataProvider.setItem(adapterPosition, adapterDataItem);

    }

    private Runnable startAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewHelper != null)
                viewHelper.getGlowLoaderView().createAndStart();

        }
    };
}
