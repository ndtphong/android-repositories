package com.ndtphong.simpletetrisgame.domain.repository;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.engine.EngineResult;

public interface GameRepository {

    @NonNull
    EngineResult createGame();

    @NonNull
    EngineResult tick();

    @NonNull
    EngineResult perform(@NonNull GameAction action);

    int getHighScore();

    int saveHighScore(int score);
}
