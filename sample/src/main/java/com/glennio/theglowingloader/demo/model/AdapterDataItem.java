package com.glennio.theglowingloader.demo.model;

import android.support.annotation.IntDef;

public class AdapterDataItem {

    public static final int TYPE_SIMPLE_SEEK_BAR_CONTROL = 1;
    public static final int TYPE_MIN_MAX_SEEK_BAR_CONTROL = 2;
    public static final int TYPE_LINE_MANAGER_CONTROL = 3;
    public static final int TYPE_CHECK_BOX_CONTROL = 4;
    public static final int TYPE_COLOR_ARRAY_PICKER = 5;


    @IntDef({
            TYPE_SIMPLE_SEEK_BAR_CONTROL,
            TYPE_MIN_MAX_SEEK_BAR_CONTROL,
            TYPE_LINE_MANAGER_CONTROL,
            TYPE_CHECK_BOX_CONTROL,
            TYPE_COLOR_ARRAY_PICKER
    })


    public @interface Type {
    }

    @Type
    private int type;

    private SimpleSeekBarControlViewModel seekBarControlViewModel;
    private MinMaxSeekBarViewModel minMaxSeekBarViewModel;
    private LineManagerControlViewModel lineManagerControlViewModel;
    private CheckBoxControlViewModel checkBoxControlViewModel;
    private ColorArrayPickerViewModel colorArrayPickerViewModel;


    public AdapterDataItem(SimpleSeekBarControlViewModel seekBarControlViewModel) {
        this.seekBarControlViewModel = seekBarControlViewModel;
        this.type = TYPE_SIMPLE_SEEK_BAR_CONTROL;
    }

    public AdapterDataItem(MinMaxSeekBarViewModel minMaxSeekBarViewModel) {
        this.minMaxSeekBarViewModel = minMaxSeekBarViewModel;
        this.type = TYPE_MIN_MAX_SEEK_BAR_CONTROL;
    }

    public AdapterDataItem(LineManagerControlViewModel lineManagerControlViewModel) {
        this.lineManagerControlViewModel = lineManagerControlViewModel;
        this.type = TYPE_LINE_MANAGER_CONTROL;
    }

    public AdapterDataItem(ColorArrayPickerViewModel colorArrayPickerViewModel) {
        this.colorArrayPickerViewModel = colorArrayPickerViewModel;
        this.type = TYPE_COLOR_ARRAY_PICKER;
    }

    public AdapterDataItem(CheckBoxControlViewModel checkBoxControlViewModel) {
        this.checkBoxControlViewModel = checkBoxControlViewModel;
        this.type = TYPE_CHECK_BOX_CONTROL;
    }

    public int getType() {
        return type;
    }



    public SimpleSeekBarControlViewModel getSeekBarControlViewModel() {
        return seekBarControlViewModel;
    }

    public MinMaxSeekBarViewModel getMinMaxSeekBarViewModel() {
        return minMaxSeekBarViewModel;
    }

    public LineManagerControlViewModel getLineManagerControlViewModel() {
        return lineManagerControlViewModel;
    }

    public CheckBoxControlViewModel getCheckBoxControlViewModel() {
        return checkBoxControlViewModel;
    }

    public ColorArrayPickerViewModel getColorArrayPickerViewModel() {
        return colorArrayPickerViewModel;
    }
}
