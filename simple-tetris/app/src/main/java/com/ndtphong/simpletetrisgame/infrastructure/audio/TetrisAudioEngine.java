package com.ndtphong.simpletetrisgame.infrastructure.audio;

import androidx.annotation.NonNull;

public interface TetrisAudioEngine {

    void setEnabled(boolean enabled);

    void play(@NonNull GameSoundEffect effect);

    void startMusic();

    void stopMusic();

    void release();
}
