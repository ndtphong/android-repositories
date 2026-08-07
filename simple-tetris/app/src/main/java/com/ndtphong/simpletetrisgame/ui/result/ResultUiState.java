package com.ndtphong.simpletetrisgame.ui.result;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameResult;

public record ResultUiState(
        int score,
        int highScore,
        boolean newHighScore
) {
    public static ResultUiState initial() {
        return builder()
                .score(0)
                .highScore(0)
                .newHighScore(false)
                .build();
    }

    @NonNull
    public static ResultUiState from(
            @NonNull GameResult result
    ) {
        return new ResultUiState(
                result.score(),
                result.highScore(),
                result.newHighScore()
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .score(score)
                .highScore(highScore)
                .newHighScore(newHighScore);
    }

    public static final class Builder {
        private int score;
        private int highScore;
        private boolean newHighScore;

        private Builder() {
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder highScore(int highScore) {
            this.highScore = highScore;
            return this;
        }

        public Builder newHighScore(boolean newHighScore) {
            this.newHighScore = newHighScore;
            return this;
        }

        public ResultUiState build() {
            return new ResultUiState(
                    score,
                    highScore,
                    newHighScore
            );
        }
    }
}