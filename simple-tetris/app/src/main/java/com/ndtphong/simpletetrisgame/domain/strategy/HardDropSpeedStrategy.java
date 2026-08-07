package com.ndtphong.simpletetrisgame.domain.strategy;

public final class HardDropSpeedStrategy implements DropSpeedStrategy{
    @Override
    public long getDelayMillis(int level) {
        return 20L;
    }
}
