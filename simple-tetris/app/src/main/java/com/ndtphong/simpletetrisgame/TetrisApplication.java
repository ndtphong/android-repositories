package com.ndtphong.simpletetrisgame;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.di.AppContainer;

public final class TetrisApplication extends Application {

    private AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();

        appContainer = new AppContainer(this);
    }

    @NonNull
    public AppContainer appContainer() {
        if (appContainer == null) {
            throw new IllegalStateException("AppContainer is unavailable");
        }

        return appContainer;
    }

    @NonNull
    public static TetrisApplication from(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();

        if (applicationContext instanceof TetrisApplication application) {
            return application;
        }

        throw new IllegalStateException(
                "TetrisApplication is not registered"
        );
    }
}
