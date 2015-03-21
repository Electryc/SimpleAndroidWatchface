package com.catinean.simpleandroidwatchface;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class WatchConfigurationPreferences {

    private static final String NAME = "WatchConfigurationPreferences";
    private static final String KEY_BACKGROUND_COLOUR = NAME + ".KEY_BACKGROUND_COLOUR";
    private static final String KEY_DATE_TIME_COLOUR = NAME + ".KEY_DATE_TIME_COLOUR";
    private static final int DEFAULT_BACKGROUND_COLOUR = Color.parseColor("black");
    private static final int DEFAULT_DATE_TIME_COLOUR = Color.parseColor("white");

    private final SharedPreferences preferences;

    public static WatchConfigurationPreferences newInstance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return new WatchConfigurationPreferences(preferences);
    }

    WatchConfigurationPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public int getBackgroundColour() {
        return preferences.getInt(KEY_BACKGROUND_COLOUR, DEFAULT_BACKGROUND_COLOUR);
    }

    public int getDateAndTimeColour() {
        return preferences.getInt(KEY_DATE_TIME_COLOUR, DEFAULT_DATE_TIME_COLOUR);
    }

    public void setBackgroundColour(int color) {
        preferences.edit().putInt(KEY_BACKGROUND_COLOUR, color).apply();
    }

    public void setDateAndTimeColour(int color) {
        preferences.edit().putInt(KEY_DATE_TIME_COLOUR, color).apply();
    }
}
