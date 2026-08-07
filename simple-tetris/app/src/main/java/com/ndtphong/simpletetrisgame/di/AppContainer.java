package com.ndtphong.simpletetrisgame.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.data.datasource.ResultLocalDataSource;
import com.ndtphong.simpletetrisgame.data.datasource.SettingsLocalDataSource;
import com.ndtphong.simpletetrisgame.data.repository.SharedPreferencesResultRepository;
import com.ndtphong.simpletetrisgame.data.repository.SharedPreferencesSettingsRepository;
import com.ndtphong.simpletetrisgame.data.datasource.HighScoreLocalDataSource;
import com.ndtphong.simpletetrisgame.data.repository.DefaultGameRepository;
import com.ndtphong.simpletetrisgame.domain.repository.GameRepository;
import com.ndtphong.simpletetrisgame.domain.repository.ResultRepository;
import com.ndtphong.simpletetrisgame.domain.repository.SettingsRepository;
import com.ndtphong.simpletetrisgame.domain.runtime.GameLoop;
import com.ndtphong.simpletetrisgame.infrastructure.runtime.ThreadGameLoop;

public final class AppContainer {

    private final GameRepository gameRepository;
    private final SettingsRepository settingsRepository;
    private final ResultRepository resultRepository;

    public AppContainer(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();

        HighScoreLocalDataSource highScoreDataSource = new HighScoreLocalDataSource(applicationContext);
        SettingsLocalDataSource settingsDataSource = new SettingsLocalDataSource(applicationContext);
        ResultLocalDataSource resultDataSource = new ResultLocalDataSource(applicationContext);

        gameRepository = new DefaultGameRepository(highScoreDataSource);
        settingsRepository = new SharedPreferencesSettingsRepository(settingsDataSource);
        resultRepository = new SharedPreferencesResultRepository(resultDataSource);
    }

    @NonNull
    public GameRepository gameRepository() {
        return gameRepository;
    }

    @NonNull
    public SettingsRepository settingsRepository() {
        return settingsRepository;
    }

    @NonNull
    public ResultRepository resultRepository() {
        return resultRepository;
    }

    @NonNull
    public GameLoop createGameLoop() {
        return new ThreadGameLoop(60);
    }
}
