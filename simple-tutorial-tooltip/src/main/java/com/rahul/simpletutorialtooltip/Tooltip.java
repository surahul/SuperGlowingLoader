package com.rahul.simpletutorialtooltip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahul.simpletutorialtooltip.internal.Alignment;
import com.rahul.simpletutorialtooltip.internal.TooltipBackgroundShape;
import com.rahul.simpletutorialtooltip.internal.TooltipView;
import com.rahul.simpletutorialtooltip.internal.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * @author Rahul Verma on 13/01/17 <rv@videoder.com>
 */

public class Tooltip {

    public static final int CONTAINED_IN_WINDOW = 1;
    public static final int CONTAINED_IN_VIEW_GROUP = 2;
    public static final int VIEW_TYPE_STANDARD = 1;
    public static final int VIEW_TYPE_CUSTOM = 2;
    @Alignment.TooltipAlignment
    int defaultAlignment = Alignment.DOWN;
    @ContainedIn
    private int containedIn;
    @Nullable
    private WeakReference<WindowManager> windowManager;
    @Nullable
    private WeakReference<ViewGroup> containerViewGroup;
    private WeakReference<TooltipView> tooltipView;
    private WeakReference<View> defaultView;
    private WeakReference<View> anchorView;
    private TranslationHelper translationHelper;
    private boolean usingCustomView = false;
    private Point screenSize = new Point();
    private boolean showing = false;
    private int minOffsetFromEdge = Utils.dpToPx(10);
    private int offsetFromAnchor = Utils.dpToPx(5);
    private Animator appearAnimation;
    private Animator disappearAnimation;
    private long showDelay = 0;
    private long autoHideDuration = -1;
    private int gravityInContainer = -1;
    private Rect containerBoundsDelta;
    private Handler handler;
    private TooltipView tooltipViewReferenceTillStart;


    private Tooltip(Context context) {
        handler = new Handler(Looper.getMainLooper());
        TooltipView tooltipView = new TooltipView(context, new TooltipBackgroundShape.Builder().build());
        tooltipView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.tooltipView = new WeakReference<TooltipView>(tooltipView);
        this.tooltipViewReferenceTillStart = tooltipView;
        setContainedInWindow(context);
        setContent(context, "");
    }

