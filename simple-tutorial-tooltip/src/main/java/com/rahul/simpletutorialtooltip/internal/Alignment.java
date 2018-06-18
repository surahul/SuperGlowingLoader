package com.rahul.simpletutorialtooltip.internal;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rahul Verma on 13/01/17 <rv@videoder.com>
 */

public class Alignment {


    public static final int START = 0;
    public static final int UP = 1;
    public static final int END = 2;
    public static final int DOWN = 3;

    @IntDef({START, END, UP, DOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TooltipAlignment {
    }

}
