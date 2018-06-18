package com.glennio.theglowingloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class Utils {

    public static float getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    public static float getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    private static int[] getScreenSize(Context context) {
        int x, y, orientation = context.getResources().getConfiguration().orientation;
        WindowManager wm = ((WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else {
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else {
            x = display.getWidth();
            y = display.getHeight();
        }

        int width = x;
        int height = y;
        return new int[]{width, height};
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    public static float pxToDp(int px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static double pxToInches(double px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float mXDpi = metrics.xdpi;
        return px / mXDpi;
    }

    public static void removeGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        else
            view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }

    public static int[] rotateArray(int[] array, int rotation) {

        if (rotation > array.length)
            rotation = rotation % array.length;

        int[] result = new int[array.length];

        for (int i = 0; i < rotation; i++) {
            result[i] = array[array.length - rotation + i];
        }

        int j = 0;
        for (int i = rotation; i < array.length; i++) {
            result[i] = array[j];
            j++;
        }

        System.arraycopy(result, 0, array, 0, array.length);
        return result;
    }

    public static int screenWidthInDp() {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float density = metrics.density;
            int dpHeight = (int) (metrics.heightPixels / density);
            int dpWidth = (int) (metrics.widthPixels / density);
            return dpWidth;
        } catch (Exception ignored) {
        }
        return 0;
    }

}
