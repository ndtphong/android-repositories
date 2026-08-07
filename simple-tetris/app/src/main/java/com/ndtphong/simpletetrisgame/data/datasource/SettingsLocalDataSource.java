package com.ndtphong.simpletetrisgame.data.datasource;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameSettings;

public final class SettingsLocalDataSource {

    private static final String FILE_NAME = "tetris_settings";

    private static final String KEY_SOUND = "sound_enabled";

    private static final String KEY_ANIMATION = "animation_enabled";

    private final SharedPreferences preferences;

    public SettingsLocalDataSource(
            @NonNull Context context
    ) {
        preferences = context.getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
        );
    }

    @NonNull
    public GameSettings getSettings() {
        return new GameSettings(
                preferences.getBoolean(KEY_SOUND, true),
                preferences.getBoolean(KEY_ANIMATION, true)
        );
    }

    public void setSoundEnabled(boolean enabled) {
        preferences.edit()
                .putBoolean(KEY_SOUND, enabled)
                .apply();
    }

    public void setAnimationEnabled(boolean enabled) {
        preferences.edit()
                .putBoolean(KEY_ANIMATION, enabled)
                .apply();
    }
}
