package com.glennio.theglowingloader.demo.view;

import android.content.Context;

import com.glennio.glowingloaderlib.GlowingLoaderView;
import com.glennio.theglowingloader.demo.view.adapter.AdapterViewHelper;

public interface ViewHelper {
    AdapterViewHelper getAdapterViewHelper();
    Context getContext();

    GlowingLoaderView getGlowLoaderView();
}
