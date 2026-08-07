package com.ndtphong.simpletetrisgame.core;

public final class GameConfig {
    private static final class Holder {
        private static final GameConfig INSTANCE = new GameConfig();
    }

    private static final int boardColumns = 10;
    private static final int boardRows = 20;

    private GameConfig() {

    }

    public static GameConfig getInstance() {
        return Holder.INSTANCE;
    }

    public int getBoardColumns() {
        return boardColumns;
    }

    public int getBoardRows() {
        return boardRows;
    }
}
