package com.ndtphong.simpletetrisgame.infrastructure.runtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ndtphong.simpletetrisgame.domain.runtime.GameLoop;

public final class ThreadGameLoop implements GameLoop {

    private final Object stateLock = new Object();

    private final long frameDurationNanos;

    @Nullable
    private TickListener tickListener;

    @Nullable
    private Thread thread;

    private boolean running;
    private boolean paused = true;

    private long dropDelayMillis = 1000L;
    private long clockVersion;

    public ThreadGameLoop(int targetFps) {
        frameDurationNanos = 1_000_000_000L / Math.max(1, targetFps);
    }

    @Override
    public void setTickListener(@NonNull TickListener listener) {
        synchronized (stateLock) {
            tickListener = listener;
        }
    }

    @Override
    public void start() {
        synchronized (stateLock) {
            running = true;
            paused = false;
            clockVersion++;

            if (thread == null
                    || !thread.isAlive()) {

                thread = new Thread(this::runLoop, "TetrisGameLoop");

                thread.start();
            } else {
                stateLock.notifyAll();
            }
        }
    }

    @Override
    public void pause() {
        synchronized (stateLock) {
            paused = true;
        }
    }

    @Override
    public void resume() {
        synchronized (stateLock) {
            if (!running) {
                return;
            }

            paused = false;
            clockVersion++;
            stateLock.notifyAll();
        }
    }

    @Override
    public void stop() {
        Thread currentThread;

        synchronized (stateLock) {
            running = false;
            paused = false;
            stateLock.notifyAll();
            currentThread = thread;
            thread = null;
        }

        if (currentThread != null) {
            currentThread.interrupt();
        }
    }

    public void setDropDelayMillis(long delayMillis) {
        synchronized (stateLock) {
            dropDelayMillis = Math.max(20L, delayMillis);
            clockVersion++;
        }
    }

    private void runLoop() {
        long lastDropNanos = System.nanoTime();
        long localClockVersion = -1L;

        while (true) {
            long delayNanos;
            long currentClockVersion;
            TickListener listener;

            synchronized (stateLock) {
                // Is in pausing
                while (running && paused) {
                    try {
                        stateLock.wait();
                    } catch (InterruptedException ignored) {
                        if (!running) {
                            return;
                        }
                    }
                }

                if (!running) {
                    return;
                }

                delayNanos = dropDelayMillis * 1_000_000L;
                currentClockVersion = clockVersion;

                listener = tickListener;
            }

            long frameStartNanos = System.nanoTime();

            if (localClockVersion != currentClockVersion) {
                localClockVersion = currentClockVersion;
                lastDropNanos = frameStartNanos;
            }

            if (frameStartNanos - lastDropNanos >= delayNanos) {

                if (listener != null) {
                    listener.onDropTick();
                }

                lastDropNanos = frameStartNanos;
            }

            sleepUntilNextFrame(frameStartNanos);
        }
    }

    private void sleepUntilNextFrame(long frameStartNanos) {
        long elapsedNanos = System.nanoTime() - frameStartNanos;

        long remainingNanos = frameDurationNanos - elapsedNanos;

        if (remainingNanos <= 0L) {
            return;
        }

        long millis = remainingNanos / 1_000_000L;
        int nanos = (int) (remainingNanos % 1_000_000L);

        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
