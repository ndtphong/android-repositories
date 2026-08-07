package com.ndtphong.simpletetrisgame.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record ScreenUiState(
        @NonNull Screen screen,
        boolean loading,
        @Nullable String error
) {
    public static ScreenUiState home() {
        return new ScreenUiState(Screen.HOME, false, null);
    }

    public ScreenUiState navigateTo(@NonNull Screen destination) {
        return new ScreenUiState(destination, false, null);
    }

    public ScreenUiState loading(boolean value) {
        return new ScreenUiState(screen, value, error);
    }

    public ScreenUiState error(@Nullable String message) {
        return new ScreenUiState(screen, false, message);
    }
}
