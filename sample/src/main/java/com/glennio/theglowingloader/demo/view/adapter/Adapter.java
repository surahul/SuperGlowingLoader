package com.glennio.theglowingloader.demo.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.demo.model.AdapterDataItem;
import com.glennio.theglowingloader.demo.presenter.adapter.AdapterDataProvider;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.CheckBoxControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.ColorArrayControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.LineManagerControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.MinMaxSeekBarControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.SimpleSeekBarControlViewHolder;

public class Adapter extends RecyclerView.Adapter implements AdapterViewHelper{
    private AdapterDataProvider dataProvider;

    private CheckBoxControlViewHolder.CheckBoxControlViewCallback checkBoxControlViewCallback;
    private ColorArrayControlViewHolder.ColorArrayControlViewCallback colorArrayControlViewCallback;
    private LineManagerControlViewHolder.LineManagerControlViewCallback lineManagerControlViewCallback;
    private MinMaxSeekBarControlViewHolder.MinMaxSeekBarControlViewCallack minMaxSeekBarControlViewCallack;
    private SimpleSeekBarControlViewHolder.SimpleSeekBarControlViewCallback simpleSeekBarControlViewCallback;

    public Adapter(AdapterDataProvider dataProvider, CheckBoxControlViewHolder.CheckBoxControlViewCallback checkBoxControlViewCallback, ColorArrayControlViewHolder.ColorArrayControlViewCallback colorArrayControlViewCallback, LineManagerControlViewHolder.LineManagerControlViewCallback lineManagerControlViewCallback, MinMaxSeekBarControlViewHolder.MinMaxSeekBarControlViewCallack minMaxSeekBarControlViewCallack, SimpleSeekBarControlViewHolder.SimpleSeekBarControlViewCallback simpleSeekBarControlViewCallback) {
        this.dataProvider = dataProvider;
        this.checkBoxControlViewCallback = checkBoxControlViewCallback;
        this.colorArrayControlViewCallback = colorArrayControlViewCallback;
        this.lineManagerControlViewCallback = lineManagerControlViewCallback;
        this.minMaxSeekBarControlViewCallack = minMaxSeekBarControlViewCallack;
        this.simpleSeekBarControlViewCallback = simpleSeekBarControlViewCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @AdapterDataItem.Type int viewType) {
        switch (viewType) {

            case AdapterDataItem.TYPE_CHECK_BOX_CONTROL:
                return new CheckBoxControlViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_checkbox_control, parent, false), checkBoxControlViewCallback);
            case AdapterDataItem.TYPE_COLOR_ARRAY_PICKER:
                return new ColorArrayControlViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_color_array_picker_control, parent, false), colorArrayControlViewCallback);
            case AdapterDataItem.TYPE_LINE_MANAGER_CONTROL:
                return new LineManagerControlViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_line_manager_control, parent, false), lineManagerControlViewCallback);
            case AdapterDataItem.TYPE_MIN_MAX_SEEK_BAR_CONTROL:
                return new MinMaxSeekBarControlViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_min_max_seek_bar_control, parent, false), minMaxSeekBarControlViewCallack);

            case AdapterDataItem.TYPE_SIMPLE_SEEK_BAR_CONTROL:
                return new SimpleSeekBarControlViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_simple_seek_bar_control, parent, false), simpleSeekBarControlViewCallback);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterDataItem item = dataProvider.getItem(position);
        switch (getItemViewType(position)) {

            case AdapterDataItem.TYPE_CHECK_BOX_CONTROL:
                ((CheckBoxControlViewHolder) holder).bind(item.getCheckBoxControlViewModel());
                break;
            case AdapterDataItem.TYPE_COLOR_ARRAY_PICKER:
                ((ColorArrayControlViewHolder) holder).bind(item.getColorArrayPickerViewModel());
                break;
            case AdapterDataItem.TYPE_LINE_MANAGER_CONTROL:
                ((LineManagerControlViewHolder) holder).bind(item.getLineManagerControlViewModel());
                break;
            case AdapterDataItem.TYPE_MIN_MAX_SEEK_BAR_CONTROL:
                ((MinMaxSeekBarControlViewHolder) holder).bind(item.getMinMaxSeekBarViewModel());
                break;
            case AdapterDataItem.TYPE_SIMPLE_SEEK_BAR_CONTROL:
                ((SimpleSeekBarControlViewHolder) holder).bind(item.getSeekBarControlViewModel());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataProvider.getItemCount();
    }

    @AdapterDataItem.Type
    @Override
    public int getItemViewType(int position) {
        return dataProvider.getItem(position).getType();
    }
}
