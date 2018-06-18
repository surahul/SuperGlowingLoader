package com.glennio.theglowingloader.demo.view.adapter.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.glennio.theglowingloader.demo.model.LineManagerControlViewModel;

public class LineManagerControlViewHolder extends RecyclerView.ViewHolder {
    public void bind(LineManagerControlViewModel lineManagerControlViewModel) {
    }

    public interface LineManagerControlViewCallback {

    }

    private LineManagerControlViewCallback viewCallback;

    public LineManagerControlViewHolder(View itemView, LineManagerControlViewCallback viewCallback) {
        super(itemView);
        this.viewCallback = viewCallback;
    }
}
