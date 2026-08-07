package com.ndtphong.simpletetrisgame.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ScreenUiState> mutableUiState =
            new MutableLiveData<>(ScreenUiState.home());

    public LiveData<ScreenUiState> getUiState() {
        return mutableUiState;
    }

    public void navigateTo(@NonNull Screen screen) {
        ScreenUiState current = requireUiState();
        mutableUiState.setValue(current.navigateTo(screen));
    }

    public void setLoading(boolean loading) {
        ScreenUiState current = requireUiState();
        mutableUiState.setValue(current.loading(loading));
    }

    public void setError(String message) {
        ScreenUiState current = requireUiState();
        mutableUiState.setValue(current.error(message));
    }

    public void clearError() {
        ScreenUiState current = requireUiState();
        mutableUiState.setValue(current.error(null));
    }

    private ScreenUiState requireUiState() {
        ScreenUiState current = mutableUiState.getValue();
        return current != null ? current : ScreenUiState.home();
    }
}
