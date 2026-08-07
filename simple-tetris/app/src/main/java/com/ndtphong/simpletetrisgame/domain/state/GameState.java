package com.ndtphong.simpletetrisgame.domain.state;

public interface GameState {
    void start(GameStateContext context);

    void pause(GameStateContext context);

    void resume(GameStateContext context);

    void gameOver(GameStateContext context);
}
