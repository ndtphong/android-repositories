package com.ndtphong.simpletetrisgame.domain.strategy;

public final class ClassicScoreStrategy implements ScoreStrategy{
    @Override
    public int calculateLineScore(int clearedLines, int level) {
        int baseScore = switch (clearedLines) {
            case 0 -> 0;
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 1000;
        };

        return baseScore * Math.max(1, level);
    }

    @Override
    public int calculateDropScore(int softDropCells, int hardDropCells) {
        return softDropCells + hardDropCells * 2;
    }
}
