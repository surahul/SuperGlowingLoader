package com.rahul.simpletutorialtooltip.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

/**
 * @author Rahul Verma on 15/01/17 <rv@videoder.com>
 */

@SuppressLint("CommitPrefEdits")
public class Prefs {

    private static final String PREFS_FILE_NAME = "tooltip_showcase_prefs";
    private static final String TRIGGER_COUNT_KEY_BASE = "trigger_count_";
    private static final String TRIGGERED_KEY_BASE = "triggered_";

    public static void incrementTrigger(@NonNull Context context, int id) {
        SharedPreferences sharedPreferences = prefs(context);
        int currentTriggerValue = sharedPreferences.getInt(TRIGGER_COUNT_KEY_BASE + String.valueOf(id), 0);
        currentTriggerValue++;
        sharedPreferences.edit().putInt(TRIGGER_COUNT_KEY_BASE + String.valueOf(id), currentTriggerValue).commit();
    }

    public static boolean timeToTrigger(@NonNull Context context, int id, int triggerThreshold, boolean singleUse) {
        if (singleUse && hasTriggered(context, id))
            return false;
        SharedPreferences sharedPreferences = prefs(context);
        int currentTriggerValue = sharedPreferences.getInt(TRIGGER_COUNT_KEY_BASE + String.valueOf(id), 0);
        return currentTriggerValue >= triggerThreshold;
    }

    public static void setTriggered(@NonNull Context context, int id) {
        SharedPreferences sharedPreferences = prefs(context);
        sharedPreferences.edit().putBoolean(TRIGGERED_KEY_BASE + String.valueOf(id), true).commit();
    }

    public static boolean hasTriggered(@NonNull Context context, int id) {
        SharedPreferences sharedPreferences = prefs(context);
        return sharedPreferences.getBoolean(TRIGGERED_KEY_BASE + String.valueOf(id), false);
    }

    @CheckResult
    @NonNull
    protected static SharedPreferences prefs(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }
}
