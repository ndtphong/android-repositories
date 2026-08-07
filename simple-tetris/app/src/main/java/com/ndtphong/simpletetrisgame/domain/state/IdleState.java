package com.ndtphong.simpletetrisgame.domain.state;

import com.ndtphong.simpletetrisgame.domain.model.GameStatus;

public final class IdleState implements GameState {
    @Override
    public void start(GameStateContext context) {
        context.changeState(new RunningState(), GameStatus.RUNNING);
    }

    @Override
    public void pause(GameStateContext context) {

    }

    @Override
    public void resume(GameStateContext context) {

    }

    @Override
    public void gameOver(GameStateContext context) {
        context.changeState(new GameOverState(), GameStatus.GAME_OVER);
    }
}
