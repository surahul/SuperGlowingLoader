package com.glennio.theglowingloader.demo.view.adapter.view_holders;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.demo.model.SimpleSeekBarControlViewModel;

import io.apptik.widget.MultiSlider;

public class SimpleSeekBarControlViewHolder extends RecyclerView.ViewHolder implements MultiSlider.OnTrackingChangeListener, MultiSlider.OnThumbValueChangeListener {


    public interface SimpleSeekBarControlViewCallback {
        void onSimpleRangeBarSeekChanged(int adapterPosition, float progress, boolean trackingTouch);
    }

    private SimpleSeekBarControlViewCallback viewCallback;
    private TextView titleTextView;
    private TextView valueTextView;
    private MultiSlider slider;
    private boolean trackingTouch;
    private SimpleSeekBarControlViewModel lastBindModel;


    public SimpleSeekBarControlViewHolder(View itemView, SimpleSeekBarControlViewCallback viewCallback) {
        super(itemView);
        this.viewCallback = viewCallback;
        this.titleTextView = itemView.findViewById(R.id.text_view);
        this.slider = itemView.findViewById(R.id.slider);
        this.slider.setOnThumbValueChangeListener(this);
        this.slider.setOnTrackingChangeListener(this);
        this.valueTextView = itemView.findViewById(R.id.value_tv);
    }

    @SuppressLint("DefaultLocale")
    public void bind(SimpleSeekBarControlViewModel seekBarControlViewModel) {
        this.lastBindModel = seekBarControlViewModel;
        this.titleTextView.setText(seekBarControlViewModel.getTitle());
        updateValueText();
        if (!trackingTouch) {
            slider.setOnTrackingChangeListener(null);
            slider.setOnThumbValueChangeListener(null);
            if (seekBarControlViewModel.isIntMode()) {
                this.slider.setMax(seekBarControlViewModel.getIntMaximumValue());
                this.slider.setMin(seekBarControlViewModel.getIntMinimumValue());
                this.slider.getThumb(0).setMax(seekBarControlViewModel.getIntMaximumValue());
                this.slider.getThumb(0).setMin(seekBarControlViewModel.getIntMinimumValue());
                this.slider.getThumb(0).setValue((int) seekBarControlViewModel.getCurrentValue());
            } else {
                this.slider.setMax((int) (seekBarControlViewModel.getFloatMaxValue() * 1000f));
                this.slider.setMin((int) (seekBarControlViewModel.getFloatMinValue() * 1000f));
                this.slider.getThumb(0).setMax((int) (seekBarControlViewModel.getFloatMaxValue() * 1000f));
                this.slider.getThumb(0).setMin((int) (seekBarControlViewModel.getFloatMinValue() * 1000f));
                this.slider.getThumb(0).setValue((int) (seekBarControlViewModel.getCurrentValue() * 1000f));
            }
            slider.setOnTrackingChangeListener(this);
            slider.setOnThumbValueChangeListener(this);


        }
    }

    private void updateValueText() {
        if (lastBindModel.isIntMode()) {
            String formatString = lastBindModel.getValueFormatString();
            int value = (int) lastBindModel.getCurrentValue();
            if (value <= 1 && formatString.endsWith("s"))
                formatString = formatString.substring(0, formatString.length() - 1);
            this.valueTextView.setText(String.format(formatString,String.valueOf(value)));
        } else {
            this.valueTextView.setText(String.format("%.2f", lastBindModel.getCurrentValue()));
        }

    }


    @Override
    public void onStartTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {
        trackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {
        trackingTouch = false;
    }

    @Override
    public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
        if (viewCallback != null && getAdapterPosition() >= 0)
            viewCallback.onSimpleRangeBarSeekChanged(getAdapterPosition(), value / (lastBindModel.isIntMode() ? 1 : 1000f), trackingTouch);
    }
}
