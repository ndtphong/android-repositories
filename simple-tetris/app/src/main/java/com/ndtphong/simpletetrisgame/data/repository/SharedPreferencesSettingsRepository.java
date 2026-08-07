package com.ndtphong.simpletetrisgame.data.repository;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.data.datasource.SettingsLocalDataSource;
import com.ndtphong.simpletetrisgame.domain.model.GameSettings;
import com.ndtphong.simpletetrisgame.domain.repository.SettingsRepository;

public final class SharedPreferencesSettingsRepository implements SettingsRepository {

    private final SettingsLocalDataSource dataSource;

    public SharedPreferencesSettingsRepository(
            @NonNull SettingsLocalDataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public GameSettings getSettings() {
        return dataSource.getSettings();
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        dataSource.setSoundEnabled(enabled);
    }

    @Override
    public void setAnimationEnabled(boolean enabled) {
        dataSource.setAnimationEnabled(enabled);
    }
}
