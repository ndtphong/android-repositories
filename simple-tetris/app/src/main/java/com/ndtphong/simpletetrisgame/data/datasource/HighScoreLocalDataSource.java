package com.ndtphong.simpletetrisgame.data.datasource;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public final class HighScoreLocalDataSource {

    private static final String FILE_NAME = "tetris_high_score";

    private static final String KEY_HIGH_SCORE = "high_score";

    private final SharedPreferences preferences;

    public HighScoreLocalDataSource(@NonNull Context context) {
        preferences = context.getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
        );
    }

    public int getHighScore() {
        return preferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public synchronized int saveIfHigher(int score) {
        int currentHighScore = getHighScore();

        if (score <= currentHighScore) {
            return currentHighScore;
        }

        preferences.edit()
                .putInt(KEY_HIGH_SCORE, score)
                .apply();

        return score;
    }
}
