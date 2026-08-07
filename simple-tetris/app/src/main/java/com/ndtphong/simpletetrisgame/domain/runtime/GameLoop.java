package com.ndtphong.simpletetrisgame.domain.runtime;

public interface GameLoop {

    interface TickListener {
        void onDropTick();
    }

    void setTickListener(TickListener listener);

    void start();

    void pause();

    void resume();

    void stop();

    void setDropDelayMillis(long delayMillis);
}
