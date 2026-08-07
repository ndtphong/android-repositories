package com.ndtphong.simpletetrisgame.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.TetrisApplication;
import com.ndtphong.simpletetrisgame.di.AppContainer;
import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.model.GameStatus;
import com.ndtphong.simpletetrisgame.infrastructure.audio.GameSoundEffect;
import com.ndtphong.simpletetrisgame.infrastructure.audio.SynthTetrisAudioEngine;
import com.ndtphong.simpletetrisgame.infrastructure.audio.TetrisAudioEngine;
import com.ndtphong.simpletetrisgame.ui.game.animation.GameAnimationEvent;
import com.ndtphong.simpletetrisgame.ui.game.control.BoardGestureController;
import com.ndtphong.simpletetrisgame.ui.game.view.NextPieceView;
import com.ndtphong.simpletetrisgame.ui.game.view.TetrisBoardView;
import com.ndtphong.simpletetrisgame.ui.main.MainViewModel;
import com.ndtphong.simpletetrisgame.ui.main.Screen;

import java.util.List;

public class GameFragment extends Fragment {

    private GameViewModel viewModel;
    private MainViewModel mainViewModel;

    private TetrisAudioEngine audioEngine;

    private final Handler animationHandler = new Handler(Looper.getMainLooper());

    @Nullable
    private Runnable resultNavigationRunnable;

    private boolean animationEnabled = true;
    private boolean gameOverNavigationScheduled;

