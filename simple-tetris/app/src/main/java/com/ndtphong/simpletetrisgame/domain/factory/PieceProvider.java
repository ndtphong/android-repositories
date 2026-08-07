package com.ndtphong.simpletetrisgame.domain.factory;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

public interface PieceProvider {

    @NonNull
    TetrominoType next();
}
