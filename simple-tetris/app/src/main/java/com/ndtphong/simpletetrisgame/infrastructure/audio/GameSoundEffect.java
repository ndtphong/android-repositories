package com.ndtphong.simpletetrisgame.infrastructure.audio;

public enum GameSoundEffect {

    START(
            new int[]{523, 659, 784},
            new int[]{70, 70, 130}
    ),

    MOVE(
            new int[]{440},
            new int[]{30}
    ),

    ROTATE(
            new int[]{660},
            new int[]{55}
    ),

    SOFT_DROP(
            new int[]{300},
            new int[]{30}
    ),

    HARD_DROP(
            new int[]{180, 120},
            new int[]{60, 110}
    ),

    LINE_CLEAR(
            new int[]{660, 880, 1100},
            new int[]{70, 70, 140}
    ),

    GAME_OVER(
            new int[]{440, 330, 220},
            new int[]{130, 130, 260}
    );

    private final int[] frequencies;
    private final int[] durations;

    GameSoundEffect(
            int[] frequencies,
            int[] durations
    ) {
        this.frequencies = frequencies;
        this.durations = durations;
    }

    public int[] frequencies() {
        return frequencies.clone();
    }

    public int[] durations() {
        return durations.clone();
    }
}
