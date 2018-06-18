package com.glennio.theglowingloader.demo.presenter.adapter;

import android.support.annotation.Nullable;

import com.glennio.theglowingloader.demo.model.AdapterDataItem;
import com.glennio.theglowingloader.demo.view.adapter.AdapterViewHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterDataProviderImpl implements AdapterDataProvider {

    @Nullable
    private AdapterViewHelper viewHelper;
    private List<AdapterDataItem> dataItems;

    public AdapterDataProviderImpl() {
        this.dataItems = new ArrayList<>();
    }

    @Override
    public void setViewHelper(@Nullable AdapterViewHelper viewHelper) {
        this.viewHelper = viewHelper;
        if (viewHelper != null)
            viewHelper.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataItems == null ? 0 : dataItems.size();
    }

    @Override
    public AdapterDataItem getItem(int position) {
        return dataItems.get(position);
    }

    @Override
    public void addItems(List<AdapterDataItem> dataItems) {
        if (dataItems != null) {
            for (AdapterDataItem item : dataItems)
                addItem(item);
        }
    }

    @Override
    public void addItem(AdapterDataItem adapterDataItem) {
        dataItems.add(adapterDataItem);
        if (viewHelper != null)
            viewHelper.notifyItemInserted(dataItems.size() - 1);
    }

    @Override
    public void setItem(int position, AdapterDataItem dataItem) {
        if (position >= 0 && position < dataItems.size()) {
            dataItems.set(position, dataItem);
            if (viewHelper != null)
                viewHelper.notifyItemChanged(position);
        }
    }
}