    private void setDefaultAnimators() {
        TooltipView tooltipView = this.tooltipView.get();

        if (tooltipView != null) {
            if (appearAnimation == null) {
                PropertyValuesHolder disappearScaleXValueHolder = PropertyValuesHolder.ofFloat("scaleX", .85f, 1);
                PropertyValuesHolder disappearScaleYValueHolder = PropertyValuesHolder.ofFloat("scaleY", .85f, 1);
                PropertyValuesHolder appearAlphaValueHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1);
                tooltipView.setPivotX((tooltipView.getMeasuredWidth() / 2f) + ((tooltipView.getMeasuredWidth() / 2f) * tooltipView.getTooltipBackgroundShape().getArrowPosOffset()));
                tooltipView.setPivotY(tooltipView.getTooltipBackgroundShape().getAlignment() == Alignment.DOWN ? 0 : tooltipView.getMeasuredHeight());
                appearAnimation = ObjectAnimator.ofPropertyValuesHolder(tooltipView, appearAlphaValueHolder, disappearScaleXValueHolder, disappearScaleYValueHolder);
                appearAnimation.setInterpolator(new DecelerateInterpolator(1.4f));
                appearAnimation.setDuration(400);
            }

            if (disappearAnimation == null) {

                PropertyValuesHolder disappearScaleXValueHolder = PropertyValuesHolder.ofFloat("scaleX", .85f);
                PropertyValuesHolder disappearScaleYValueHolder = PropertyValuesHolder.ofFloat("scaleY", .85f);
                PropertyValuesHolder disappearAlphaValueHolder = PropertyValuesHolder.ofFloat("alpha", 0f);

                disappearAnimation = ObjectAnimator.ofPropertyValuesHolder(tooltipView, disappearScaleXValueHolder, disappearScaleYValueHolder, disappearAlphaValueHolder);
                disappearAnimation.setInterpolator(new AccelerateInterpolator(1.3f));
                disappearAnimation.setDuration(220);
            }
        }
    }


    public void show() {
        if (showing)
            return;
        showing = true;
        switch (containedIn) {
            case CONTAINED_IN_VIEW_GROUP:
                showInViewGroup();
                break;
            case CONTAINED_IN_WINDOW:
                showInWindow();
                break;
        }
        if (autoHideDuration > 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (showing)
                        hide();
                }
            }, autoHideDuration);
        }
    }

    private void showInViewGroup() {
        post(new Runnable() {
            @Override
            public void run() {
                if (Tooltip.this.containerViewGroup != null) {
                    ViewGroup containerViewGroup = Tooltip.this.containerViewGroup.get();
                    TooltipView tooltipView = Tooltip.this.tooltipView.get();
                    if (containerViewGroup != null && tooltipView == null && tooltipViewReferenceTillStart != null) {
                        tooltipView = tooltipViewReferenceTillStart;
                        Tooltip.this.tooltipView = new WeakReference<TooltipView>(tooltipView);
                        tooltipViewReferenceTillStart = null;
                    }
                    if (containerViewGroup != null && tooltipView != null) {
                        tooltipView.setAlpha(0);
                        containerViewGroup.addView(tooltipView);
                        if (!hasAnchor()) {
                            applyGravity();
                        }
                        post(new Runnable() {
                            @Override
                            public void run() {
                                int[] containerSizeAndLocation = setInitialTooltipViewPosition();
                                setDefaultAnimators();
                                if (hasAnchor()) {
                                    translationHelper = new TranslationHelper(anchorView.get(), Tooltip.this, TranslationHelper.MODE_FOLLOW_BLINDLY, new int[]{containerSizeAndLocation[0], containerSizeAndLocation[1]}, new int[]{containerSizeAndLocation[2], containerSizeAndLocation[3]}, containerBoundsDelta);
                                }
                                appearAnimation.start();
                            }
                        });
                    }
                }
            }
        }, showDelay);
    }

    private void applyGravity() {
        if (gravityInContainer != -1 && tooltipView != null && containerViewGroup != null) {
            TooltipView tooltipView = this.tooltipView.get();
            ViewGroup container = this.containerViewGroup.get();
            if (tooltipView != null && container != null && tooltipView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) tooltipView.getLayoutParams()).gravity = gravityInContainer;
                tooltipView.setLayoutParams(tooltipView.getLayoutParams());

            }
        }
    }

    private boolean hasAnchor() {
        return anchorView != null && anchorView.get() != null;
    }

    private void showInWindow() {
        View activityRootView = null;
        if (activityRootView != null) {
            IBinder windowToken = activityRootView.getWindowToken();
            TooltipView tooltipView = this.tooltipView.get();
            if (windowManager != null && windowManager.get() != null && windowToken != null && tooltipView != null) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.gravity = Gravity.START | Gravity.TOP;
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.format = PixelFormat.TRANSLUCENT;
                lp.flags = computeFlags(lp.flags);
                lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                lp.token = windowToken;
                lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
                lp.setTitle("Tooltip:" + Integer.toHexString(hashCode()));
                windowManager.get().addView(tooltipView, lp);
            }
        }
    }

    private void setAutoHideDuration(long duration) {
        this.autoHideDuration = duration;
    }

    private void setContainerViewGroup(@NonNull ViewGroup viewGroup) {
        this.containerViewGroup = new WeakReference<>(viewGroup);
        this.containedIn = CONTAINED_IN_VIEW_GROUP;
    }

    private void setContainerBoundsDelta(Rect containerBoundsDelta) {
        this.containerBoundsDelta = containerBoundsDelta;
    }

    private void setContainedInWindow(@NonNull Context context) {
        this.windowManager = new WeakReference<>((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        this.containedIn = CONTAINED_IN_WINDOW;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.screenSize.set(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    private void setCustomView(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("cannot set null custom view to Tooltip");
        }
        TooltipView tooltipView = this.tooltipView.get();
        if (tooltipView != null) {
            tooltipView.removeAllViews();
            tooltipView.addView(view);
        }
        usingCustomView = true;
    }

    private void setAnchorView(View view) {
        this.anchorView = new WeakReference<View>(view);
    }

    private void post(Runnable runnable) {
        if (runnable != null) {
            TooltipView tooltipView = this.tooltipView.get();
            if (tooltipView != null) {
                handler.post(runnable);
            }
        }
    }

    private void post(Runnable runnable, long delay) {
        if (runnable != null) {
            TooltipView tooltipView = this.tooltipView.get();
            if (tooltipView != null) {
                handler.postDelayed(runnable, delay);
            }
        }
    }

    @Size(4)
    private int[] setInitialTooltipViewPosition() {
        ViewGroup containerViewGroup = this.containerViewGroup == null ? null : this.containerViewGroup.get();
        TooltipView tooltipView = this.tooltipView.get();
        if (containerViewGroup != null && tooltipView != null) {

            View anchorView = this.anchorView == null ? null : this.anchorView.get();

            if (anchorView != null) {
                int[] viewLocationInScreen = new int[2];
                anchorView.getLocationOnScreen(viewLocationInScreen);

                int[] containerLocationInScreen = new int[2];
                containerViewGroup.getLocationOnScreen(containerLocationInScreen);

                containerLocationInScreen[0] += containerViewGroup.getPaddingLeft();
                containerLocationInScreen[1] += containerViewGroup.getPaddingTop();

                int containerWidth = containerViewGroup.getMeasuredWidth() - (containerViewGroup.getPaddingLeft() + containerViewGroup.getPaddingRight());
                int containerHeight = containerViewGroup.getMeasuredHeight() - (containerViewGroup.getPaddingTop() + containerViewGroup.getPaddingBottom());

                int anchorLeftInContainer = viewLocationInScreen[0] - containerLocationInScreen[0];
                int anchorTopInContainer = viewLocationInScreen[1] - containerLocationInScreen[1];
                int anchorRightInContainer = anchorLeftInContainer + anchorView.getMeasuredWidth();
                int anchorBottomInContainer = anchorTopInContainer + anchorView.getMeasuredHeight();

                int anchorWidth = anchorRightInContainer - anchorLeftInContainer;
                int anchorHeight = anchorBottomInContainer - anchorTopInContainer;

                int tooltipHeight = tooltipView.getMeasuredHeight();
                int tooltipWidth = tooltipView.getMeasuredWidth();


                @Alignment.TooltipAlignment int alignment = defaultAlignment;
                if (tooltipHeight + offsetFromAnchor + anchorHeight + anchorTopInContainer > containerHeight - minOffsetFromEdge) {
                    alignment = Alignment.UP;
                }

                int tooltipLeft = (int) ((anchorLeftInContainer + (anchorWidth / 2f)) - tooltipWidth / 2f);


                int offset = 0;
                if (tooltipLeft < minOffsetFromEdge) {
                    offset = minOffsetFromEdge - tooltipLeft;
                }
                if (tooltipLeft + tooltipWidth > containerWidth - minOffsetFromEdge) {
                    offset = (containerWidth - minOffsetFromEdge) - (tooltipLeft + tooltipWidth);
                }

                tooltipLeft += offset;

                int tooltipTop = 0;


                switch (alignment) {
                    case Alignment.DOWN:
                        tooltipTop = anchorBottomInContainer + offsetFromAnchor;
                        break;
                    case Alignment.UP:
                        tooltipTop = anchorTopInContainer - (tooltipHeight + offsetFromAnchor);
                        break;
                }

                tooltipView.setTranslationX(tooltipLeft);
                tooltipView.setTranslationY(tooltipTop);

                tooltipView.getTooltipBackgroundShape().setAlignment(alignment);
                tooltipView.getTooltipBackgroundShape().setArrowPosOffsetPx(-offset);

                tooltipView.resetDrawable();

                return new int[]{containerWidth, containerHeight, containerLocationInScreen[0], containerLocationInScreen[1]};
            } else {


                int[] containerLocationInScreen = new int[2];
                containerViewGroup.getLocationOnScreen(containerLocationInScreen);

                int[] tooltipLocationInScreen = new int[2];
                tooltipView.getLocationOnScreen(tooltipLocationInScreen);

                int tooltipHeight = tooltipView.getMeasuredHeight();
                int tooltipWidth = tooltipView.getMeasuredWidth();

                int containerWidth = containerViewGroup.getMeasuredWidth() - (containerViewGroup.getPaddingLeft() + containerViewGroup.getPaddingRight());
                int containerHeight = containerViewGroup.getMeasuredHeight() - (containerViewGroup.getPaddingTop() + containerViewGroup.getPaddingBottom());


                int tooltipLeftInContainer = tooltipLocationInScreen[0] - containerLocationInScreen[0];
                int tooltipTopInContainer = tooltipLocationInScreen[1] - containerLocationInScreen[1];
                int tooltipRightInContainer = tooltipLeftInContainer + tooltipWidth;
                int tooltipBottomInContainer = tooltipTopInContainer + tooltipHeight;

                int horizontalOffset = 0;
                if (tooltipLeftInContainer < minOffsetFromEdge) {
                    horizontalOffset = minOffsetFromEdge - tooltipLeftInContainer;
                }
                if (tooltipRightInContainer > containerWidth - minOffsetFromEdge) {
                    horizontalOffset = (containerWidth - minOffsetFromEdge) - (tooltipRightInContainer);
                }

                int verticalOffset = 0;
                if (tooltipTopInContainer < minOffsetFromEdge) {
                    verticalOffset = minOffsetFromEdge - tooltipTopInContainer;
                }
                if (tooltipBottomInContainer > containerHeight - minOffsetFromEdge) {
                    verticalOffset = (containerHeight - minOffsetFromEdge) - (tooltipBottomInContainer);
                }


                tooltipView.setTranslationX(horizontalOffset);
                tooltipView.setTranslationY(verticalOffset);

                tooltipView.getTooltipBackgroundShape().setArrowPosOffsetPx(-horizontalOffset);

                tooltipView.resetDrawable();

                return new int[]{containerWidth, containerHeight, containerLocationInScreen[0], containerLocationInScreen[1]};
            }

        }
        return new int[]{0, 0, 0, 0};
    }

    private void setMinOffsetFromEdge(@Px int offset) {
        this.minOffsetFromEdge = offset;
    }

    private void setOffsetFromAnchor(@Px int offset) {
        this.offsetFromAnchor = offset;
    }

    private void setContent(Context context, String content) {
        TooltipView tooltipView = this.tooltipView.get();
        if (tooltipView != null) {
            tooltipView.removeAllViews();

            View defaultView = this.defaultView == null ? null : this.defaultView.get();
            if (defaultView == null) {
                defaultView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.default_tooltip_content_layout, tooltipView, false);
                this.defaultView = new WeakReference<>(defaultView);
            }
            defaultView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            tooltipView.addView(defaultView);
            updateDefaultViewContentColor();
            ((TextView) defaultView.findViewById(R.id.text_view)).setText(content);
        }
        usingCustomView = false;
    }

    public boolean isShowing() {
        return showing;
    }

    private void setCloseIcon(Drawable drawable) {
        TooltipView tooltipView = this.tooltipView == null ? null : this.tooltipView.get();
        if (tooltipView != null) {
            View closeView = tooltipView.findViewById(R.id.close);
            if (closeView != null && closeView instanceof ImageView) {
                ((ImageView) closeView).setImageDrawable(drawable);
            }
        }
    }

    private void setOnCloseClickListener(final View.OnClickListener onCloseClickListener) {
        TooltipView tooltipView = this.tooltipView == null ? null : this.tooltipView.get();
        if (tooltipView != null) {
            View closeView = tooltipView.findViewById(R.id.close);
            if (closeView != null) {
                closeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hide();
                        if (onCloseClickListener != null)
                            onCloseClickListener.onClick(v);
                    }
                });
            }
        }
    }

    public void setOnContentClickListener(View.OnClickListener onClickListener) {
        TooltipView tooltipView = this.tooltipView == null ? null : this.tooltipView.get();
        if (tooltipView != null) {
            tooltipView.setOnClickListener(onClickListener);
        }
    }

    public void hideImmediate() {
        if (translationHelper != null)
            translationHelper.release();
        showing = false;
        TooltipView tooltipView = Tooltip.this.tooltipView.get();
        if (tooltipView != null) {
            ViewParent parent = tooltipView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(tooltipView);
            }
        }
    }

    public void hide() {
        if (!showing)
            return;
        showing = false;
        if (disappearAnimation != null) {
            disappearAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    hideImmediate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationEnd(animation);
                }
            });
            disappearAnimation.start();
        } else {
            if (translationHelper != null)
                translationHelper.release();
        }
    }

    public void hideWithoutTracking() {
        if (translationHelper != null)
            translationHelper.release();
        hide();
    }

    private void setShowDelay(long showDelay) {
        this.showDelay = showDelay;
    }

    private void setTooltipBackgroundColor(@ColorInt int color) {
        TooltipView tooltipView = this.tooltipView.get();
        if (tooltipView != null) {
            tooltipView.getTooltipBackgroundShape().setColor(color);
            tooltipView.resetDrawable();
        }
        if (!usingCustomView) {
            updateDefaultViewContentColor();
        }
    }

    private void updateDefaultViewContentColor() {
        TooltipView tooltipView = this.tooltipView.get();
        if (tooltipView != null) {
            int color = tooltipView.getTooltipBackgroundShape().getColor();
            boolean isColorLight = Utils.isColorLight(color);
            int contentColor = isColorLight ? 0xfa000000 : 0Xfaffffff;
            ((TextView) tooltipView.findViewById(R.id.text_view)).setTextColor(contentColor);
            Utils.setTint((ImageView) tooltipView.findViewById(R.id.close), contentColor);
        }
    }

    private void setContentMaxWidth(@NonNull Context context, @Px int maxWidth) {
        View defaultView = this.defaultView.get();
        if (defaultView == null)
            setContent(context, "");
        defaultView = this.defaultView.get();
        if (defaultView != null) {
            TextView textview = (TextView) defaultView.findViewById(R.id.text_view);
            textview.setMaxWidth(maxWidth);
        }
    }

    private int computeFlags(int curFlags) {
        curFlags &= ~(
                WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        curFlags |= WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES;
        curFlags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        curFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        curFlags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        return curFlags;
    }

    public TooltipView getTooltipView() {
        if (tooltipView != null)
            return tooltipView.get();
        return null;
    }

    public void setDefaultAlignment(@Alignment.TooltipAlignment int defaultAlignment) {
        this.defaultAlignment = defaultAlignment;
    }

    private void setGravityInContainer(int gravity) {
        this.gravityInContainer = gravity;
    }

    @IntDef({CONTAINED_IN_WINDOW, CONTAINED_IN_VIEW_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContainedIn {
    }

    @IntDef({VIEW_TYPE_STANDARD, VIEW_TYPE_CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
    }

    public static class Builder {
        private Tooltip tooltip;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            this.tooltip = new Tooltip(context);
        }

        public Builder setContainerViewGroup(@NonNull ViewGroup viewGroup) {
            tooltip.setContainerViewGroup(viewGroup);
            return this;
        }

        public Builder setContainedInWindow(@NonNull Context context) {
            tooltip.setContainedInWindow(context);
            return this;
        }

        public Builder setContent(String content) {
            tooltip.setContent(context, content);
            return this;
        }

        public Builder setContentRes(@StringRes int contentRes) {
            tooltip.setContent(context, context.getString(contentRes));
            return this;
        }

        public Builder setCustomView(@NonNull View view) {
            tooltip.setCustomView(view);
            return this;
        }

        public Builder setCustomView(@LayoutRes int layoutRes) {
            setCustomView(((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutRes, tooltip.getTooltipView(), false));
            return this;
        }

        public Builder setAnchorView(View view) {
            tooltip.setAnchorView(view);
            return this;
        }

        public Builder setDefaultAlignment(@Alignment.TooltipAlignment int alignment) {
            tooltip.setDefaultAlignment(alignment);
            return this;
        }

        public Builder setContentMaxWidth(@Px int maxWidth) {
            tooltip.setContentMaxWidth(context, maxWidth);
            return this;
        }

        public Builder setColor(@ColorInt int color) {
            tooltip.setTooltipBackgroundColor(color);
            return this;
        }

        public Builder setColorRes(@ColorRes int colorRes) {
            return setColor(context.getResources().getColor(colorRes));
        }

        public Builder setContainerBoundsDelta(Rect containerBoundsDelta) {
            tooltip.setContainerBoundsDelta(containerBoundsDelta);
            return this;
        }

        public Builder setAutoHideDuration(long durationMillis) {
            tooltip.setAutoHideDuration(durationMillis);
            return this;
        }

        public Builder setMinOffsetFromEdge(@IntRange(from = 0) @Px int offset) {
            tooltip.setMinOffsetFromEdge(offset);
            return this;
        }

        public Builder setOffsetFromAnchor(@IntRange(from = 0) @Px int offset) {
            tooltip.setOffsetFromAnchor(offset);
            return this;
        }

        public Builder setOnContentClickListener(View.OnClickListener onClickListener) {
            tooltip.setOnContentClickListener(onClickListener);
            return this;
        }

        public Builder setShowDelay(long showDelay) {
            tooltip.setShowDelay(showDelay);
            return this;
        }

        public Tooltip build() {
            this.context = null;
            return tooltip;
        }

        public Builder setGravityInContainer(int gravity) {
            tooltip.setGravityInContainer(gravity);
            return this;
        }

        public Builder setCloseIconResource(@DrawableRes int drawableResource) {
            tooltip.setCloseIcon(context.getResources().getDrawable(drawableResource));
            return this;
        }

        public Builder setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
            tooltip.setOnCloseClickListener(onCloseClickListener);
            return this;
        }


        public void show() {
            build().show();
        }


    }

    private static class TranslationHelper {

        static final int MODE_FOLLOW_BLINDLY = 1;
        static final int MODE_FOLLOW_TILL_BOUNDS = 2;
        private WeakReference<View> anchorView;
        private Tooltip tooltip;
        @Mode
        private int mode;
        private boolean active = false;
        private int[] anchorLocation = new int[2];
        private int[] lastAnchorLocation = new int[2];
        private int[] containerSize = new int[2];
        private int[] containerLocationOnScreen = new int[2];
        private Rect containerBoundsDelta;

        private Runnable trackPositionRunnable = new Runnable() {

            @Override
            public void run() {
                final View anchorView = TranslationHelper.this.anchorView.get();
                final TooltipView tooltipView = TranslationHelper.this.tooltip.tooltipView.get();
                if (active & anchorView != null && tooltipView != null) {
                    anchorView.getLocationOnScreen(anchorLocation);
                    switch (mode) {
                        case TranslationHelper.MODE_FOLLOW_BLINDLY:
                            int translationY = anchorLocation[1] - lastAnchorLocation[1];
                            int translationX = anchorLocation[0] - lastAnchorLocation[0];
                            tooltipView.setTranslationY(tooltipView.getTranslationY() + translationY);
                            tooltipView.setTranslationX(tooltipView.getTranslationX() + translationX);
                            if ((anchorLocation[0] == 0 && anchorLocation[1] == 0)
                                    || (anchorLocation[0] + anchorView.getMeasuredWidth() < containerLocationOnScreen[0] + containerBoundsDelta.left)
                                    || (anchorLocation[0] > containerLocationOnScreen[0] + containerSize[0] + containerBoundsDelta.right)
                                    || (anchorLocation[1] + anchorView.getMeasuredHeight() < containerLocationOnScreen[1] + containerBoundsDelta.top)
                                    || (anchorLocation[1] > containerLocationOnScreen[1] + containerSize[1] + containerBoundsDelta.bottom)) {
                                tooltipView.setVisibility(View.INVISIBLE);
                            } else {
                                tooltipView.setVisibility(View.VISIBLE);
                            }
                            break;
                        case TranslationHelper.MODE_FOLLOW_TILL_BOUNDS:
                            break;
                    }
                    lastAnchorLocation[0] = anchorLocation[0];
                    lastAnchorLocation[1] = anchorLocation[1];
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                        anchorView.postDelayed(this, 20);
                } else if (tooltipView != null) {
                    tooltipView.setVisibility(View.INVISIBLE);
                }
            }
        };
        private WeakReference<ViewTreeObserver.OnPreDrawListener> onDrawListenerWeakReference = new WeakReference<ViewTreeObserver.OnPreDrawListener>(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                trackPositionRunnable.run();
                return true;
            }
        });


        public TranslationHelper(@NonNull final View anchorView, @NonNull Tooltip tooltip, @Mode int mode, @Size(2) int[] containerSize, @Size(2) int[] containerLocationOnScreen, Rect containerBoundsDelta) {
            this.anchorView = new WeakReference<>(anchorView);
            this.containerSize[0] = containerSize[0];
            this.containerSize[1] = containerSize[1];
            this.containerBoundsDelta = containerBoundsDelta == null ? new Rect() : containerBoundsDelta;

            this.containerLocationOnScreen[0] = containerLocationOnScreen[0];
            this.containerLocationOnScreen[1] = containerLocationOnScreen[1];

            this.tooltip = tooltip;
            this.mode = mode;
            this.active = true;

            anchorView.getLocationOnScreen(lastAnchorLocation);
            ViewTreeObserver.OnPreDrawListener onPreDrawListener = onDrawListenerWeakReference.get();
            if (onPreDrawListener != null)
                anchorView.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        }

        public void release() {
            active = false;
            View anchorView = this.anchorView.get();
            if (anchorView != null) {
                ViewTreeObserver.OnPreDrawListener onPreDrawListener = onDrawListenerWeakReference.get();
                if (onPreDrawListener != null) {
                    anchorView.getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
                }
            }
            this.anchorView.clear();
            this.onDrawListenerWeakReference.clear();

        }

        @IntDef({MODE_FOLLOW_BLINDLY, MODE_FOLLOW_TILL_BOUNDS})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Mode {
        }


    }

}
