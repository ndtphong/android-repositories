package com.ndtphong.simpletetrisgame.ui.result;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.TetrisApplication;
import com.ndtphong.simpletetrisgame.di.AppContainer;
import com.ndtphong.simpletetrisgame.ui.main.MainViewModel;
import com.ndtphong.simpletetrisgame.ui.main.Screen;

public class ResultFragment extends Fragment {
    private ResultViewModel viewModel;
    private MainViewModel mainViewModel;

    public ResultFragment() {
        super(R.layout.fragment_result);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        AppContainer container = TetrisApplication.from(requireContext()).appContainer();

        ResultViewModelFactory factory = new ResultViewModelFactory(container.resultRepository());

        viewModel = new ViewModelProvider(this, factory).get(ResultViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        TextView score = view.findViewById(R.id.text_score);
        TextView highScore = view.findViewById(R.id.text_high_score);
        TextView newHighScore = view.findViewById(R.id.text_new_high_score);

        Button restartBtn = view.findViewById(R.id.button_restart);
        Button homeBtn = view.findViewById(R.id.button_home);

        viewModel.getUiState().observe(
                getViewLifecycleOwner(),
                state -> {
                    score.setText(getString(R.string.score_value, state.score()));
                    highScore.setText(getString(R.string.high_score_value, state.highScore()));
                    newHighScore.setVisibility(state.newHighScore() ? View.VISIBLE : View.GONE);
                }
        );

        restartBtn.setOnClickListener(v -> {
            viewModel.clearResult();
            mainViewModel.navigateTo(Screen.GAME);
        });

        homeBtn.setOnClickListener(v ->
                mainViewModel.navigateTo(Screen.HOME)
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewModel != null) {
            viewModel.reload();
        }
    }
}
