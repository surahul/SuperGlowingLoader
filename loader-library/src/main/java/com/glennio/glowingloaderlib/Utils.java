package com.glennio.glowingloaderlib;

import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.util.DisplayMetrics;

import java.util.Random;

public class Utils {

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    @ColorInt
    public static int withAlpha(@ColorInt int baseColor, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public static float randomFloat(Random random, float min, float max) {
        if (max <= min)
            return min;
        return min + (random.nextFloat() * (max - min));
    }

    public static int randomInt(Random random, int min, int max) {
        return (int) (min + (random.nextFloat() * (max - min)));
    }

    public static Object randomObject(Random random, Object[] array) {
        if (array == null || array.length == 0)
            return null;
        return array[random.nextInt(array.length)];
    }

    public static int randomInt(Random random, int[] array) {
        if (array == null || array.length == 0)
            return 0;
        return array[random.nextInt(array.length)];
    }

    public static float randomFloat(Random random, float[] array) {
        if (array == null || array.length == 0)
            return 0;
        return array[random.nextInt(array.length)];
    }

    public static int nextNegativeOrPositive(Random random) {
        return random.nextBoolean() ? -1 : 1;
    }
}
