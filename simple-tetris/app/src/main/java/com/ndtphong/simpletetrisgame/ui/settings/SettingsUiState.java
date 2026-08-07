package com.ndtphong.simpletetrisgame.ui.settings;

public record SettingsUiState(
        boolean soundEnabled,
        boolean animationEnabled
) {
    public static SettingsUiState initial() {
        return builder()
                .soundEnabled(true)
                .animationEnabled(true)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .soundEnabled(soundEnabled)
                .animationEnabled(animationEnabled);
    }

    public static final class Builder {
        private boolean soundEnabled;
        private boolean animationEnabled;

        private Builder() {
        }

        public Builder soundEnabled(boolean soundEnabled) {
            this.soundEnabled = soundEnabled;
            return this;
        }

        public Builder animationEnabled(boolean animationEnabled) {
            this.animationEnabled = animationEnabled;
            return this;
        }

        public SettingsUiState build() {
            return new SettingsUiState(
                    soundEnabled,
                    animationEnabled
            );
        }
    }
}