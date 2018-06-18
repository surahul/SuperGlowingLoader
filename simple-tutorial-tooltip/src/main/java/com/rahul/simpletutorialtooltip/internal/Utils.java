package com.rahul.simpletutorialtooltip.internal;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * @author Rahul Verma on 13/01/17 <rv@videoder.com>
 */

public class Utils {

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    public static void setTint(@NonNull ImageView image, @ColorInt int color) {
        image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static boolean isColorLight(@ColorInt int color) {
        final double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.4;
    }


}
