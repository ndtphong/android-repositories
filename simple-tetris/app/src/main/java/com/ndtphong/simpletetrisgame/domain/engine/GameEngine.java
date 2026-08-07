package com.ndtphong.simpletetrisgame.domain.engine;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.factory.PieceProvider;
import com.ndtphong.simpletetrisgame.domain.factory.TetrominoFactory;
import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.domain.model.Piece;
import com.ndtphong.simpletetrisgame.domain.model.Tetromino;
import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import java.util.Arrays;

public final class GameEngine {

    private final Object boardLock = new Object();

    private final int rows;
    private final int columns;
    private final TetrominoType[][] lockedCells;

    private final TetrominoFactory factory;
    private final PieceProvider pieceProvider;

    private Piece activePiece;
    private Tetromino nextTetromino;
    private boolean gameOver;

    public GameEngine(
            int rows,
            int columns,
            @NonNull TetrominoFactory factory,
            @NonNull PieceProvider pieceProvider
    ) {
        this(
                rows,
                columns,
                factory,
                pieceProvider,
                new TetrominoType[rows][columns]
        );
    }

    GameEngine(
            int rows,
            int columns,
            @NonNull TetrominoFactory factory,
            @NonNull PieceProvider pieceProvider,
            @NonNull TetrominoType[][] initialLockedCells
    ) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Invalid board size");
        }

        if (initialLockedCells.length != rows) {
            throw new IllegalArgumentException("Invalid locked cell rows");
        }

        this.rows = rows;
        this.columns = columns;
        this.factory = factory;
        this.pieceProvider = pieceProvider;
        this.lockedCells = copyLockedCells(
                initialLockedCells,
                rows,
                columns
        );

        nextTetromino = createNextTetromino();
        spawnPiece();
    }

    @NonNull
    public EngineResult perform(@NonNull GameAction action) {
        synchronized (boardLock) {
            if (gameOver) {
                return result(new int[0], 0, 0, false);
            }

            return switch (action) {
                case MOVE_LEFT -> moveHorizontal(-1);
                case MOVE_RIGHT -> moveHorizontal(1);
                case ROTATE -> rotateClockwise();
                case SOFT_DROP -> moveDown(true);
                case HARD_DROP -> hardDrop();
            };
        }
    }

    @NonNull
    public EngineResult tick() {
        synchronized (boardLock) {
            return moveDown(false);
        }
    }

    @NonNull
    public EngineResult snapshot() {
        synchronized (boardLock) {
            return result(new int[0], 0, 0, false);
        }
    }

    @NonNull
    private EngineResult moveHorizontal(int offset) {
        if (activePiece == null) {
            return result(new int[0], 0, 0, false);
        }

        Piece moved = new Piece(
                activePiece.type(),
                activePiece.shape(),
                activePiece.row(),
                activePiece.column() + offset
        );

        if (canPlace(moved)) {
            activePiece = moved;
        }

        return result(new int[0], 0, 0, false);
    }

    @NonNull
    private EngineResult moveDown(boolean userSoftDrop) {
        if (activePiece == null || gameOver) {
            return result(new int[0], 0, 0, false);
        }

        Piece moved = new Piece(
                activePiece.type(),
                activePiece.shape(),
                activePiece.row() + 1,
                activePiece.column()
        );

        if (canPlace(moved)) {
            activePiece = moved;

            return result(
                    new int[0],
                    userSoftDrop ? 1 : 0,
                    0,
                    false
            );
        }

        return lockPiece(0);
    }

    @NonNull
    private EngineResult rotateClockwise() {
        if (activePiece == null) {
            return result(new int[0], 0, 0, false);
        }

        int[][] rotatedShape = rotate(activePiece.shape());

        int[][] kickOffsets = {
                {0, 0},
                {0, -1},
                {0, 1},
                {0, -2},
                {0, 2},
                {-1, 0}
        };

        for (int[] kick : kickOffsets) {
            Piece rotated = new Piece(
                    activePiece.type(),
                    rotatedShape,
                    activePiece.row() + kick[0],
                    activePiece.column() + kick[1]
            );

            if (canPlace(rotated)) {
                activePiece = rotated;
                break;
            }
        }

        return result(new int[0], 0, 0, false);
    }

    @NonNull
    private EngineResult hardDrop() {
        if (activePiece == null) {
            return result(new int[0], 0, 0, false);
        }

        int startRow = activePiece.row();
        int destinationRow = calculateGhostRow(activePiece);
        int droppedCells = Math.max(0, destinationRow - startRow);

        activePiece = new Piece(
                activePiece.type(),
                activePiece.shape(),
                destinationRow,
                activePiece.column()
        );

        return lockPiece(droppedCells);
    }

    @NonNull
    private EngineResult lockPiece(int hardDropCells) {
        if (activePiece == null) {
            return result(new int[0], 0, 0, false);
        }

        int[][] shape = activePiece.shape();

        for (int row = 0; row < shape.length; row++) {
            for (int column = 0; column < shape[row].length; column++) {

                if (shape[row][column] == 0) {
                    continue;
                }

                int boardRow = activePiece.row() + row;
                int boardColumn = activePiece.column() + column;

                if (boardRow >= 0
                        && boardRow < rows
                        && boardColumn >= 0
                        && boardColumn < columns) {

                    lockedCells[boardRow][boardColumn] = activePiece.type();
                }
            }
        }

        int[] clearedRows = clearCompletedLines();

        activePiece = null;
        spawnPiece();

        return result(
                clearedRows,
                0,
                hardDropCells,
                true
        );
    }

    private void spawnPiece() {
        Tetromino current = nextTetromino;
        nextTetromino = createNextTetromino();

        int[][] shape = current.shape();

        int startColumn = (columns - shape[0].length) / 2;

        Piece spawned = new Piece(
                current.type(),
                shape,
                0,
                startColumn
        );

        if (!canPlace(spawned)) {
            activePiece = null;
            gameOver = true;
            return;
        }

        activePiece = spawned;
    }

    @NonNull
    private int[] clearCompletedLines() {
        int[] clearedRows = new int[rows];
        int clearedCount = 0;
        int writeRow = rows - 1;

        for (int readRow = rows - 1; readRow >= 0; readRow--) {

            if (isCompletedLine(readRow)) {
                clearedRows[clearedCount++] = readRow;
                continue;
            }

            if (writeRow != readRow) {
                System.arraycopy(
                        lockedCells[readRow],
                        0,
                        lockedCells[writeRow],
                        0,
                        columns
                );
            }

            writeRow--;
        }

        while (writeRow >= 0) {
            Arrays.fill(lockedCells[writeRow], null);
            writeRow--;
        }

        return Arrays.copyOf(clearedRows, clearedCount);
    }

    private boolean isCompletedLine(int row) {
        for (int column = 0; column < columns; column++) {
            if (lockedCells[row][column] == null) {
                return false;
            }
        }

        return true;
    }

    private boolean canPlace(@NonNull Piece piece) {
        int[][] shape = piece.shape();

        for (int row = 0; row < shape.length; row++) {
            for (int column = 0; column < shape[row].length; column++) {

                if (shape[row][column] == 0) {
                    continue;
                }

                int boardRow = piece.row() + row;
                int boardColumn = piece.column() + column;

                if (boardColumn < 0
                        || boardColumn >= columns
                        || boardRow >= rows) {
                    return false;
                }

                if (boardRow >= 0 && lockedCells[boardRow][boardColumn] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    private int calculateGhostRow(@NonNull Piece piece) {
        int ghostRow = piece.row();

        while (true) {
            Piece ghostPiece = new Piece(
                    piece.type(),
                    piece.shape(),
                    ghostRow + 1,
                    piece.column()
            );

            if (!canPlace(ghostPiece)) {
                break;
            }

            ghostRow++;
        }

        return ghostRow;
    }

    private int[][] rotate(@NonNull int[][] shape) {
        int rows = shape.length;
        int columns = shape[0].length;

        int[][] rotated = new int[columns][rows];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                // square 3x3
                rotated[column][rows - 1 - row] = shape[row][column];
            }
        }

        return rotated;
    }

    @NonNull
    private Tetromino createNextTetromino() {
        return factory.create(pieceProvider.next());
    }

    @NonNull
    private EngineResult result(
            @NonNull int[] clearedRows,
            int softDropCells,
            int hardDropCells,
            boolean pieceLocked
    ) {
        int ghostRow = activePiece == null
                ? -1
                : calculateGhostRow(activePiece);

        BoardState board = new BoardState(
                rows,
                columns,
                lockedCells,
                activePiece,
                ghostRow
        );

        return new EngineResult(
                board,
                nextTetromino,
                clearedRows,
                softDropCells,
                hardDropCells,
                pieceLocked,
                gameOver
        );
    }

    @NonNull
    private static TetrominoType[][] copyLockedCells(
            @NonNull TetrominoType[][] source,
            int rows,
            int columns
    ) {
        if (source.length != rows) {
            throw new IllegalArgumentException("Invalid locked cell rows");
        }

        TetrominoType[][] result = new TetrominoType[rows][columns];

        for (int row = 0; row < rows; row++) {
            if (source[row].length != columns) {
                throw new IllegalArgumentException("Invalid locked cell columns");
            }

            System.arraycopy(source[row], 0, result[row], 0, columns);
        }

        return result;
    }

}
