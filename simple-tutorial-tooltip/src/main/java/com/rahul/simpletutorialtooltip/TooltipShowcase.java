package com.rahul.simpletutorialtooltip;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rahul.simpletutorialtooltip.internal.Prefs;

import java.lang.ref.WeakReference;

/**
 * @author Rahul Verma on 15/01/17 <rv@videoder.com>
 */

public class TooltipShowcase {

    private WeakReference<Tooltip> tooltip;
    private int showcaseId = -1;
    private int minTriggerCount = 0;
    private boolean singleUse = false;
    private ShouldShowValidator shouldShowValidator;
    private TooltipShowcase(Tooltip tooltip) {
        this.tooltip = new WeakReference<>(tooltip);
    }

    private void setSingleUse(int showcaseId) {
        this.showcaseId = showcaseId;
        this.singleUse = true;
    }

    private void setMinTriggerCount(int showcaseId, int triggerCount) {
        this.showcaseId = showcaseId;
        this.minTriggerCount = triggerCount;
    }

    public boolean show(Context context) {
        if (shouldShowValidator != null && !shouldShowValidator.shouldShow())
            return false;
        if (tooltip != null) {
            Tooltip tooltip = this.tooltip.get();
            if (tooltip != null) {
                if (showcaseId == -1) {
                    tooltip.show();
                    return true;
                } else {
                    if (Prefs.timeToTrigger(context, showcaseId, minTriggerCount, singleUse)) {
                        tooltip.show();
                        Prefs.setTriggered(context, showcaseId);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setShouldShowValidator(ShouldShowValidator shouldShowValidator) {
        this.shouldShowValidator = shouldShowValidator;
    }

    public interface ShouldShowValidator {
        boolean shouldShow();
    }

    public static class Builder {
        private Context context;
        private TooltipShowcase tooltipShowcase;

        public Builder(Context context, @NonNull Tooltip tooltip) {
            this.context = context;
            this.tooltipShowcase = new TooltipShowcase(tooltip);
        }

        public Builder setSingleUse(int showcaseId) {
            tooltipShowcase.setSingleUse(showcaseId);
            return this;
        }

        public Builder setShouldShowValidator(ShouldShowValidator shouldShowValidator) {
            tooltipShowcase.setShouldShowValidator(shouldShowValidator);
            return this;
        }

        public Builder setShowOnMinTriggerCount(int showcaseId, int triggerCount) {
            tooltipShowcase.setMinTriggerCount(showcaseId, triggerCount);
            return this;
        }

        public TooltipShowcase build() {
            return tooltipShowcase;
        }

        public boolean show() {
            return build().show(context);
        }
    }

}
