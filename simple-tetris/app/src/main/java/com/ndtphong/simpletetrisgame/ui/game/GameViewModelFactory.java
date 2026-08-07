package com.ndtphong.simpletetrisgame.ui.game;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.di.AppContainer;
import com.ndtphong.simpletetrisgame.ui.game.mapper.BoardUiMapper;
import com.ndtphong.simpletetrisgame.ui.game.mapper.NextPieceUiMapper;
import com.ndtphong.simpletetrisgame.ui.game.mapper.PieceUiMapper;

public final class GameViewModelFactory implements ViewModelProvider.Factory {

    private final AppContainer container;

    public GameViewModelFactory(
            @NonNull AppContainer container
    ) {
        this.container = container;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GameViewModel.class)) {

            PieceUiMapper pieceMapper = new PieceUiMapper();
            BoardUiMapper boardMapper = new BoardUiMapper(pieceMapper);
            NextPieceUiMapper nextPieceMapper = new NextPieceUiMapper(pieceMapper);

            return (T) new GameViewModel(
                    container.gameRepository(),
                    container.settingsRepository(),
                    container.resultRepository(),
                    container.createGameLoop(),
                    boardMapper,
                    nextPieceMapper
            );
        }

        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
    }
}
