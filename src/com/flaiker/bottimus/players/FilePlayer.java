package com.flaiker.bottimus.players;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class FilePlayer extends EventPlayer implements AudioSource {
    private File audioFile;

    public FilePlayer() {
    }

    public FilePlayer(File file) throws IOException, UnsupportedAudioFileException {
        setAudioFile(file);
    }

    private void setAudioFile(File file) throws IOException, UnsupportedAudioFileException {
        reset();
        audioFile = file;
        setAudioSource(AudioSystem.getAudioInputStream(file));
    }

    @Override
    public void setSource(Object object) throws IOException, UnsupportedAudioFileException, ClassCastException {
        if (!File.class.isInstance(object)) throw new ClassCastException("Object must be an instance of File.");
        setAudioFile((File) object);
    }

    @Override
    public String getSourceAsString() {
        return audioFile != null ? audioFile.getAbsolutePath() : "";
    }

    @Override
    public void restart() {
        reset();
    }
}
