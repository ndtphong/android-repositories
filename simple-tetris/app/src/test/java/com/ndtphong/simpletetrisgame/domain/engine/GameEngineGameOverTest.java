package com.ndtphong.simpletetrisgame.domain.engine;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import org.junit.Test;

public final class GameEngineGameOverTest {
    @Test
    public void blockedSpawn_setsGameOver() {
        TetrominoType[][] cells = new TetrominoType[20][10];

        cells[0][4] = TetrominoType.Z;
        cells[0][5] = TetrominoType.Z;

        GameEngine engine = new GameEngine(
                20,
                10,
                new TetrominoFactory(),
                new FixedPieceProvider(TetrominoType.O),
                cells
        );

        EngineResult result = engine.snapshot();

        assertTrue(result.gameOver());
        assertNull(result.board().activePiece());
    }
}
