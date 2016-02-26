package com.flaiker.bottimus.players;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Interface to set the source of an audio player.
 */
public interface AudioSource {
    /**
     * Set the audio source of the player.
     * <p>
     * The type of the source parameter is intentionally {@link Object} so that it can be called from a generic method /
     * context. A {@link ClassCastException} must be thrown if the given type does not match the needed type by the
     * player.
     *
     * @param source The new audio source
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws ClassCastException
     */
    void setSource(Object source) throws IOException, UnsupportedAudioFileException, ClassCastException;

    /**
     * @return An identifier for the current source
     */
    String getSourceAsString();
}
