package com.rahul.simpletutorialtooltip.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Rahul Verma on 13/01/17 <rv@videoder.com>
 */

public class TooltipView extends FrameLayout {

    private TooltipBackgroundShape tooltipBackgroundShape;


    public TooltipView(Context context, TooltipBackgroundShape tooltipBackgroundShape) {
        super(context);
        this.tooltipBackgroundShape = tooltipBackgroundShape;
        init();
    }

    public TooltipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TooltipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TooltipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (tooltipBackgroundShape == null)
            tooltipBackgroundShape = new TooltipBackgroundShape.Builder().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground(tooltipBackgroundShape);
        else
            setBackgroundDrawable(tooltipBackgroundShape);
    }


    public TooltipBackgroundShape getTooltipBackgroundShape() {
        return tooltipBackgroundShape;
    }


    public void resetDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
            setBackground(tooltipBackgroundShape);
        } else {
            setBackgroundDrawable(null);
            setBackgroundDrawable(tooltipBackgroundShape);
        }
    }
}
