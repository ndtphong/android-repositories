package com.ndtphong.simpletetrisgame.domain.engine;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import org.junit.Test;

public final class GameEngineCollisionTest {
    @Test
    public void softDrop_collisionLocksPiece() {
        TetrominoType[][] cells = new TetrominoType[20][10];

        cells[2][4] = TetrominoType.Z;
        cells[2][5] = TetrominoType.Z;

        GameEngine engine = new GameEngine(
                20,
                10,
                new TetrominoFactory(),
                new FixedPieceProvider(TetrominoType.O),
                cells
        );

        EngineResult result = engine.perform(GameAction.SOFT_DROP);

        assertTrue(result.pieceLocked());

        TetrominoType[][] locked = result.board().lockedCells();

        assertNotEquals(null, locked[0][4]);
        assertNotEquals(null, locked[0][5]);
        assertNotEquals(null, locked[1][4]);
        assertNotEquals(null, locked[1][5]);
    }
}
