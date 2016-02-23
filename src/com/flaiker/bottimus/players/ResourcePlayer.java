package com.flaiker.bottimus.players;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

/**
 * Player for playing audio files from a resource {@link URL}
 */
public class ResourcePlayer extends EventPlayer implements AudioSource {
    private URL resourceUrl = null;

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
    public void restart() {
        reset();
    }

    @Override
    public void setSource(Object object) throws IOException, UnsupportedAudioFileException, ClassCastException {
        if (!URL.class.isInstance(object)) throw new ClassCastException("Object must be an instance of URL.");
        setInputStream((URL) object);
    }

    @Override
    public String getSourceAsString() {
        return resourceUrl != null ? resourceUrl.toString() : "";
    }
}
