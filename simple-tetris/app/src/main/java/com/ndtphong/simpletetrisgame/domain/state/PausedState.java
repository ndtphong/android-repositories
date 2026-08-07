package com.ndtphong.simpletetrisgame.domain.state;

import com.ndtphong.simpletetrisgame.domain.model.GameStatus;

public final class PausedState implements GameState {
    @Override
    public void start(GameStateContext context) {

    }

    @Override
    public void pause(GameStateContext context) {

    }

    @Override
    public void resume(GameStateContext context) {
        context.changeState(new RunningState(), GameStatus.RUNNING);
    }

    @Override
    public void gameOver(GameStateContext context) {
        context.changeState(new GameOverState(), GameStatus.GAME_OVER);
    }
}
