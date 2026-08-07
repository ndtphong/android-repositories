package com.ndtphong.simpletetrisgame.domain.engine;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;
import com.ndtphong.simpletetrisgame.domain.model.Piece;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class GameEngineMovementTest {
    private GameEngine engine;

    @Before
    public void setUp() {
        engine = new GameEngine(
                20,
                10,
                new TetrominoFactory(),
                new FixedPieceProvider(TetrominoType.I)
        );
    }

    @Test
    public void moveLeft_stopsAtBoardBoundary() {
        for (int index = 0; index < 20; index++) {
            engine.perform(GameAction.MOVE_LEFT);
        }

        Piece piece = engine.snapshot()
                .board()
                .activePiece();

        Assert.assertNotNull(piece);
        Assert.assertEquals(0, piece.column());
    }

    @Test
    public void moveRight_stopsAtBoardBoundary() {
        for (int index = 0; index < 20; index++) {
            engine.perform(GameAction.MOVE_RIGHT);
        }

        Piece piece = engine.snapshot()
                .board()
                .activePiece();

        Assert.assertNotNull(piece);
        Assert.assertEquals(6, piece.column());
    }

    @Test
    public void rotateI_createsVerticalShape() {
        engine.perform(GameAction.ROTATE);

        Piece piece = engine.snapshot()
                .board()
                .activePiece();

        Assert.assertNotNull(piece);
        Assert.assertEquals(4, piece.shape().length);
        Assert.assertEquals(1, piece.shape()[0].length);
    }
}
