package com.ndtphong.simpletetrisgame.ui.result;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ndtphong.simpletetrisgame.domain.repository.ResultRepository;

public final class ResultViewModel extends ViewModel {

    private final ResultRepository repository;
    private final MutableLiveData<ResultUiState> mutableUiState = new MutableLiveData<>(ResultUiState.initial());

    public ResultViewModel(
            @NonNull ResultRepository repository
    ) {
        this.repository = repository;
        reload();
    }

    public LiveData<ResultUiState> getUiState() {
        return mutableUiState;
    }

    public void reload() {
        mutableUiState.setValue(ResultUiState.from(repository.getResult()));
    }

    public void clearResult() {
        repository.clearResult();

        mutableUiState.setValue(ResultUiState.initial());
    }
}
