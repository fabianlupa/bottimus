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
