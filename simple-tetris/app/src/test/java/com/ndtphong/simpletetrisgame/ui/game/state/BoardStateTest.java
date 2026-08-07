package com.ndtphong.simpletetrisgame.ui.game.state;

import static org.junit.Assert.assertEquals;

import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import org.junit.Test;

public final class BoardStateTest {
    @Test
    public void constructor_copiesLockedCells() {
        TetrominoType[][] source = new TetrominoType[20][10];
        source[5][5] = TetrominoType.T;

        BoardState state = new BoardState(
                20,
                10,
                source,
                null,
                -1
        );

        source[5][5] = TetrominoType.O;

        assertEquals(TetrominoType.T, state.lockedCells()[5][5]);
    }

    @Test
    public void getter_returnsCopy() {
        TetrominoType[][] source = new TetrominoType[20][10];
        source[5][5] = TetrominoType.O;

        BoardState state = new BoardState(
                20,
                10,
                source,
                null,
                -1
        );

        TetrominoType[][] returned = state.lockedCells();
        returned[5][5] = TetrominoType.S;

        assertEquals(TetrominoType.O, state.lockedCells()[5][5]);
    }
}
