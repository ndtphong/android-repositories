package com.ndtphong.simpletetrisgame.ui.game;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameSettings;
import com.ndtphong.simpletetrisgame.domain.model.GameStatus;
import com.ndtphong.simpletetrisgame.ui.game.animation.GameAnimationEvent;
import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.ui.game.state.BoardUiState;
import com.ndtphong.simpletetrisgame.ui.game.state.NextPieceUiState;

import java.util.List;
import java.util.Objects;

public record GameUiState(
        @NonNull GameStatus status,
        int score,
        int highScore,
        int lines,
        int level,
        long dropDelayMillis,
        @NonNull BoardUiState board,
        @NonNull NextPieceUiState nextPiece,
        boolean soundEnabled,
        boolean animationEnabled,
        long animationBatchId,
        @NonNull List<GameAnimationEvent> animations
) {

    public GameUiState {
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(board, "board must not be null");
        Objects.requireNonNull(nextPiece, "nextPiece must not be null");
        Objects.requireNonNull(animations, "animations must not be null");

        animations = List.copyOf(animations);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static GameUiState initial(
            @NonNull BoardUiState board,
            @NonNull NextPieceUiState nextPiece,
            int highScore,
            @NonNull GameSettings settings
    ) {
        return new GameUiState(
                GameStatus.IDLE,
                0,
                highScore,
                0,
                1,
                1000L,
                board,
                nextPiece,
                settings.soundEnabled(),
                settings.animationEnabled(),
                0L,
                List.of()
        );
    }

    public GameUiState withStatus(@NonNull GameStatus newStatus) {
        return toBuilder()
                .status(newStatus)
                .build();
    }

    public GameUiState withAnimations(
            long newAnimationBatchId,
            @NonNull List<GameAnimationEvent> newAnimations
    ) {
        return toBuilder()
                .animations(newAnimationBatchId, newAnimations)
                .build();
    }

    public GameUiState withProgress(
            @NonNull BoardUiState newBoard,
            @NonNull NextPieceUiState newNextPiece,
            int newScore,
            int newHighScore,
            int newLines,
            int newLevel,
            long newDropDelay,
            long newAnimationBatchId,
            @NonNull List<GameAnimationEvent> newAnimations
    ) {
        return toBuilder()
                .board(newBoard)
                .nextPiece(newNextPiece)
                .score(newScore)
                .highScore(newHighScore)
                .lines(newLines)
                .level(newLevel)
                .dropDelayMillis(newDropDelay)
                .animations(newAnimationBatchId, newAnimations)
                .build();
    }

    public GameUiState withHighScore(int newHighScore) {
        return toBuilder()
                .highScore(newHighScore)
                .build();
    }

    public GameUiState withEngineResult(
            @NonNull BoardUiState newBoard,
            @NonNull NextPieceUiState newNextPiece,
            int clearedLines
    ) {
        return toBuilder()
                .board(newBoard)
                .nextPiece(newNextPiece)
                .lines(lines + clearedLines)
                .build();
    }

    public GameUiState withBoard(@NonNull BoardUiState newBoard) {
        return toBuilder()
                .board(newBoard)
                .build();
    }

    public GameUiState clearAnimations() {
        return toBuilder()
                .animations(animationBatchId, List.of())
                .build();
    }

    public static final class Builder {

        private GameStatus status = GameStatus.IDLE;
        private int score;
        private int highScore;
        private int lines;
        private int level = 1;
        private long dropDelayMillis = 1_000L;
        private BoardUiState board;
        private NextPieceUiState nextPiece;

        private boolean soundEnabled;
        private boolean animationEnabled;
        private long animationBatchId;
        private List<GameAnimationEvent> animations;

        private Builder() {
        }

        private Builder(GameUiState state) {
            this.status = state.status();
            this.score = state.score();
            this.highScore = state.highScore();
            this.lines = state.lines();
            this.level = state.level();
            this.dropDelayMillis = state.dropDelayMillis();
            this.board = state.board();
            this.nextPiece = state.nextPiece();
            this.animationBatchId = state.animationBatchId;
            this.animations = List.copyOf(state.animations);
        }

        public Builder status(@NonNull GameStatus status) {
            this.status = Objects.requireNonNull(
                    status,
                    "status must not be null"
            );
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder highScore(int highScore) {
            this.highScore = highScore;
            return this;
        }

        public Builder lines(int lines) {
            this.lines = lines;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder dropDelayMillis(long dropDelayMillis) {
            this.dropDelayMillis = dropDelayMillis;
            return this;
        }

        public Builder board(@NonNull BoardUiState board) {
            this.board = Objects.requireNonNull(board, "board must not be null");
            return this;
        }

        public Builder nextPiece(@NonNull NextPieceUiState nextPiece) {
            this.nextPiece = Objects.requireNonNull(nextPiece, "nextPiece must not be null");
            return this;
        }

        public Builder animations(long animationBatchId, @NonNull List<GameAnimationEvent> animations) {
            Objects.requireNonNull(animations, "nextPiece must not be null");

            this.animationBatchId = animationBatchId;
            this.animations = List.copyOf(animations);

            return this;
        }

        public GameUiState build() {
            return new GameUiState(
                    status,
                    score,
                    highScore,
                    lines,
                    level,
                    dropDelayMillis,
                    Objects.requireNonNull(board, "board must be set"),
                    Objects.requireNonNull(nextPiece, "nextPiece must be set"),
                    soundEnabled,
                    animationEnabled,
                    animationBatchId,
                    Objects.requireNonNull(animations, "animations must be set")
            );
        }
    }
}
