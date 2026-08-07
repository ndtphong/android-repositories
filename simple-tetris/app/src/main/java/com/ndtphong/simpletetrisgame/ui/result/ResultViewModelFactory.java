package com.ndtphong.simpletetrisgame.ui.result;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.domain.repository.ResultRepository;

public final class ResultViewModelFactory implements ViewModelProvider.Factory {

    private final ResultRepository repository;

    public ResultViewModelFactory(@NonNull ResultRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ResultViewModel.class)) {
            return (T) new ResultViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
    }
}
