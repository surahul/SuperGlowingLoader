package com.glennio.theglowingloader.demo.view.adapter.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.demo.model.ColorArrayPickerViewModel;
import com.glennio.theglowingloader.demo.view.custom.ColorArrayDisplayerView;

public class ColorArrayControlViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public interface ColorArrayControlViewCallback {

        void onRemoveColorClicked(int adapterPosition);

        void onAddColorClicked(int adapterPosition);
    }

    private ColorArrayControlViewCallback viewCallback;
    private ColorArrayDisplayerView colorArrayDisplayerView;
    private TextView titleTextView;
    private TextView valueTextView;
    private ImageView leftIcon;
    private ImageView rightIcon;

    public ColorArrayControlViewHolder(View itemView, ColorArrayControlViewCallback viewCallback) {
        super(itemView);
        this.viewCallback = viewCallback;
        this.titleTextView = itemView.findViewById(R.id.text_view);
        this.valueTextView = itemView.findViewById(R.id.value_tv);
        this.colorArrayDisplayerView = itemView.findViewById(R.id.color_array);
        leftIcon = itemView.findViewById(R.id.add);
        leftIcon.setOnClickListener(this);
        rightIcon = itemView.findViewById(R.id.remove);
        rightIcon.setOnClickListener(this);
    }

    public void bind(ColorArrayPickerViewModel colorArrayPickerViewModel) {
        titleTextView.setText(colorArrayPickerViewModel.getTitle());
        int[] colors = colorArrayPickerViewModel.getColors();

        int colorsLength = colors == null ? 0 : colors.length;
        valueTextView.setText(String.format(colorsLength <= 1 ? "%s color" : "%s colors", String.valueOf(colorsLength)));
        this.colorArrayDisplayerView.setColors(colors);
        leftIcon.setVisibility(colorArrayPickerViewModel.isShowLeftIcon() ? View.VISIBLE : View.GONE);
        rightIcon.setVisibility(colorArrayPickerViewModel.isShowRightIcon() ? View.VISIBLE : View.GONE);
        if (colorArrayPickerViewModel.getLeftIconResource() != -1)
            leftIcon.setImageResource(colorArrayPickerViewModel.getLeftIconResource());
        else {
            leftIcon.setImageResource(R.drawable.ic_remove);
        }
        if (colorArrayPickerViewModel.getRightIconResource() != -1)
            rightIcon.setImageResource(colorArrayPickerViewModel.getRightIconResource());
        else {
            rightIcon.setImageResource(R.drawable.ic_add);
        }
        valueTextView.setVisibility(colorArrayPickerViewModel.isShowValueText() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (viewCallback != null && getAdapterPosition() >= 0) {
            switch (v.getId()) {
                case R.id.add:
                    viewCallback.onAddColorClicked(getAdapterPosition());
                    break;
                case R.id.remove:
                    viewCallback.onRemoveColorClicked(getAdapterPosition());
                    break;
            }
        }

    }
}
