package com.ndtphong.simpletetrisgame.domain.model;

public record GameResult(
        int score,
        int highScore,
        boolean newHighScore
) {
}
