package com.glennio.theglowingloader.demo.presenter;

import com.glennio.theglowingloader.demo.presenter.adapter.AdapterDataProvider;
import com.glennio.theglowingloader.demo.view.ViewHelper;

public interface Presenter {

    void setViewHelper(ViewHelper viewHelper);

    AdapterDataProvider getAdapterDataProvider();

    void onCheckControlChanged(int adapterPosition);

    void onSimpleRangeBarSeekChanged(int adapterPosition, float progress, boolean trackingTouch);

    void onRemoveColorClicked(int adapterPosition);

    void onAddColorClicked(int adapterPosition);
}
