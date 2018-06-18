package com.glennio.theglowingloader.demo.presenter.adapter;

import android.support.annotation.Nullable;

import com.glennio.theglowingloader.demo.model.AdapterDataItem;
import com.glennio.theglowingloader.demo.view.adapter.AdapterViewHelper;

import java.util.List;

public interface AdapterDataProvider {

    void setViewHelper(@Nullable AdapterViewHelper viewHelper);

    int getItemCount();

    void addItem(AdapterDataItem adapterDataItem);

    AdapterDataItem getItem(int position);

    void addItems(List<AdapterDataItem> dataItems);

    void setItem(int position, AdapterDataItem dataItem);
}
