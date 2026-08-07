package com.ndtphong.simpletetrisgame.data.datasource;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameResult;

public final class ResultLocalDataSource {

    private static final String FILE_NAME = "tetris_result";

    private static final String KEY_SCORE = "score";

    private static final String KEY_HIGH_SCORE = "high_score";

    private static final String KEY_NEW_HIGH_SCORE = "new_high_score";

    private final SharedPreferences preferences;

    public ResultLocalDataSource(
            @NonNull Context context
    ) {
        preferences = context.getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
        );
    }

    @NonNull
    public GameResult getResult() {
        return new GameResult(
                preferences.getInt(KEY_SCORE, 0),
                preferences.getInt(KEY_HIGH_SCORE, 0),
                preferences.getBoolean(KEY_NEW_HIGH_SCORE, false)
        );
    }

    public void saveResult(@NonNull GameResult result) {
        preferences.edit()
                .putInt(KEY_SCORE, result.score())
                .putInt(KEY_HIGH_SCORE, result.highScore())
                .putBoolean(KEY_NEW_HIGH_SCORE, result.newHighScore())
                .apply();
    }

    public void clear() {
        preferences.edit()
                .clear()
                .apply();
    }
}
