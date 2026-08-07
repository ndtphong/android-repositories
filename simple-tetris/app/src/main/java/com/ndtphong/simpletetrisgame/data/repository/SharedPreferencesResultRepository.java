package com.ndtphong.simpletetrisgame.data.repository;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.data.datasource.ResultLocalDataSource;
import com.ndtphong.simpletetrisgame.domain.model.GameResult;
import com.ndtphong.simpletetrisgame.domain.repository.ResultRepository;

public final class SharedPreferencesResultRepository implements ResultRepository {

    private final ResultLocalDataSource dataSource;

    public SharedPreferencesResultRepository(
            @NonNull ResultLocalDataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public GameResult getResult() {
        return dataSource.getResult();
    }

    @Override
    public void saveResult(@NonNull GameResult result) {
        dataSource.saveResult(result);
    }

    @Override
    public void clearResult() {
        dataSource.clear();
    }
}
