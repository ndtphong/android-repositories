package com.ndtphong.simpletetrisgame.ui.game.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.core.GameConfig;
import com.ndtphong.simpletetrisgame.domain.model.BoardState;
import com.ndtphong.simpletetrisgame.domain.model.Piece;
import com.ndtphong.simpletetrisgame.ui.game.state.BoardUiState;
import com.ndtphong.simpletetrisgame.ui.game.state.PieceUiState;

public final class TetrisBoardView extends View {

    private final Paint blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ghostPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF blockRect = new RectF();

    private int[] flashingRows = new int[0];
    private float lineFlashAlpha;

    private ValueAnimator lineClearAnimator;

    private BoardUiState board;

    private float cellSize;
    private float boardLeft;
    private float boardTop;

    public TetrisBoardView(Context context) {
        this(context, null);
    }

    public TetrisBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TetrisBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(dp(1));
        gridPaint.setColor(color(R.color.board_grid));

        ghostPaint.setStyle(Paint.Style.STROKE);
        ghostPaint.setStrokeWidth(dp(2));
        ghostPaint.setAlpha(130);

        blockPaint.setStyle(Paint.Style.FILL);

        GameConfig gameConfig = GameConfig.getInstance();
        board = BoardUiState.empty(gameConfig.getBoardRows(), gameConfig.getBoardColumns());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec);

        float ratio = (float) board.columns() / board.rows();

        int measuredWidth = availableWidth;
        int measuredHeight = Math.round(measuredWidth / ratio);

        if (measuredHeight > availableHeight) {
            measuredHeight = availableHeight;
            measuredWidth = Math.round(measuredHeight * ratio);
        }

        setMeasuredDimension(
                resolveSize(measuredWidth, widthMeasureSpec),
                resolveSize(measuredHeight, heightMeasureSpec)
        );
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        calculateBoardBounds();
        drawGrid(canvas);
        drawLockedCells(canvas);
        drawGhostPiece(canvas);
        drawActivePiece(canvas);
        drawLineFlash(canvas);
    }

    public void render(@NonNull BoardUiState board) {
        this.board = board;
        requestLayout();
        invalidate();
    }

    public void animateHardDrop() {
        animate().cancel();

        setTranslationY(0f);

        animate()
                .translationY(dp(8))
                .setDuration(45L)
                .withEndAction(() ->
                        animate()
                                .translationY(0f)
                                .setDuration(75L)
                                .start()
                )
                .start();
    }

    public void animatePieceSpawn() {
        animate().cancel();

        setScaleX(0.97f);
        setScaleY(0.97f);
        setAlpha(0.75f);

        animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(160L)
                .start();
    }

    public void animateLineClear(
            @NonNull int[] rows
    ) {
        if (lineClearAnimator != null) {
            lineClearAnimator.cancel();
        }

        flashingRows = rows.clone();

        lineClearAnimator = ValueAnimator.ofFloat(
                0f,
                1f,
                0f,
                1f,
                0f
        );

        lineClearAnimator.setDuration(300L);

        lineClearAnimator.addUpdateListener(
                animator -> {
                    lineFlashAlpha = (float) animator.getAnimatedValue();
                    invalidate();
                }
        );

        lineClearAnimator.start();
    }

    private void drawLineFlash(
            @NonNull Canvas canvas
    ) {
        if (lineFlashAlpha <= 0f) {
            return;
        }

        blockPaint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.line_clear_flash
                )
        );

        blockPaint.setAlpha(
                Math.round(255f * lineFlashAlpha)
        );

        for (int row : flashingRows) {
            if (row < 0 || row >= board.rows()) {
                continue;
            }

            float top = boardTop + row * cellSize;

            canvas.drawRect(
                    boardLeft,
                    top,
                    boardLeft + board.columns() * cellSize,
                    top + cellSize,
                    blockPaint
            );
        }

        blockPaint.setAlpha(255);
    }

    private void calculateBoardBounds() {
        float widthCell = (float) getWidth() / board.columns();
        float heightCell = (float) getHeight() / board.rows();

        cellSize = Math.min(widthCell, heightCell);

        float boardWidth = cellSize * board.columns();
        float boardHeight = cellSize * board.rows();

        boardLeft = (getWidth() - boardWidth) / 2f;
        boardTop = (getHeight() - boardHeight) / 2f;
    }

    private void drawGrid(@NonNull Canvas canvas) {
        for (int column = 0; column < board.columns(); column++) {
            float x = boardLeft + column * cellSize;

            canvas.drawLine(
                    x,
                    boardTop,
                    x,
                    boardTop + board.rows() * cellSize,
                    gridPaint
            );
        }

        for (int row = 0; row < board.rows(); row++) {
            float y = boardTop + row * cellSize;

            canvas.drawLine(
                    boardLeft,
                    y,
                    boardLeft + board.columns() * cellSize,
                    y,
                    gridPaint
            );
        }
    }

    private void drawLockedCells(@NonNull Canvas canvas) {
        int[][] cells = board.lockedCells();

        for (int row = 0; row < board.rows(); row++) {
            for (int column = 0; column < board.columns(); column++) {
                int colorResource = cells[row][column];

                if (colorResource != 0) {
                    drawBlock(canvas, row, column, color(colorResource), false);
                }
            }
        }
    }

    private void drawActivePiece(@NonNull Canvas canvas) {
        PieceUiState piece = board.activePiece();

        if (piece == null) {
            return;
        }

        drawPiece(
                canvas,
                piece.shape(),
                piece.row(),
                piece.column(),
                color(piece.colorResource()),
                false
        );
    }

    private void drawGhostPiece(@NonNull Canvas canvas) {
        PieceUiState piece = board.activePiece();

        if (piece == null || board.ghostRow() < 0) {
            return;
        }

        drawPiece(
                canvas,
                piece.shape(),
                board.ghostRow(),
                piece.column(),
                color(piece.colorResource()),
                true
        );
    }

    private void drawPiece(
            @NonNull Canvas canvas,
            @NonNull int[][] shape,
            int startRow,
            int startColumn,
            @ColorInt int blockColor,
            boolean ghost
    ) {
        for (int row = 0; row < shape.length; row++) {
            for (int column = 0; column < shape[row].length; column++) {
                if (shape[row][column] == 1) {
                    drawBlock(canvas, startRow + row, startColumn + column, blockColor, ghost);
                }
            }
        }
    }

    private void drawBlock(
            @NonNull Canvas canvas,
            int row,
            int column,
            @ColorInt int blockColor,
            boolean ghost
    ) {
        if (row < 0
                || row >= board.rows()
                || column < 0
                || column >= board.columns()) {
            return;
        }

        float inset = dp(2);

        float left = boardLeft + column * cellSize + inset;
        float top = boardTop + row * cellSize + inset;
        float right = boardLeft + (column + 1) * cellSize - inset;
        float bottom = boardTop + (row + 1) * cellSize - inset;

        blockRect.set(left, top, right, bottom);

        if (ghost) {
            ghostPaint.setColor(blockColor / 2);
            canvas.drawRoundRect(
                    blockRect,
                    dp(4),
                    dp(4),
                    ghostPaint
            );
        } else {
            blockPaint.setColor(blockColor);

            canvas.drawRoundRect(
                    blockRect,
                    dp(4),
                    dp(4),
                    blockPaint
            );
        }
    }

    @ColorInt
    private int color(int colorResource) {
        return ContextCompat.getColor(getContext(), colorResource);
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(null);

        if (lineClearAnimator != null) {
            lineClearAnimator.removeAllUpdateListeners();
            lineClearAnimator.cancel();
            lineClearAnimator = null;
        }

        animate().cancel();
        clearAnimation();
        super.onDetachedFromWindow();
    }
}
