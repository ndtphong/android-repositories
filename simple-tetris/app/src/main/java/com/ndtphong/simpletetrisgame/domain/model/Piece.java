package com.ndtphong.simpletetrisgame.domain.model;

import java.util.Objects;

public record Piece(
        TetrominoType type,
        int[][] shape,
        int row,
        int column
) {
    public Piece {
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

        for (int index = 0;
             index < source.length;
             index++) {
            result[index] = source[index].clone();
        }

        return result;
    }
}