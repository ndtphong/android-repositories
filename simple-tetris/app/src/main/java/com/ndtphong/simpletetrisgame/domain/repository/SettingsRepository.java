package com.ndtphong.simpletetrisgame.domain.repository;

import com.ndtphong.simpletetrisgame.domain.model.GameSettings;

public interface SettingsRepository {

    GameSettings getSettings();

    void setSoundEnabled(boolean enabled);

    void setAnimationEnabled(boolean enabled);
}
