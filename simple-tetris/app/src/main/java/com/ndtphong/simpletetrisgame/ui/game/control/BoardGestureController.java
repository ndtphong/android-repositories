package com.ndtphong.simpletetrisgame.ui.game.control;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;

import java.util.function.Consumer;

public final class BoardGestureController implements View.OnTouchListener {

    private static final float SWIPE_DISTANCE = 80f;
    private static final float SWIPE_VELOCITY = 80f;

    private final GestureDetector detector;

    @Nullable
    private View targetView;

    public BoardGestureController(
            @NonNull Context context,
            @NonNull Consumer<GameAction> actionConsumer
    ) {
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                if (targetView != null) {
                    targetView.performClick();
                }

                return true;
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                actionConsumer.accept(GameAction.HARD_DROP);
                return true;
            }

            @Override
            public boolean onFling(@Nullable MotionEvent first,
                                   @NonNull MotionEvent second,
                                   float velocityX,
                                   float velocityY
            ) {
                if (first == null) {
                    return false;
                }

                float distanceX = second.getX() - first.getX();
                float distanceY = second.getY() - first.getY();

                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if (Math.abs(distanceX) < SWIPE_DISTANCE
                            || Math.abs(velocityX) < SWIPE_VELOCITY) {
                        return false;
                    }

                    actionConsumer.accept(
                            distanceX > 0
                                    ? GameAction.MOVE_RIGHT
                                    : GameAction.MOVE_LEFT
                    );
                    return true;
                }

                if (distanceY > SWIPE_DISTANCE && Math.abs(velocityY) > SWIPE_VELOCITY) {
                    actionConsumer.accept(GameAction.SOFT_DROP);
                    return true;
                }

                return false;
            }
        };
        detector = new GestureDetector(context, gestureListener);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        targetView = view;
        return detector.onTouchEvent(motionEvent);
    }
}
