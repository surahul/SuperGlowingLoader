package com.glennio.theglowingloader.demo.view.adapter.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.glennio.theglowingloader.demo.model.MinMaxSeekBarViewModel;

public class MinMaxSeekBarControlViewHolder extends RecyclerView.ViewHolder {
    public void bind(MinMaxSeekBarViewModel minMaxSeekBarViewModel) {
    }

    public interface MinMaxSeekBarControlViewCallack {

    }

    private MinMaxSeekBarControlViewCallack viewCallack;

    public MinMaxSeekBarControlViewHolder(View itemView, MinMaxSeekBarControlViewCallack viewCallack) {
        super(itemView);
        this.viewCallack = viewCallack;
    }
}
