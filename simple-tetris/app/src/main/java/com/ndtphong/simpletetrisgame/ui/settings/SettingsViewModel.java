package com.ndtphong.simpletetrisgame.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ndtphong.simpletetrisgame.data.repository.SharedPreferencesSettingsRepository;
import com.ndtphong.simpletetrisgame.domain.model.GameSettings;
import com.ndtphong.simpletetrisgame.domain.repository.SettingsRepository;

public final class SettingsViewModel extends ViewModel {

    private final SettingsRepository repository;

    private final MutableLiveData<SettingsUiState> mutableUiState = new MutableLiveData<>();
    ;

    public SettingsViewModel(@NonNull SettingsRepository repository) {
        this.repository = repository;
        reload();
    }

    public void reload() {
        GameSettings settings = repository.getSettings();

        mutableUiState.setValue(
                currentState().toBuilder()
                        .animationEnabled(settings.animationEnabled())
                        .soundEnabled(settings.soundEnabled())
                        .build()
        );
    }

    public LiveData<SettingsUiState> getUiState() {
        return mutableUiState;
    }

    public void setSoundEnabled(boolean enabled) {
        repository.setSoundEnabled(enabled);
        mutableUiState.setValue(
                currentState().toBuilder()
                        .soundEnabled(enabled)
                        .build()
        );
    }

    public void setAnimationEnabled(boolean enabled) {
        repository.setAnimationEnabled(enabled);
        mutableUiState.setValue(
                currentState().toBuilder()
                        .animationEnabled(enabled)
                        .build()
        );
    }

    private SettingsUiState currentState() {
        SettingsUiState state = mutableUiState.getValue();
        return state != null ? state : SettingsUiState.initial();
    }
}