    public GameFragment() {
        super(R.layout.fragment_game);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        AppContainer container = TetrisApplication.from(requireContext()).appContainer();

        viewModel = new ViewModelProvider(
                this,
                new GameViewModelFactory(container)
        ).get(GameViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        audioEngine = new SynthTetrisAudioEngine();

        GameUiState initialState = viewModel.getUiState().getValue();

        if (initialState != null) {
            audioEngine.setEnabled(initialState.soundEnabled());
            animationEnabled = initialState.animationEnabled();
        }

        TextView score = view.findViewById(R.id.text_score);
        TextView lines = view.findViewById(R.id.text_lines);
        TextView level = view.findViewById(R.id.text_level);
        TextView highScore = view.findViewById(R.id.text_high_score);
        TextView gameOverOverlay = view.findViewById(R.id.text_game_over_overlay);

        TetrisBoardView boardView = view.findViewById(R.id.view_board);
        NextPieceView nextPieceView = view.findViewById(R.id.view_next_piece);

        Button finishBtn = view.findViewById(R.id.button_finish);
        Button backBtn = view.findViewById(R.id.button_back);

        ImageButton leftBtn = view.findViewById(R.id.button_left);
        ImageButton rightBtn = view.findViewById(R.id.button_right);
        ImageButton rotateBtn = view.findViewById(R.id.button_rotate);
        ImageButton softDropBtn = view.findViewById(R.id.button_soft_drop);
        ImageButton hardDropBtn = view.findViewById(R.id.button_hard_drop);

        viewModel.getUiState().observe(
                getViewLifecycleOwner(),
                state -> {
                    if (state.status() == GameStatus.GAME_OVER) {
                        navigateGameOver();
                    }

                    score.setText(getString(R.string.score_value, state.score()));
                    lines.setText(getString(R.string.lines_value, state.lines()));
                    level.setText(getString(R.string.level_value, state.level()));
                    highScore.setText(getString(R.string.high_score_value, state.highScore()));

                    boardView.render(state.board());
                    nextPieceView.render(state.nextPiece());

                    if (!state.animations().isEmpty()) {
                        playAnimations(
                                boardView,
                                gameOverOverlay,
                                state.animations()
                        );

                        viewModel.consumeAnimations(state.animationBatchId());
                    }

                    if (state.status() == GameStatus.GAME_OVER && !gameOverNavigationScheduled) {
                        gameOverNavigationScheduled = true;

                        resultNavigationRunnable = () -> {
                                    if (!isAdded()|| getView() == null) {
                                        return;
                                    }

                                    mainViewModel.navigateTo(Screen.RESULT);
                                };

                        animationHandler.postDelayed(resultNavigationRunnable, 650L);
                    }
                }
        );

        finishBtn.setOnClickListener(v -> {
                    viewModel.finishGame();
                    navigateGameOver();
                }
        );

        backBtn.setOnClickListener(v -> {
                    viewModel.pauseGame();
                    navigateGameOver();
                }
        );

        leftBtn.setOnClickListener(v ->
                performAction(GameAction.MOVE_LEFT)
        );

        rightBtn.setOnClickListener(v ->
                performAction(GameAction.MOVE_RIGHT)
        );

        rotateBtn.setOnClickListener(v ->
                performAction(GameAction.ROTATE)
        );

        softDropBtn.setOnClickListener(v ->
                performAction(GameAction.SOFT_DROP)
        );

        hardDropBtn.setOnClickListener(v ->
                performAction(GameAction.HARD_DROP)
        );

        boardView.setOnTouchListener(
                new BoardGestureController(
                        requireContext(),
                        this::performAction
                )
        );
        boardView.setOnClickListener(v ->
                viewModel.performAction(GameAction.ROTATE)
        );

        viewModel.startGame();

        if (audioEngine != null) {
            audioEngine.play(GameSoundEffect.START);
            audioEngine.startMusic();
        }
    }

    private void performAction(@NonNull GameAction action) {
        if (!viewModel.isRunning()) {
            return;
        }

        switch (action) {
            case MOVE_LEFT, MOVE_RIGHT -> audioEngine.play(GameSoundEffect.MOVE);
            case ROTATE -> audioEngine.play(GameSoundEffect.ROTATE);
            case SOFT_DROP -> audioEngine.play(GameSoundEffect.SOFT_DROP);
            case HARD_DROP -> {
            }
        }

        viewModel.performAction(action);
    }

    private void navigateGameOver() {
        resultNavigationRunnable = () -> {
            if (!isAdded() || getView() == null) {
                return;
            }

            mainViewModel.navigateTo(Screen.RESULT);
        };

        animationHandler.postDelayed(
                resultNavigationRunnable,
                650L
        );
    }

    private void playAnimations(
            @NonNull TetrisBoardView board,
            @NonNull TextView gameOverOverlay,
            @NonNull List<GameAnimationEvent> events
    ) {
        long delay = 0L;

        for (GameAnimationEvent event : events) {
            animationHandler.postDelayed(
                    () -> {
                        if (!isAdded() || getView() == null || audioEngine == null) {
                            return;
                        }

                        switch (event.type()) {
                            case HARD_DROP -> {
                                audioEngine.play(GameSoundEffect.HARD_DROP);

                                if (animationEnabled) {
                                    board.animateHardDrop();
                                }
                            }

                            case LINE_CLEAR -> {
                                audioEngine.play(GameSoundEffect.LINE_CLEAR);

                                if (animationEnabled) {
                                    board.animateLineClear(event.rows());
                                }
                            }

                            case PIECE_SPAWN -> {
                                if (animationEnabled) {
                                    board.animatePieceSpawn();
                                }
                            }

                            case GAME_OVER -> {
                                audioEngine.stopMusic();

                                audioEngine.play(GameSoundEffect.GAME_OVER);

                                if (animationEnabled) {
                                    gameOverOverlay.setVisibility(View.VISIBLE);

                                    gameOverOverlay.startAnimation(
                                            AnimationUtils.loadAnimation(requireContext(), R.anim.game_over_in)
                                    );
                                }
                            }
                        }
                    },
                    delay
            );

            delay += switch (event.type()) {
                case HARD_DROP -> 130L;
                case LINE_CLEAR -> 310L;
                case PIECE_SPAWN -> 170L;
                case GAME_OVER -> 350L;
            };
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewModel != null) {
            viewModel.resumeGame();
        }

        if (audioEngine != null) {
            audioEngine.startMusic();
        }
    }

    @Override
    public void onPause() {
        if (audioEngine != null) {
            audioEngine.stopMusic();
        }

        if (viewModel != null) {
            viewModel.pauseGame();
        }

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        animationHandler.removeCallbacksAndMessages(null);
        resultNavigationRunnable = null;
        gameOverNavigationScheduled = false;

        if (audioEngine != null) {
            audioEngine.release();
            audioEngine = null;
        }

        super.onDestroyView();
    }
}
