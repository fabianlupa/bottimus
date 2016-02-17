package com.flaiker.bottimus.tools;

import net.dv8tion.jda.audio.player.Player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

/**
 * Player for playing audio files from a resource {@link URL}
 */
public class ResourcePlayer extends Player {
    private URL resourceUrl = null;
    private boolean started = false;
    private boolean playing = false;
    private boolean paused = false;
    private boolean stopped = true;

    public ResourcePlayer() {
    }

    public ResourcePlayer(URL resourceUrl) throws IOException, UnsupportedAudioFileException {
        setInputStream(resourceUrl);
    }

    public void setInputStream(URL resourceUrl) throws IOException, UnsupportedAudioFileException {
        reset();
        this.resourceUrl = resourceUrl;
        setAudioSource(AudioSystem.getAudioInputStream(resourceUrl));
    }

    @Override
    public void play() {
        if (started && stopped)
            throw new IllegalStateException("Cannot start a player after it has been stopped.\n" +
                    "Please use the restart method or load a new file.");
        started = true;
        playing = true;
        paused = false;
        stopped = false;
    }

    @Override
    public void pause() {
        playing = false;
        paused = true;
    }

    @Override
    public void stop() {
        playing = false;
        paused = false;
        stopped = true;
    }

    @Override
    public void restart() {
        try {
            URL oldResourceUrl = resourceUrl;
            reset();
            setInputStream(oldResourceUrl);
            play();
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    protected void reset() {
        resourceUrl = null;
        started = false;
        playing = false;
        paused = false;
        stopped = true;
    }
}
