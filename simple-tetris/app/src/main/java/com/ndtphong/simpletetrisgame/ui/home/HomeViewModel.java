package com.ndtphong.simpletetrisgame.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public final class HomeViewModel extends ViewModel {

    private final MutableLiveData<HomeUiState> mutableUiState =
            new MutableLiveData<>(HomeUiState.initial());

    public LiveData<HomeUiState> getUiState() {
        return mutableUiState;
    }
}
