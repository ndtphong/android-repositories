package com.ndtphong.simpletetrisgame.ui.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.ui.game.state.NextPieceUiState;

public final class NextPieceView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private NextPieceUiState nextPiece =
            new NextPieceUiState(
                    new int[][]{{1}},
                    R.color.tetromino_i
            );

    public NextPieceView(Context context) {
        this(context, null);
    }

    public NextPieceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL);
    }

    public void render(@NonNull NextPieceUiState nextPiece) {
        this.nextPiece = nextPiece;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int[][] shape = nextPiece.shape();

        int rows = shape.length;
        int columns = 0;

        for (int[] row : shape) {
            columns = Math.max(columns, row.length);
        }

        float cellSize = Math.min(
                (float) getWidth() / columns,
                (float) getHeight() / rows
        );

        float pieceWidth = columns * cellSize;
        float pieceHeight = rows * cellSize;

        float startX = (getWidth() - pieceWidth) / 2f;
        float startY = (getHeight() - pieceHeight) / 2f;

        paint.setColor(
                ContextCompat.getColor(
                        getContext(),
                        nextPiece.colorResource()
                )
        );

        float inset = dp(2);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < shape[row].length; column++) {
                if (shape[row][column] == 0) {
                    continue;
                }

                rect.set(
                        startX + column * cellSize + inset,
                        startY + row * cellSize + inset,
                        startX + (column + 1) * cellSize - inset,
                        startY + (row + 1) * cellSize - inset
                );

                canvas.drawRoundRect(rect, dp(4), dp(4), paint);
            }
        }
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }
}
