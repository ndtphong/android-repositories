package com.ndtphong.simpletetrisgame.ui.game.animation;

import androidx.annotation.NonNull;

public record GameAnimationEvent(
        @NonNull GameAnimationType type,
        @NonNull int[] rows,
        int distance
) {
    public GameAnimationEvent {
        rows = rows.clone();
    }

    @Override
    public int[] rows() {
        return rows.clone();
    }

    public static GameAnimationEvent hardDrop(int distance) {
        return new GameAnimationEvent(
                GameAnimationType.HARD_DROP,
                new int[0],
                distance
        );
    }

    public static GameAnimationEvent lineClear(
            @NonNull int[] rows
    ) {
        return new GameAnimationEvent(
                GameAnimationType.LINE_CLEAR,
                rows,
                0
        );
    }

    public static GameAnimationEvent pieceSpawn() {
        return new GameAnimationEvent(
                GameAnimationType.PIECE_SPAWN,
                new int[0],
                0
        );
    }

    public static GameAnimationEvent gameOver() {
        return new GameAnimationEvent(
                GameAnimationType.GAME_OVER,
                new int[0],
                0
        );
    }
}
