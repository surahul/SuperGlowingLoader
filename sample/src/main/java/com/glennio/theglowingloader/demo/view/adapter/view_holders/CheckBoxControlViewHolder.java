package com.glennio.theglowingloader.demo.view.adapter.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.demo.model.CheckBoxControlViewModel;

public class CheckBoxControlViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    public interface CheckBoxControlViewCallback {

        void onCheckControlChanged(int adapterPosition);
    }

    private CheckBox checkBox;
    private TextView textView;

    private CheckBoxControlViewCallback viewCallback;

    public CheckBoxControlViewHolder(View itemView, CheckBoxControlViewCallback checkBoxControlViewCallback) {
        super(itemView);
        this.viewCallback = checkBoxControlViewCallback;
        this.checkBox = itemView.findViewById(R.id.checkbox);
        this.textView = itemView.findViewById(R.id.text_view);
        this.checkBox.setOnCheckedChangeListener(this);
        this.itemView.setOnClickListener(this);
    }

    public void bind(CheckBoxControlViewModel checkBoxControlViewModel) {
        this.textView.setText(checkBoxControlViewModel.getText());
        this.checkBox.setOnCheckedChangeListener(null);
        this.checkBox.setChecked(checkBoxControlViewModel.isChecked());
        this.checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (viewCallback != null && getAdapterPosition() >= 0) {
            viewCallback.onCheckControlChanged(getAdapterPosition());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(itemView)) {
            this.checkBox.toggle();
        }
    }
}
