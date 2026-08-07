package com.ndtphong.simpletetrisgame.domain.engine;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.factory.PieceProvider;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

final class FixedPieceProvider implements PieceProvider {

    private final TetrominoType[] pieces;
    private int index;

    FixedPieceProvider(@NonNull TetrominoType... pieces) {
        if (pieces.length == 0) {
            throw new IllegalArgumentException("Pieces cannot be empty");
        }

        this.pieces = pieces.clone();
    }

    @NonNull
    @Override
    public TetrominoType next() {
        TetrominoType result = pieces[index % pieces.length];

        index++;
        return result;
    }
}