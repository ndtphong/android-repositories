package com.ndtphong.simpletetrisgame.ui.game;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ndtphong.simpletetrisgame.domain.action.GameAction;
import com.ndtphong.simpletetrisgame.domain.engine.EngineResult;
import com.ndtphong.simpletetrisgame.domain.model.GameResult;
import com.ndtphong.simpletetrisgame.domain.model.GameSettings;
import com.ndtphong.simpletetrisgame.domain.model.GameStatus;
import com.ndtphong.simpletetrisgame.domain.repository.GameRepository;
import com.ndtphong.simpletetrisgame.domain.repository.ResultRepository;
import com.ndtphong.simpletetrisgame.domain.repository.SettingsRepository;
import com.ndtphong.simpletetrisgame.domain.runtime.GameLoop;
import com.ndtphong.simpletetrisgame.domain.state.GameStateContext;
import com.ndtphong.simpletetrisgame.domain.strategy.ClassicScoreStrategy;
import com.ndtphong.simpletetrisgame.domain.strategy.DropSpeedStrategy;
import com.ndtphong.simpletetrisgame.domain.strategy.NormalDropSpeedStrategy;
import com.ndtphong.simpletetrisgame.domain.strategy.ScoreStrategy;
import com.ndtphong.simpletetrisgame.ui.game.animation.GameAnimationEvent;
import com.ndtphong.simpletetrisgame.ui.game.mapper.BoardUiMapper;
import com.ndtphong.simpletetrisgame.ui.game.mapper.NextPieceUiMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class GameViewModel extends ViewModel {

    private final Object uiStateLock = new Object();
    private final Object operationLock = new Object();

    private final GameRepository gameRepository;
    private final ResultRepository resultRepository;

    private final GameLoop gameLoop;

    private final BoardUiMapper boardMapper;
    private final NextPieceUiMapper nextPieceMapper;

    private final ScoreStrategy scoreStrategy = new ClassicScoreStrategy();

    private final DropSpeedStrategy speedStrategy = new NormalDropSpeedStrategy();

    private final GameStateContext stateContext = new GameStateContext(this::updateStatus);

    private final MutableLiveData<GameUiState> mutableUiState = new MutableLiveData<>();

    private final int initialHighScore;

    private GameUiState uiState;
    private long animationBatchId;
    private boolean resultSaved;

    public GameViewModel(
            @NonNull GameRepository gameRepository,
            @NonNull SettingsRepository settingsRepository,
            @NonNull ResultRepository resultRepository,
            @NonNull GameLoop gameLoop,
            @NonNull BoardUiMapper boardMapper,
            @NonNull NextPieceUiMapper nextPieceMapper
    ) {
        this.gameRepository = gameRepository;
        this.resultRepository = resultRepository;
        this.gameLoop = gameLoop;
        this.boardMapper = boardMapper;
        this.nextPieceMapper = nextPieceMapper;

        EngineResult initial = gameRepository.createGame();

        GameSettings settings = settingsRepository.getSettings();

        initialHighScore = gameRepository.getHighScore();

        uiState = GameUiState.initial(
                boardMapper.map(initial.board()),
                nextPieceMapper.map(initial.nextPiece()),
                initialHighScore,
                settings
        );

        mutableUiState.setValue(uiState);

        gameLoop.setTickListener(this::onDropTick);

        gameLoop.setDropDelayMillis(uiState.dropDelayMillis());
    }

    public LiveData<GameUiState> getUiState() {
        return mutableUiState;
    }

    public void startGame() {
        if (currentState().status() != GameStatus.IDLE) {
            return;
        }

        stateContext.start();
        gameLoop.start();
    }

    public void pauseGame() {
        if (currentState().status() != GameStatus.RUNNING) {
            return;
        }

        gameLoop.pause();
        stateContext.pause();
    }

    public void resumeGame() {
        if (currentState().status() != GameStatus.PAUSED) {
            return;
        }

        stateContext.resume();
        gameLoop.resume();
    }

    public void performAction(@NonNull GameAction action) {
        if (currentState().status() != GameStatus.RUNNING) {
            return;
        }

        executeOperation(() ->
                gameRepository.perform(action)
        );
    }

    public void finishGame() {
        if (currentState().status() == GameStatus.GAME_OVER) {
            return;
        }

        gameLoop.pause();
        saveResult();
        stateContext.gameOver();
        publishManualGameOverAnimation();
    }

    public void consumeAnimations(long batchId) {
        updateUiState(current -> {
            if (current.animationBatchId() != batchId) {
                return current;
            }

            return current.clearAnimations();
        });
    }

    public boolean isRunning() {
        return currentState().status() == GameStatus.RUNNING;
    }

    private void onDropTick() {
        executeOperation(gameRepository::tick);
    }

    private void executeOperation(
            @NonNull Supplier<EngineResult> operation
    ) {
        synchronized (operationLock) {
            applyEngineResult(operation.get());
        }
    }

    private void applyEngineResult(@NonNull EngineResult result) {
        GameUiState updated;

        synchronized (uiStateLock) {
            int newLines = uiState.lines() + result.clearedLines();

            int newLevel = Math.max(1, newLines / 10 + 1);

            int lineScore = scoreStrategy.calculateLineScore(result.clearedLines(), uiState.level());

            int dropScore = scoreStrategy.calculateDropScore(
                    result.softDropCells(),
                    result.hardDropCells()
            );

            int newScore = uiState.score() + lineScore + dropScore;

            int newHighScore = Math.max(uiState.highScore(), newScore);

            long newDelay = speedStrategy.getDelayMillis(newLevel);

            List<GameAnimationEvent> animations = createAnimations(result);

            long batchId = animations.isEmpty() ? uiState.animationBatchId() : ++animationBatchId;

            uiState = uiState.withProgress(
                    boardMapper.map(result.board()),
                    nextPieceMapper.map(result.nextPiece()),
                    newScore,
                    newHighScore,
                    newLines,
                    newLevel,
                    newDelay,
                    batchId,
                    animations
            );

            updated = uiState;
        }

        gameLoop.setDropDelayMillis(updated.dropDelayMillis());

        mutableUiState.postValue(updated);

        if (result.gameOver()) {
            gameLoop.pause();
            saveResult();
            stateContext.gameOver();
        }
    }

    @NonNull
    private List<GameAnimationEvent> createAnimations(
            @NonNull EngineResult result
    ) {
        List<GameAnimationEvent> animations = new ArrayList<>();

        if (result.hardDropCells() > 0) {
            animations.add(GameAnimationEvent.hardDrop(result.hardDropCells()));
        }

        if (result.clearedLines() > 0) {
            animations.add(GameAnimationEvent.lineClear(result.clearedRows()));
        }

        if (result.gameOver()) {
            animations.add(GameAnimationEvent.gameOver());
        } else if (result.pieceLocked()) {
            animations.add(GameAnimationEvent.pieceSpawn());
        }

        return animations;
    }

    private void publishManualGameOverAnimation() {
        updateUiState(current ->
                current.withAnimations(
                        ++animationBatchId,
                        List.of(GameAnimationEvent.gameOver())
                )
        );
    }

    private void saveResult() {
        synchronized (uiStateLock) {
            if (resultSaved) {
                return;
            }

            resultSaved = true;
        }

        GameUiState current = currentState();

        int persistedHighScore = gameRepository.saveHighScore(currentState().score());

        resultRepository.saveResult(
                new GameResult(
                        current.score(),
                        persistedHighScore,
                        current.score() > initialHighScore
                )
        );

        updateUiState(state ->
                state.withHighScore(persistedHighScore)
        );
    }

    private void updateStatus(
            @NonNull GameStatus status
    ) {
        updateUiState(current ->
                current.withStatus(status)
        );
    }

    private void updateUiState(
            @NonNull UnaryOperator<GameUiState> reducer
    ) {
        GameUiState updatedState;

        synchronized (uiStateLock) {
            uiState = reducer.apply(uiState);
            updatedState = uiState;
        }

        mutableUiState.postValue(updatedState);
    }

    @NonNull
    private GameUiState currentState() {
        synchronized (uiStateLock) {
            return uiState;
        }
    }

    @Override
    protected void onCleared() {
        gameLoop.stop();
        super.onCleared();
    }
}
