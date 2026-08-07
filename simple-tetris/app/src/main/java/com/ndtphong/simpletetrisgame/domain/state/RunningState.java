package com.ndtphong.simpletetrisgame.domain.state;

import com.ndtphong.simpletetrisgame.domain.model.GameStatus;

public final class RunningState implements GameState {
    @Override
    public void start(GameStateContext context) {

    }

    @Override
    public void pause(GameStateContext context) {
        context.changeState(new PausedState(), GameStatus.PAUSED);
    }

    @Override
    public void resume(GameStateContext context) {

    }

    @Override
    public void gameOver(GameStateContext context) {
        context.changeState(new GameOverState(), GameStatus.GAME_OVER);
    }
}
