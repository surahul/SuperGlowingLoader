package com.glennio.theglowingloader.demo.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.glennio.glowingloaderlib.GlowingLoaderView;
import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.Utils;
import com.glennio.theglowingloader.demo.presenter.Presenter;
import com.glennio.theglowingloader.demo.view.adapter.Adapter;
import com.glennio.theglowingloader.demo.view.adapter.AdapterViewHelper;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.CheckBoxControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.ColorArrayControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.LineManagerControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.MinMaxSeekBarControlViewHolder;
import com.glennio.theglowingloader.demo.view.adapter.view_holders.SimpleSeekBarControlViewHolder;
import com.glennio.theglowingloader.demo.view.custom.UserLockBottomSheetBehavior;
import com.rahul.simpletutorialtooltip.Tooltip;
import com.rahul.simpletutorialtooltip.TooltipShowcase;
import com.rahul.simpletutorialtooltip.internal.Alignment;
import com.rahul.simpletutorialtooltip.internal.TooltipBackgroundShape;

public class ViewHelperImpl implements ViewHelper, View.OnClickListener {

    private static final int SHOWCASE_ID_SCROLL_TOOLTIP = 1;

    private View root;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Adapter adapter;

    private Presenter presenter;
    private GlowingLoaderView glowingLoaderView;
    private View controlsPanel;
    private View controlPanelHeader;
    private View controlPanelHeaderIcon;
    private ViewGroup tooltipContainer;
    private UserLockBottomSheetBehavior controlPanelBottomSheetBehavior;


    public ViewHelperImpl(View root, Presenter presenter) {
        this.root = root;
        this.presenter = presenter;
        this.recyclerView = root.findViewById(R.id.controls_recycler_view);


        this.adapter = new Adapter(presenter.getAdapterDataProvider(), checkBoxControlViewCallback, colorArrayControlViewCallback, lineManagerControlViewCallback, minMaxSeekBarControlViewCallack, simpleSeekBarControlViewCallback);
        this.layoutManager = new LinearLayoutManager(root.getContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setItemAnimator(null);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        this.recyclerView.addItemDecoration(dividerItemDecoration);
        this.recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        this.glowingLoaderView = root.findViewById(R.id.glowing_loader);

        this.controlPanelHeader = root.findViewById(R.id.controls_panel_header);
        this.controlPanelHeader.setOnClickListener(this);
        this.controlPanelHeaderIcon = root.findViewById(R.id.controls_panel_header_icon);
        this.controlsPanel = root.findViewById(R.id.controls_pannel);
        this.controlPanelBottomSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(controlsPanel);
        this.controlPanelBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        this.tooltipContainer = root.findViewById(R.id.tooltip_house);


        setUpLayoutParams();

        controlPanelHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeGlobalLayoutListener(controlsPanel, this);
                if (controlPanelBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetCallback.onSlide(controlsPanel, 0);
                else if (controlPanelBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetCallback.onSlide(controlsPanel, 1);
                controlPanelBottomSheetBehavior.setPeekHeight(controlPanelHeader.getMeasuredHeight());
            }
        });

//        setFullScreen();
    }

    private void setUpLayoutParams() {
        float screenHeight = Utils.getScreenHeight(getContext());
        float minControlPanelHeight = screenHeight *   .55f;
        controlsPanel.getLayoutParams().height = (int) minControlPanelHeight;
        controlsPanel.setLayoutParams(controlsPanel.getLayoutParams());
    }

    @Override
    public Context getContext() {
        return root.getContext();
    }

    private CheckBoxControlViewHolder.CheckBoxControlViewCallback checkBoxControlViewCallback = new CheckBoxControlViewHolder.CheckBoxControlViewCallback() {
        @Override
        public void onCheckControlChanged(int adapterPosition) {
            presenter.onCheckControlChanged(adapterPosition);
        }
    };

    private ColorArrayControlViewHolder.ColorArrayControlViewCallback colorArrayControlViewCallback = new ColorArrayControlViewHolder.ColorArrayControlViewCallback() {
        @Override
        public void onAddColorClicked(int adapterPosition) {
            presenter.onAddColorClicked(adapterPosition);
        }

        @Override
        public void onRemoveColorClicked(int adapterPosition) {
            presenter.onRemoveColorClicked(adapterPosition);
        }
    };

    private LineManagerControlViewHolder.LineManagerControlViewCallback lineManagerControlViewCallback = new LineManagerControlViewHolder.LineManagerControlViewCallback() {
    };

    private MinMaxSeekBarControlViewHolder.MinMaxSeekBarControlViewCallack minMaxSeekBarControlViewCallack = new MinMaxSeekBarControlViewHolder.MinMaxSeekBarControlViewCallack() {
    };

    private SimpleSeekBarControlViewHolder.SimpleSeekBarControlViewCallback simpleSeekBarControlViewCallback = new SimpleSeekBarControlViewHolder.SimpleSeekBarControlViewCallback() {
        @Override
        public void onSimpleRangeBarSeekChanged(int adaptgerPosition, float progress, boolean trackingTouch) {
            presenter.onSimpleRangeBarSeekChanged(adaptgerPosition, progress, trackingTouch);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.controls_panel_header:
                toggleControlPanelOpenState();
                break;
        }
    }

    private void toggleControlPanelOpenState() {
        if (controlPanelBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            controlPanelBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (controlPanelBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            controlPanelBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                checkAndShowScrollDownTooltip();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            controlPanelHeaderIcon.setScaleY(1f - 2 * slideOffset);
            updateGlowViewHeight(slideOffset);
        }
    };

    private void checkAndShowScrollDownTooltip() {
        Tooltip tooltip = new Tooltip.Builder(getContext())
                .setAnchorView(null)
                .setAutoHideDuration(7000)
                .setContainerViewGroup(tooltipContainer)
                .setContent(getContext().getString(R.string.scroll_tooltop_content))
                .setColor(getContext().getResources().getColor(R.color.colorAccent))
                .setGravityInContainer(Gravity.BOTTOM | Gravity.CENTER)
                .setMinOffsetFromEdge(Utils.dpToPx(16))
                .setDefaultAlignment(Alignment.DOWN)
                .setCloseIconResource(R.drawable.ic_down)
                .build();
        TooltipBackgroundShape tooltipBackgroundShape = tooltip.getTooltipView().getTooltipBackgroundShape();
        tooltipBackgroundShape.setCornerRadius(Utils.dpToPx(30));
        tooltipBackgroundShape.setArrowSize(0, 0);
        int padding = Utils.dpToPx(10);
        tooltipBackgroundShape.setPadding(new Rect(padding, padding, padding, padding));
        new TooltipShowcase.Builder(getContext(), tooltip)
                .setSingleUse(SHOWCASE_ID_SCROLL_TOOLTIP)
                .show();
    }

    @Override
    public AdapterViewHelper getAdapterViewHelper() {
        return adapter;
    }

    private void updateGlowViewHeight(float sheetOffset) {

        int height = (int) (root.getMeasuredHeight() - (controlPanelHeader.getMeasuredHeight() + (sheetOffset * (controlsPanel.getMeasuredHeight() - controlPanelHeader.getMeasuredHeight()))));
        glowingLoaderView.getLayoutParams().height = height;
        glowingLoaderView.setLayoutParams(glowingLoaderView.getLayoutParams());
    }

    public void setFullScreen() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public GlowingLoaderView getGlowLoaderView() {
        return glowingLoaderView;
    }

    @Override
    public void onResume() {
//        setFullScreen();
    }
}
