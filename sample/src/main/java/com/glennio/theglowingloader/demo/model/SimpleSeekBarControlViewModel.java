package com.glennio.theglowingloader.demo.model;

public class SimpleSeekBarControlViewModel extends BaseControlViewModel {
    private int intMinimumValue;
    private int intMaximumValue;
    private float floatMinValue;
    private float floatMaxValue;
    private boolean intMode;
    private String title;
    private float currentValue;
    private String valueFormatString;

    public SimpleSeekBarControlViewModel(int id, String title, int minimumValue, int maximumValue, int currentValue, String valueFormatString) {
        super(id);
        this.title = title;
        this.intMinimumValue = minimumValue;
        this.intMaximumValue = maximumValue;
        this.intMode = true;
        this.currentValue = currentValue;
        this.valueFormatString = valueFormatString;
    }

    public SimpleSeekBarControlViewModel(int id, String title, float minimumValue, float maximumValue, float currentValue) {
        super(id);
        this.title = title;
        this.floatMinValue = minimumValue;
        this.floatMaxValue = maximumValue;
        this.intMode = false;
        this.currentValue = currentValue;
        this.valueFormatString = valueFormatString;
    }

    public int getIntMinimumValue() {
        return intMinimumValue;
    }

    public void setIntMinimumValue(int intMinimumValue) {
        this.intMinimumValue = intMinimumValue;
    }

    public int getIntMaximumValue() {
        return intMaximumValue;
    }

    public void setIntMaximumValue(int intMaximumValue) {
        this.intMaximumValue = intMaximumValue;
    }

    public float getFloatMinValue() {
        return floatMinValue;
    }

    public void setFloatMinValue(float floatMinValue) {
        this.floatMinValue = floatMinValue;
    }

    public float getFloatMaxValue() {
        return floatMaxValue;
    }

    public void setFloatMaxValue(float floatMaxValue) {
        this.floatMaxValue = floatMaxValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public String getValueFormatString() {
        return valueFormatString;
    }

    public void setValueFormatString(String valueFormatString) {
        this.valueFormatString = valueFormatString;
    }

    public boolean isIntMode() {
        return intMode;
    }
}
