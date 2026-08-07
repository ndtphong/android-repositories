package com.ndtphong.simpletetrisgame.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.data.repository.SharedPreferencesSettingsRepository;
import com.ndtphong.simpletetrisgame.domain.repository.SettingsRepository;

public final class SettingsViewModelFactory implements ViewModelProvider.Factory{

    private final SettingsRepository repository;

    public SettingsViewModelFactory(
            @NonNull SettingsRepository repository
    ) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
    }
}
