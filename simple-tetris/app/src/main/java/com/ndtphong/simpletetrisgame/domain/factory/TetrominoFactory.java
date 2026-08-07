package com.ndtphong.simpletetrisgame.domain.factory;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.domain.model.Tetromino;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

public final class TetrominoFactory {

    public Tetromino create(TetrominoType type) {
        return switch (type) {
            case I -> new Tetromino(
                    type,
                    new int[][]{
                            {1, 1, 1, 1}
                    },
                    R.color.tetromino_i
            );

            case J -> new Tetromino(
                    type,
                    new int[][]{
                            {1, 0, 0},
                            {1, 1, 1}
                    },
                    R.color.tetromino_j
            );

            case L -> new Tetromino(
                    type,
                    new int[][]{
                            {0, 0, 1},
                            {1, 1, 1}
                    },
                    R.color.tetromino_l
            );

            case O -> new Tetromino(
                    type,
                    new int[][]{
                            {1, 1},
                            {1, 1}
                    },
                    R.color.tetromino_o
            );

            case S -> new Tetromino(
                    type,
                    new int[][]{
                            {0, 1, 1},
                            {1, 1, 0}
                    },
                    R.color.tetromino_s
            );

            case T -> new Tetromino(
                    type,
                    new int[][]{
                            {0, 1, 0},
                            {1, 1, 1}
                    },
                    R.color.tetromino_t
            );

            case Z -> new Tetromino(
                    type,
                    new int[][]{
                            {1, 1, 0},
                            {0, 1, 1}
                    },
                    R.color.tetromino_z
            );
        };
    }
}
