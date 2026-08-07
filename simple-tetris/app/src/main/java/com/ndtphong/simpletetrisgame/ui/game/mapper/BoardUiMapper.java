package com.ndtphong.simpletetrisgame.ui.game.mapper;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;
import com.ndtphong.simpletetrisgame.ui.game.state.BoardUiState;
import com.ndtphong.simpletetrisgame.ui.game.state.PieceUiState;

public final class BoardUiMapper {

    private final PieceUiMapper pieceMapper;

    public BoardUiMapper(@NonNull PieceUiMapper pieceMapper) {
        this.pieceMapper = pieceMapper;
    }

    @NonNull
    public BoardUiState map(@NonNull BoardState board) {
        TetrominoType[][] source = board.lockedCells();

        int[][] lockedCells = new int[board.rows()][board.columns()];

        for (int row = 0; row < board.rows(); row++) {
            for (int column = 0; column < board.columns(); column++) {
                TetrominoType type = source[row][column];

                if (type != null) {
                    lockedCells[row][column] = pieceMapper.colorResource(type);
                }
            }
        }

        PieceUiState activePiece = board.activePiece() == null
                ? null
                : pieceMapper.map(board.activePiece());

        return new BoardUiState(
                board.rows(),
                board.columns(),
                lockedCells,
                activePiece,
                board.ghostRow()
        );
    }
}
