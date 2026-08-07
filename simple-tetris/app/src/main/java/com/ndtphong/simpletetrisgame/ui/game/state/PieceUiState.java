package com.ndtphong.simpletetrisgame.ui.game.state;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

public record PieceUiState (
        @NonNull int[][] shape,
        int row,
        int column,
        @ColorRes int colorResource
) {
    public PieceUiState {
        shape = copy(shape);
    }

    @Override
    public int[][] shape() {
        return copy(shape);
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];

        for (int index = 0; index < source.length; index++) {
            result[index] = source[index].clone();
        }

        return result;
    }
}
