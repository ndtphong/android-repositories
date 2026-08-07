package com.ndtphong.simpletetrisgame.domain.strategy;

public interface ScoreStrategy {

    int calculateLineScore(int clearedLines, int level);

    int calculateDropScore(int softDropCells, int hardDropCells);
}
