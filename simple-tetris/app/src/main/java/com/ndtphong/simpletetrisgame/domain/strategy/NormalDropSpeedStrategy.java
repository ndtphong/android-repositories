package com.ndtphong.simpletetrisgame.domain.strategy;

public final class NormalDropSpeedStrategy implements DropSpeedStrategy {

    @Override
    public long getDelayMillis(int level) {
        return Math.max(100L, 1000L - ((long) level - 1L) * 75L);
    }
}
