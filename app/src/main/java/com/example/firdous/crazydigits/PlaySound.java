package com.example.firdous.crazydigits;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by encrypted on 7/8/17.
 */

public class PlaySound {

    static MediaPlayer mediaPlayer;
    static MediaPlayer backgroundMediaPlayer;

    void intializeMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
    }

    MediaPlayer createSound(Context context, int type) throws IOException {
        intializeMediaPlayer();
        mediaPlayer.reset();
        String path = "android.resource://com.firdous.crazydigits/raw/";
        switch (type) {
            case 0:
                mediaPlayer.setDataSource(context, Uri.parse(path + "click"));
                mediaPlayer.prepare();
                break;

            default:
                mediaPlayer.setDataSource(context, Uri.parse(path + "wrong"));
                mediaPlayer.prepare();
        }
        return mediaPlayer;
    }

    void playSound() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.seekTo(0);
        else mediaPlayer.start();
    }

    void destroyObject() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    void playSoundBackground() {
        if (backgroundMediaPlayer.isPlaying()) {
            backgroundMediaPlayer.setLooping(true);
            backgroundMediaPlayer.setVolume(50, 50);
            backgroundMediaPlayer.seekTo(0);
        } else {
            backgroundMediaPlayer.setLooping(true);
            backgroundMediaPlayer.setVolume(50, 50);
            backgroundMediaPlayer.start();
        }
    }

    void destroyObjectBackground() {
        if (backgroundMediaPlayer != null) {
            if (backgroundMediaPlayer.isPlaying())
                backgroundMediaPlayer.stop();

            backgroundMediaPlayer.release();
            backgroundMediaPlayer = null;
        }
    }

    void intializeMediaPlayerBackground() {
        if (backgroundMediaPlayer == null)
            backgroundMediaPlayer = new MediaPlayer();
    }

    MediaPlayer createSoundBackground(Context context) throws IOException {
        intializeMediaPlayerBackground();
        backgroundMediaPlayer.reset();
        String path = "android.resource://com.firdous.crazydigits/raw/";
        backgroundMediaPlayer.setDataSource(context, Uri.parse(path + "music"));
        backgroundMediaPlayer.prepare();
        return backgroundMediaPlayer;
    }

    int pauseBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.isPlaying())
            backgroundMediaPlayer.pause();
        return backgroundMediaPlayer.getCurrentPosition();
    }

    int getSeekLength() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.isPlaying())
            return backgroundMediaPlayer.getCurrentPosition();
        else return 0;
    }

    void resumeBackgroundMusic(int length) {
        backgroundMediaPlayer.seekTo(length);
        backgroundMediaPlayer.start();
    }

}
