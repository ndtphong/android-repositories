package com.ndtphong.simpletetrisgame.data.repository;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.core.GameConfig;
import com.ndtphong.simpletetrisgame.data.datasource.HighScoreLocalDataSource;
import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.engine.EngineResult;
import com.ndtphong.simpletetrisgame.domain.engine.GameEngine;
import com.ndtphong.simpletetrisgame.domain.factory.SevenBagPieceProvider;
import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.repository.GameRepository;

public final class DefaultGameRepository implements GameRepository {

    private final Object engineLock = new Object();

    private final HighScoreLocalDataSource highScoreLocalDataSource;

    private GameEngine gameEngine;

    public DefaultGameRepository(@NonNull HighScoreLocalDataSource highScoreLocalDataSource) {
        this.highScoreLocalDataSource = highScoreLocalDataSource;
    }

    @NonNull
    @Override
    public EngineResult createGame() {
        synchronized (engineLock) {
            GameConfig config = GameConfig.getInstance();

            gameEngine = new GameEngine(
                    config.getBoardRows(),
                    config.getBoardColumns(),
                    new TetrominoFactory(),
                    new SevenBagPieceProvider()
            );

            return gameEngine.snapshot();
        }
    }

    @NonNull
    @Override
    public EngineResult tick() {
        synchronized (engineLock) {
            return requireEngine().tick();
        }
    }

    @NonNull
    @Override
    public EngineResult perform(@NonNull GameAction action) {
        synchronized (engineLock) {
            return requireEngine().perform(action);
        }
    }

    @Override
    public int getHighScore() {
        return highScoreLocalDataSource.getHighScore();
    }

    @Override
    public int saveHighScore(int score) {
        return highScoreLocalDataSource.saveIfHigher(score);
    }

    @NonNull
    private GameEngine requireEngine() {
        if (gameEngine == null) {
            throw new IllegalStateException("Game has not been created");
        }

        return gameEngine;
    }
}
