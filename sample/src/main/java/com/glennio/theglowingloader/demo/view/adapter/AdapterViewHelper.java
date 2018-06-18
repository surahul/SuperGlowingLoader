package com.glennio.theglowingloader.demo.view.adapter;

public interface AdapterViewHelper {
    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    void notifyItemInserted(int position);

    void notifyItemMoved(int fromPosition, int toPosition);

    void notifyItemRemoved(int position);
}
