package com.ndtphong.simpletetrisgame.domain.engine;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.domain.model.Tetromino;

import java.util.Objects;

public record EngineResult(
        @NonNull BoardState board,
        @NonNull Tetromino nextPiece,
        @NonNull int[] clearedRows,
        int softDropCells,
        int hardDropCells,
        boolean pieceLocked,
        boolean gameOver
) {
    public EngineResult {
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextPiece);
        clearedRows = clearedRows.clone();
    }

    @Override
    public int[] clearedRows() {
        return clearedRows.clone();
    }

    public int clearedLines() {
        return clearedRows.length;
    }
}
