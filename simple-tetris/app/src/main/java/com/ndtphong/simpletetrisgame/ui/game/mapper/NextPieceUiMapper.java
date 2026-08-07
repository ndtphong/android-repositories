package com.ndtphong.simpletetrisgame.ui.game.mapper;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.Tetromino;
import com.ndtphong.simpletetrisgame.ui.game.state.NextPieceUiState;

public final class NextPieceUiMapper {

    private final PieceUiMapper pieceMapper;

    public NextPieceUiMapper(@NonNull PieceUiMapper pieceMapper) {
        this.pieceMapper = pieceMapper;
    }

    @NonNull
    public NextPieceUiState map(@NonNull Tetromino tetromino) {
        return new NextPieceUiState(
                tetromino.shape(),
                pieceMapper.colorResource(tetromino.type())
        );
    }
}
