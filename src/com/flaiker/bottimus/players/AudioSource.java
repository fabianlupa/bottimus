/******************************************************************************
 * Copyright (C) 2016 Fabian Lupa                                             *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License          *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.      *
 ******************************************************************************/

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
