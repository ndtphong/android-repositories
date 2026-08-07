package com.ndtphong.simpletetrisgame.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.TetrisApplication;
import com.ndtphong.simpletetrisgame.di.AppContainer;
import com.ndtphong.simpletetrisgame.ui.main.MainViewModel;
import com.ndtphong.simpletetrisgame.ui.main.Screen;

public class SettingsFragment extends Fragment {

    private MainViewModel mainViewModel;
    private SettingsViewModel viewModel;
    private boolean renderingState;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        AppContainer container = TetrisApplication
                .from(requireContext())
                .appContainer();

        viewModel = new ViewModelProvider(
                this,
                new SettingsViewModelFactory(container.settingsRepository())
        ).get(SettingsViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity())
                .get(MainViewModel.class);


        SwitchCompat soundSwitch = view.findViewById(R.id.switch_sound);
        SwitchCompat animationSwitch = view.findViewById(R.id.switch_animation);
        Button backBtn = view.findViewById(R.id.button_back);

        viewModel.getUiState().observe(
                getViewLifecycleOwner(),
                state -> {
                    renderingState = true;
                    soundSwitch.setChecked(state.soundEnabled());
                    animationSwitch.setChecked(state.animationEnabled());
                    renderingState = false;
                }
        );

        soundSwitch.setOnCheckedChangeListener(
                (button, checked) -> {
                    if (!renderingState) {
                        viewModel.setSoundEnabled(checked);
                    }
                }
        );

        animationSwitch.setOnCheckedChangeListener(
                (button, checked) -> {
                    if (!renderingState) {
                        viewModel.setAnimationEnabled(checked);
                    }
                }
        );

        backBtn.setOnClickListener(v ->
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
