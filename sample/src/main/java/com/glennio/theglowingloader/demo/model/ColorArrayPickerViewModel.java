package com.glennio.theglowingloader.demo.model;

import java.util.ArrayList;
import java.util.List;

public class ColorArrayPickerViewModel extends BaseControlViewModel {
    private List<Integer> colors;
    private String title;
    private boolean showLeftIcon = true;
    private boolean showRightIcon = true;
    private int leftIconResource = -1;
    private int rightIconResource = -1;
    private boolean showValueText = true;

    public ColorArrayPickerViewModel(int id, String title, int[] colors) {
        super(id);
        this.colors = new ArrayList<>();
        if (colors != null) {
            for (int color : colors)
                addColor(color);
        }
        this.title = title;
    }

    public boolean isShowLeftIcon() {
        return showLeftIcon;
    }

    public boolean isShowRightIcon() {
        return showRightIcon;
    }

    public int getLeftIconResource() {
        return leftIconResource;
    }

    public int getRightIconResource() {
        return rightIconResource;
    }

    public ColorArrayPickerViewModel(int id, String title, int color) {
        this(id, title, new int[]{color});
    }

    public void addColor(int color) {
        this.colors.add(color);
    }


    public void removeColor() {
        if (colors.size() > 0)
            colors.remove(colors.size() - 1);
    }


    public int[] getColors() {
        int[] colors = new int[this.colors.size()];
        for (int i = 0; i < this.colors.size(); i++) {
            int color = this.colors.get(i);
            colors[i] = color;
        }
        return colors;
    }

    public String getTitle() {
        return title;
    }

    public void setShowLeftIcon(boolean showLeftIcon) {
        this.showLeftIcon = showLeftIcon;
    }

    public void setShowRightIcon(boolean showRightIcon) {
        this.showRightIcon = showRightIcon;
    }

    public boolean isShowValueText() {
        return showValueText;
    }

    public void setLeftIconResource(int leftIconResource) {
        this.leftIconResource = leftIconResource;
    }

    public void setRightIconResource(int rightIconResource) {
        this.rightIconResource = rightIconResource;
    }

    public void setShowValueText(boolean showValueText) {
        this.showValueText = showValueText;
    }

    public void setColors(int[] colors) {
        if (colors != null) {
            this.colors.clear();
            for (int color : colors)
                addColor(color);
        }
    }
}
