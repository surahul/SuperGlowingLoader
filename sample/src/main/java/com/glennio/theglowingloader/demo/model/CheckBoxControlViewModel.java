package com.glennio.theglowingloader.demo.model;

public class CheckBoxControlViewModel extends BaseControlViewModel {

    private boolean checked;
    private String text;

    public CheckBoxControlViewModel(int id, String text, boolean checked) {
        super(id);
        this.checked = checked;
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
