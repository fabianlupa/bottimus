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

package com.flaiker.bottimus.services;

import com.flaiker.bottimus.audioprovider.VlcClient;
import com.flaiker.bottimus.configuration.Configuration;
import com.flaiker.bottimus.players.AudioSourceManager;
import com.flaiker.bottimus.players.EventPlayer;
import com.flaiker.bottimus.players.FilePlayer;
import com.flaiker.bottimus.players.ResourcePlayer;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.VoiceChannel;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Class for managing audio.
 * <p>
 * Using:
 * <ul>
 * <li>{@link VlcClient}</li>
 * <li>{@link Playlist}</li>
 * <li>{@link AudioSourceManager}</li>
 * </ul>
 */
public class AudioService {
    private static final Logger LOG = Logger.getLogger(AudioService.class.getName());
    private boolean initialized = false;
    private final VlcClient vlcClient;
    private final AudioSourceManager audioSourceManager;
    private final Playlist playlist;

    public AudioService() {
        vlcClient = new VlcClient();
        playlist = new Playlist();

        List<EventPlayer> players = new ArrayList<>();
        ResourcePlayer resourcePlayer = new ResourcePlayer();
        FilePlayer filePlayer = new FilePlayer();
        players.add(resourcePlayer);
        players.add(filePlayer);

        audioSourceManager = AudioSourceManager.newAudioSourceManager(players, filePlayer);
        audioSourceManager.addStoppedListener(FilePlayer.class, playlist::next);
        audioSourceManager.setVolumeAll(0.2f);

        playlist.setTrackChangedListener(this::playCurrentPlaylistTrack);
    }

    /**
     * Initialize the service by joining the {@link Configuration#VOICE_CHANNEL voice channel}.
     *
     * @param jda The jda instance to use
     */
    public void init(JDA jda) {
        if (!initialized) {
            // Join the voice channel
            Optional<VoiceChannel> voiceChannel =
                    jda.getVoiceChannelByName(Configuration.VOICE_CHANNEL).stream().findFirst();

            if (voiceChannel.isPresent()) {
                jda.getAudioManager().openAudioConnection(voiceChannel.get());
                jda.getAudioManager().setSendingHandler(audioSourceManager);
                LOG.info("Joined voice channel '" + Configuration.VOICE_CHANNEL + "'");
            } else {
                LOG.warning("Could not find voice channel '" + Configuration.VOICE_CHANNEL + "'");
            }
        }

        initialized = true;
    }

    /**
     * Sends a vocal greeting to the voice channel.
     */
    public void sendGreeting() {
        try {
            audioSourceManager.play(ResourcePlayer.class, getClass().getResource("/hello.mp3"));
        } catch (IOException | UnsupportedAudioFileException e) {
            LOG.warning("Could not find/open/decode the greeting audio file");
        }
    }

    /**
     * Adds a track to the playlist.
     *
     * @param track The track to be added
     */
    public void addToPlaylist(Playlist.Track track) {
        playlist.addTrack(track);
        if (!playlist.getCurrentTrack().isPresent()) startPlaylist();
    }

    /**
     * Skips to the next track in the playlist.
     *
     * @throws IllegalStateException
     */
    public void nextInPlaylist() throws IllegalStateException {
        if (!playlist.getCurrentTrack().isPresent())
            throw new IllegalStateException("Playlist has not been started yet");

        playlist.next();
    }

    /**
     * Skips to the previous track in the playlist.
     *
     * @throws IllegalStateException
     */
    public void previousInPlaylist() throws IllegalStateException {
        if (!playlist.getCurrentTrack().isPresent())
            throw new IllegalStateException("Playlist has not been started yet");

        playlist.previous();
    }

    /**
     * Skips to the start of the playlist.
     *
     * @throws IllegalStateException
     */
    public void startPlaylist() throws IllegalStateException {
        if (playlist.hasItems()) playlist.toStart();
        else throw new IllegalStateException("Cannot start playlist, it has not tracks.");
    }

    /**
     * Sets the volume.
     *
     * @param volume The new volume
     */
    public void setVolume(float volume) {
        audioSourceManager.setVolumeAll(volume);
    }

    /**
     * Sets the mode of the playlist.
     *
     * @param mode The new mode
     */
    public void setPlayListMode(Playlist.Mode mode) {
        playlist.setMode(mode);
    }

    private void playCurrentPlaylistTrack() throws IllegalStateException {
        if (!playlist.getCurrentTrack().isPresent())
            throw new IllegalStateException("Playlist has not been started yet");

        LOG.info("Preparing current playlist track...");
        playlist.getCurrentTrack().ifPresent(t -> vlcClient.download(t.url, () -> {
            try {
                LOG.info("Got stream, starting playback");
                audioSourceManager.play(FilePlayer.class, new File(Configuration.TEMP_FILE));
            } catch (IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }));
    }

    public String getPlaylistStatus() {
        return playlist.getStatus();
    }
}
