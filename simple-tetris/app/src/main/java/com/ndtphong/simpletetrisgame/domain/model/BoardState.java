package com.ndtphong.simpletetrisgame.domain.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public record BoardState(
        int rows,
        int columns,
        @NonNull TetrominoType[][] lockedCells,
        @Nullable Piece activePiece,
        int ghostRow
) {
    public BoardState {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Invalid board size");
        }
        lockedCells = copy(lockedCells);
    }

    @Override
    public TetrominoType[][] lockedCells() {
        return copy(lockedCells);
    }

    private static TetrominoType[][] copy(TetrominoType[][] source) {
        Objects.requireNonNull(source);

        TetrominoType[][] result = new TetrominoType[source.length][];

        for (int row = 0; row < source.length; row++) {
            result[row] = source[row].clone();
        }

        return result;
    }
}
