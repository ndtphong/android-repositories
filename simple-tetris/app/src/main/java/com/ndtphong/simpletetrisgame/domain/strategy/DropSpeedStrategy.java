package com.ndtphong.simpletetrisgame.domain.strategy;

public interface DropSpeedStrategy {
    long getDelayMillis(int level);
}
