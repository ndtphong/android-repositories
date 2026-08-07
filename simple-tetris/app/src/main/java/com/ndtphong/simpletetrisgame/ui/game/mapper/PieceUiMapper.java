package com.ndtphong.simpletetrisgame.ui.game.mapper;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.domain.model.Piece;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;
import com.ndtphong.simpletetrisgame.ui.game.state.PieceUiState;

public final class PieceUiMapper {

    @NonNull
    public PieceUiState map(@NonNull Piece piece) {
        return new PieceUiState(
                piece.shape(),
                piece.row(),
                piece.column(),
                colorResource(piece.type())
        );
    }

    @ColorRes
    public int colorResource(
            @NonNull TetrominoType type
    ) {
        return switch (type) {
            case I -> R.color.tetromino_i;
            case J -> R.color.tetromino_j;
            case L -> R.color.tetromino_l;
            case O -> R.color.tetromino_o;
            case S -> R.color.tetromino_s;
            case T -> R.color.tetromino_t;
            case Z -> R.color.tetromino_z;
        };
    }
}
