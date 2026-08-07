package com.ndtphong.simpletetrisgame.domain.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public record Tetromino(
        @NonNull TetrominoType type,
        int[][] shape,
        int colorResource
) {
    public Tetromino {
        Objects.requireNonNull(type);
        shape = copy(shape);
    }

    @Override
    public int[][] shape() {
        return copy(shape);
    }

    private static int[][] copy(int[][] source) {
        Objects.requireNonNull(source);

        int[][] result = new int[source.length][];

        for (int row = 0; row < source.length; row++) {
            result[row] = source[row].clone();
        }

        return result;
    }
}
