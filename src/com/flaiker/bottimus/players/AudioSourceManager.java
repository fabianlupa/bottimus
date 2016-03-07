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

import net.dv8tion.jda.audio.AudioSendHandler;
import net.dv8tion.jda.audio.player.Player;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class for managing multiple {@link EventPlayer EventPlayers}.
 * <p>
 * This class provides the {@link AudioSendHandler} interface to get the sound output. Multiple EventPlayers can be
 * registered and played from. Each EventPlayer must be unique in its type in the list of EventPlayers. If multiple
 * instances of the same type of EventPlayer must be managed, create a subclass.
 * <p>
 * One EventPlayer is registered as the default player. If it is active and another player is invoked to play sound the
 * default player will resume playback after the invoked player has finished playing.
 * <p>
 * State (resume/pause/stop) can be controlled for each EventPlayer individually or for all of them.
 *
 * @see EventPlayer
 * @see NoSuitablePlayerFoundException
 * @see UniquePlayerWrapper
 * @see AudioSource
 */
public class AudioSourceManager implements AudioSendHandler {
    private static final Logger LOG = Logger.getLogger(AudioSourceManager.class.getName());
    private List<EventPlayer> players;
    private EventPlayer currentPlayer;
    private EventPlayer defaultPlayer;
    private boolean defaultGotPaused;

    /**
     * Create a new {@link AudioSourceManager} instance.
     *
     * @param players       The list of players to be managed, each of them must have a unique type
     * @param defaultPlayer The default player, must be contained in players
     * @return The created instance
     * @throws IllegalArgumentException
     */
    public static AudioSourceManager newAudioSourceManager(List<EventPlayer> players, EventPlayer defaultPlayer)
            throws IllegalArgumentException {
        // Check player list size
        if (players.size() == 0)
            throw new IllegalArgumentException("Player list must have at least one entry.");

        // Check if list contains default player
        if (!players.contains(defaultPlayer))
            throw new IllegalArgumentException("Default player must be in player list.");

        // Check if the players in the list are all of different types
        Set<UniquePlayerWrapper> set = new HashSet<>();
        try {
            players.forEach(p -> set.add(new UniquePlayerWrapper(p)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Players are not unique.");
        }

        return new AudioSourceManager(players, defaultPlayer);
    }

    private AudioSourceManager(List<EventPlayer> players, EventPlayer defaultPlayer) {
        this.players = players;
        this.defaultPlayer = defaultPlayer;
        this.currentPlayer = defaultPlayer;

        players.stream().filter(p -> p != defaultPlayer).forEach(p -> p.addStoppedListener(() -> {
            LOG.info("Finished playing using '" + p.getClass().getName() + "'");
            this.currentPlayer = defaultPlayer;
            if (defaultGotPaused) {
                LOG.info("Resuming default player");
                defaultPlayer.play();
                defaultGotPaused = false;
            }
        }));
    }

    /**
     * Add a stop listener to the specified managed player.
     *
     * @param type     Type of the player where the listener should be added
     * @param runnable The listener
     * @throws NoSuitablePlayerFoundException
     */
    public <T extends EventPlayer> void addStoppedListener(Class<T> type, Runnable runnable)
            throws NoSuitablePlayerFoundException {
        Optional<T> player = getPlayer(type);
        player.ifPresent(p -> p.addStoppedListener(runnable));
        player.orElseThrow(NoSuitablePlayerFoundException::new);
    }

    /**
     * Play a sound resource using the specified managed player.
     * <p>
     * To use this method the player to be used must implement the {@see AudioSource} interface.
     *
     * @param type  Type of the player to use
     * @param sound The sound resource to use (not type safe)
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws NoSuitablePlayerFoundException
     * @throws ClassCastException
     */
    public <T extends EventPlayer & AudioSource> void play(Class<T> type, Object sound)
            throws IOException, UnsupportedAudioFileException, NoSuitablePlayerFoundException {
        Optional<T> audioSource = getAudioSourcePlayer(type);
        if (audioSource.isPresent()) {
            if (currentPlayer == defaultPlayer && currentPlayer.isPlaying()) {
                LOG.info("Pausing default player");
                currentPlayer.pause();
                defaultGotPaused = true;
            }
            audioSource.get().setSource(sound);
            LOG.info("Playing using '" + audioSource.get().getClass().getName() + "' now");
            currentPlayer = audioSource.get();
            currentPlayer.play();
        } else throw new NoSuitablePlayerFoundException();
    }

    /**
     * Stop playback on all managed players.
     */
    public void stopAll() {
        players.forEach(Player::stop);
    }

    /**
     * Stop playback on the specified managed player.
     *
     * @param type The player to use
     * @throws NoSuitablePlayerFoundException
     */
    public <T extends EventPlayer> void stop(Class<T> type) throws NoSuitablePlayerFoundException {
        Optional<T> player = getPlayer(type);
        player.ifPresent(Player::stop);
        player.orElseThrow(NoSuitablePlayerFoundException::new);
    }

    /**
     * Set the volume on all managed players.
     *
     * @param volume The new volume
     */
    public void setVolumeAll(float volume) {
        players.forEach(p -> p.setVolume(volume));
    }

    /**
     * Set the volume on the specified managed player.
     *
     * @param type   The player to use
     * @param volume The new volume
     * @throws NoSuitablePlayerFoundException
     */
    public <T extends EventPlayer> void setVolume(Class<T> type, float volume) throws NoSuitablePlayerFoundException {
        Optional<T> player = getPlayer(type);
        player.ifPresent(p -> p.setVolume(volume));
        player.orElseThrow(NoSuitablePlayerFoundException::new);
    }

    /**
     * Pause playback on all managed players.
     */
    public void pauseAll() {
        players.forEach(Player::pause);
    }

    /**
     * Pause playback on the specified managed player.
     *
     * @param type The player to use
     * @throws NoSuitablePlayerFoundException
     */
    public <T extends EventPlayer> void pause(Class<T> type) throws NoSuitablePlayerFoundException {
        Optional<T> player = getPlayer(type);
        player.ifPresent(Player::pause);
        player.orElseThrow(NoSuitablePlayerFoundException::new);
    }

    /**
     * Resume playback on all managed players.
     */
    public void resumeAll() {
        players.forEach(Player::play);
    }

    /**
     * Resume playback on the specified managed player.
     *
     * @param type The player to use
     * @throws NoSuitablePlayerFoundException
     */
    public <T extends EventPlayer> void resume(Class<T> type) throws NoSuitablePlayerFoundException {
        Optional<T> player = getPlayer(type);
        player.ifPresent(Player::play);
        player.orElseThrow(NoSuitablePlayerFoundException::new);
    }

    private <T extends EventPlayer> Optional<T> getPlayer(Class<T> type) {
        return players.stream().filter(p -> type.isAssignableFrom(p.getClass())).map(p -> (T) p).findFirst();
    }

    private <T extends AudioSource> Optional<T> getAudioSource(Class<T> type) {
        return players.stream().filter(p -> type.isAssignableFrom(p.getClass())).map(p -> (T) p).findFirst();
    }

    private <T extends EventPlayer & AudioSource> Optional<T> getAudioSourcePlayer(Class<T> type) {
        return players.stream().filter(p -> type.isAssignableFrom(p.getClass())).map(p -> (T) p).findFirst();
    }

    @Override
    public boolean canProvide() {
        return !currentPlayer.isStopped() && !currentPlayer.isPaused();
    }

    @Override
    public byte[] provide20MsAudio() {
        if (canProvide()) return currentPlayer.provide20MsAudio();

        return new byte[0];
    }
}
