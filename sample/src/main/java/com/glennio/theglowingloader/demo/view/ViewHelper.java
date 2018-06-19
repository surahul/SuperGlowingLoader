package com.glennio.theglowingloader.demo.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;

import com.glennio.glowingloaderlib.GlowingLoaderView;
import com.glennio.theglowingloader.demo.view.adapter.AdapterViewHelper;

public interface ViewHelper {
    public interface Callback{
        Activity getActivity();
    }


    AdapterViewHelper getAdapterViewHelper();
    Context getContext();

    GlowingLoaderView getGlowLoaderView();

    void onResume();

    void setBackgroundColor(@ColorInt int color);
}
