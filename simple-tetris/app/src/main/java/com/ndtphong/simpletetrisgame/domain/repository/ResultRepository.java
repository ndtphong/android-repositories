package com.ndtphong.simpletetrisgame.domain.repository;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameResult;

public interface ResultRepository {

    @NonNull
    GameResult getResult();

    void saveResult(@NonNull GameResult result);

    void clearResult();
}
