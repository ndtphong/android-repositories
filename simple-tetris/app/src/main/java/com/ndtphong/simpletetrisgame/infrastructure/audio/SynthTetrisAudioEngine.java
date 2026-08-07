package com.ndtphong.simpletetrisgame.infrastructure.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SynthTetrisAudioEngine implements TetrisAudioEngine {

    private static final int SAMPLE_RATE = 22_050;

    private static final int[] MUSIC_FREQUENCIES = {
            659, 494, 523, 587,
            523, 494, 440, 440,
            523, 659, 587, 523,
            494, 523, 587, 659
    };

    private static final int[] MUSIC_DURATIONS = {
            180, 90, 90, 180,
            90, 90, 180, 180,
            180, 180, 180, 180,
            180, 180, 180, 360
    };

    private final Object audioLock = new Object();

    private final ExecutorService soundExecutor = Executors.newCachedThreadPool();

    private final short[] musicPcm = synthesize(
            MUSIC_FREQUENCIES,
            MUSIC_DURATIONS,
            0.12f
    );

    private boolean enabled = true;
    private boolean released;

    @Nullable
    private AudioTrack musicTrack;

    @Override
    public void setEnabled(boolean enabled) {
        synchronized (audioLock) {
            this.enabled = enabled;
        }

        if (!enabled) {
            stopMusic();
        }
    }

    @Override
    public void play(@NonNull GameSoundEffect effect) {
        synchronized (audioLock) {
            if (!enabled || released) {
                return;
            }
        }

        soundExecutor.execute(() -> {
            short[] pcm = synthesize(
                    effect.frequencies(),
                    effect.durations(),
                    0.25f
            );

            AudioTrack track = createTrack(
                    pcm.length,
                    AudioAttributes.CONTENT_TYPE_SONIFICATION
            );

            try {
                track.write(
                        pcm,
                        0,
                        pcm.length,
                        AudioTrack.WRITE_BLOCKING
                );

                track.play();

                Thread.sleep(totalDuration(effect.durations()) + 50L    );
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } finally {
                safelyRelease(track);
            }
        });
    }

    @Override
    public void startMusic() {
        synchronized (audioLock) {
            if (!enabled
                    || released
                    || musicTrack != null) {
                return;
            }

            AudioTrack track = createTrack(
                    musicPcm.length,
                    AudioAttributes.CONTENT_TYPE_MUSIC
            );

            track.write(
                    musicPcm,
                    0,
                    musicPcm.length,
                    AudioTrack.WRITE_BLOCKING
            );

            track.setLoopPoints(
                    0,
                    musicPcm.length,
                    -1
            );

            track.play();
            musicTrack = track;
        }
    }

    @Override
    public void stopMusic() {
        AudioTrack track;

        synchronized (audioLock) {
            track = musicTrack;
            musicTrack = null;
        }

        if (track != null) {
            safelyRelease(track);
        }
    }

    @Override
    public void release() {
        synchronized (audioLock) {
            if (released) {
                return;
            }

            released = true;
        }

        stopMusic();
        soundExecutor.shutdownNow();
    }

    @NonNull
    private static AudioTrack createTrack(
            int sampleCount,
            int contentType
    ) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(contentType)
                .build();

        AudioFormat format = new AudioFormat.Builder()
                .setSampleRate(SAMPLE_RATE)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build();

        return new AudioTrack.Builder()
                .setAudioAttributes(attributes)
                .setAudioFormat(format)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .setBufferSizeInBytes(sampleCount * Short.BYTES)
                .build();
    }

    private static short[] synthesize(
            @NonNull int[] frequencies,
            @NonNull int[] durations,
            float volume
    ) {
        if (frequencies.length != durations.length) {
            throw new IllegalArgumentException(
                    "Frequency and duration sizes differ"
            );
        }

        int totalSamples = 0;

        for (int duration : durations) {
            totalSamples += duration * SAMPLE_RATE / 1000;
        }

        short[] pcm = new short[totalSamples];
        int outputIndex = 0;

        for (int note = 0; note < frequencies.length; note++) {

            int frequency = frequencies[note];
            int noteSamples = durations[note] * SAMPLE_RATE / 1000;

            int fadeSamples = Math.min(
                    SAMPLE_RATE / 100,
                    Math.max(1, noteSamples / 4)
            );

            for (int sample = 0; sample < noteSamples; sample++) {

                float attack = Math.min(1f, (float) sample / fadeSamples);

                float release = Math.min(
                        1f,
                        (float) (noteSamples - sample - 1) / fadeSamples
                );

                float envelope = Math.min(attack, release);

                double value = frequency == 0
                        ? 0.0
                        : Math.sin(2.0 * Math.PI * frequency * sample / SAMPLE_RATE);

                pcm[outputIndex++] = (short) (value * Short.MAX_VALUE * volume * envelope);
            }
        }

        return pcm;
    }

    private static long totalDuration(
            @NonNull int[] durations
    ) {
        long total = 0L;

        for (int duration : durations) {
            total += duration;
        }

        return total;
    }

    private static void safelyRelease(
            @NonNull AudioTrack track
    ) {
        try {
            track.stop();
            track.flush();
        } catch (IllegalStateException ignored) {
        }
        track.release();
    }
}
