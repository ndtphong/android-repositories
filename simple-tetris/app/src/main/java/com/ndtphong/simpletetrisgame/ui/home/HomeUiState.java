package com.ndtphong.simpletetrisgame.ui.home;

public record HomeUiState(
        String title,
        boolean playEnabled,
        boolean settingsEnabled
) {
    public static HomeUiState initial() {
        return builder()
                .title("TETRIS")
                .playEnabled(true)
                .settingsEnabled(true)
                .build();
    }

    public Builder toBuilder() {
        return new Builder()
                .title(title)
                .playEnabled(playEnabled)
                .settingsEnabled(settingsEnabled);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(HomeUiState state) {
        return new Builder()
                .title(state.title())
                .playEnabled(state.playEnabled())
                .settingsEnabled(state.settingsEnabled());
    }

    public static final class Builder {
        private String title;
        private boolean playEnabled;
        private boolean settingsEnabled;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder playEnabled(boolean playEnabled) {
            this.playEnabled = playEnabled;
            return this;
        }

        public Builder settingsEnabled(boolean settingsEnabled) {
            this.settingsEnabled = settingsEnabled;
            return this;
        }

        public HomeUiState build() {
            return new HomeUiState(
                    title,
                    playEnabled,
                    settingsEnabled
            );
        }
    }
}