package com.ndtphong.simpletetrisgame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ndtphong.simpletetrisgame.ui.main.MainViewModel;
import com.ndtphong.simpletetrisgame.ui.main.Screen;
import com.ndtphong.simpletetrisgame.ui.main.ScreenUiState;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private MainViewModel viewModel;
    private Screen renderedScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }

        navController = navHostFragment.getNavController();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getUiState().observe(this, this::navigateTo);
    }

    private void navigateTo(ScreenUiState state) {
        if (state.screen() == renderedScreen) {
            return;
        }

        renderedScreen = state.screen();

        int destinationId = switch (state.screen()) {
            case HOME -> R.id.homeFragment;
            case GAME -> R.id.gameFragment;
            case SETTINGS -> R.id.settingsFragment;
            case RESULT -> R.id.resultFragment;
        };

        navigateOnce(destinationId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp()
                || super.onSupportNavigateUp();
    }

    private void navigateOnce(int destinationId) {
        if (navController.getCurrentDestination() == null
                || navController.getCurrentDestination().getId() != destinationId) {
            navController.navigate(destinationId);
        }
    }
}