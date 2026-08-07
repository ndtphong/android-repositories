package com.ndtphong.simpletetrisgame.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ndtphong.simpletetrisgame.R;
import com.ndtphong.simpletetrisgame.ui.main.MainViewModel;
import com.ndtphong.simpletetrisgame.ui.main.Screen;

public class HomeFragment extends Fragment {

    private MainViewModel mainViewModel;
    private HomeViewModel viewModel;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity())
                .get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        TextView title = view.findViewById(R.id.text_title);
        Button playBtn = view.findViewById(R.id.button_play);
        Button settingsBtn = view.findViewById(R.id.button_settings);

        viewModel.getUiState().observe(
                getViewLifecycleOwner(),
                state -> {
                    title.setText(state.title());
                    playBtn.setEnabled(state.playEnabled());
                    settingsBtn.setEnabled(state.settingsEnabled());
                }
        );

        playBtn.setOnClickListener(v ->
                mainViewModel.navigateTo(Screen.GAME)
        );

        settingsBtn.setOnClickListener(v ->
                mainViewModel.navigateTo(Screen.SETTINGS)
        );
    }
}
