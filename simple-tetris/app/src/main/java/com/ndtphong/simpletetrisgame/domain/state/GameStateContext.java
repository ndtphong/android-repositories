package com.ndtphong.simpletetrisgame.domain.state;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.GameStatus;

public final class GameStateContext {
    public interface Listener {
        void onStatusChanged(@NonNull GameStatus status);
    }

    private final Listener listener;
    private GameState state = new IdleState();

    public GameStateContext(@NonNull Listener listener) {
        this.listener = listener;
    }

    public void start() {
        state.start(this);
    }

    public void pause() {
        state.pause(this);
    }

    public void resume() {
        state.resume(this);
    }

    public void gameOver() {
        state.gameOver(this);
    }

    void changeState(
            @NonNull GameState newState,
            @NonNull GameStatus status
    ) {
        state = newState;
        listener.onStatusChanged(status);
    }
}
