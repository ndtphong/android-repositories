package com.ndtphong.simpletetrisgame.ui.game.state;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record BoardUiState(
        int rows,
        int columns,
        @NonNull int[][] lockedCells,
        @Nullable PieceUiState activePiece,
        int ghostRow
) {
    public BoardUiState {
        lockedCells = copy(lockedCells);
    }

    public static BoardUiState empty(int rows, int columns) {
        return new BoardUiState(
                rows,
                columns,
                new int[rows][columns],
                null,
                -1
        );
    }

    @Override
    public int[][] lockedCells() {
        return copy(lockedCells);
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];

        for (int index = 0; index < source.length; index++) {
            result[index] = source[index].clone();
        }

        return result;
    }
}
