package com.ndtphong.simpletetrisgame.domain.engine;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import org.junit.Test;

public final class GameEngineLineClearTest {
    @Test
    public void hardDropO_clearsTwoLines() {
        TetrominoType[][] cells = new TetrominoType[20][10];

        for (int row = 18; row <= 19; row++) {
            for (int column = 0;
                 column < 10;
                 column++) {

                if (column != 4 && column != 5) {
                    cells[row][column] = TetrominoType.J;
                }
            }
        }

        GameEngine engine = new GameEngine(
                20,
                10,
                new TetrominoFactory(),
                new FixedPieceProvider(
                        TetrominoType.O,
                        TetrominoType.T
                ),
                cells
        );

        EngineResult result =
                engine.perform(GameAction.HARD_DROP);

        assertEquals(2, result.clearedLines());

        assertArrayEquals(
                new int[]{19, 18},
                result.clearedRows()
        );

        TetrominoType[][] board = result.board().lockedCells();

        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 10; column++) {
                assertNull(board[row][column]);
            }
        }
    }
}
