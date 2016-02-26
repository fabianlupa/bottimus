package com.flaiker.bottimus.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A playlist for managing {@link Track tracks}.
 */
public class Playlist {
    private static final Logger LOG = Logger.getLogger(Playlist.class.getName());
    private List<Track> tracks;
    private Optional<Track> current;
    private Mode mode;
    private Runnable trackChangedListener;

    public Playlist() {
        tracks = new ArrayList<>();
        mode = Mode.NORMAL;
        current = Optional.empty();
    }

    /**
     * @return The current track
     */
    public Optional<Track> getCurrentTrack() {
        return current;
    }

    /**
     * @return A copy of the tracklist
     */
    public List<Track> getTracks() {
        return new ArrayList<>(tracks);
    }

    public boolean hasItems() {
        return !tracks.isEmpty();
    }


    /**
     * Skip to the next track in the playlist.
     * <p>Notifies the {@link Playlist#setTrackChangedListener(Runnable) TrackChangedListener}.
     *
     * @throws IllegalStateException Playlist does not have any items or no track is selected or there is no next track
     */
    public void next() throws IllegalStateException {
        if (!hasItems())
            throw new IllegalStateException("Cannot go to next track because the playlist is empty");
        if (!current.isPresent())
            throw new IllegalStateException("Cannot go to next track because there is no current track");

        int index = tracks.indexOf(current.get());
        if (index == tracks.size() - 1 && mode != Mode.LOOPING)
            throw new IllegalStateException("Cannot go to next track because the current track is the last "
                    + "and looping is disabled");

        if (index == tracks.size() - 1) current = Optional.of(tracks.get(0));
        else current = Optional.of(tracks.get(index + 1));

        LOG.info("Skipped to next track in playlist");

        notifyTrackChangedListener();
    }

    /**
     * Skip to the previous track in the playlist.
     * <p>Notifies the {@link Playlist#setTrackChangedListener(Runnable) TrackChangedListener}.
     *
     * @throws IllegalStateException Playlist does not have any items or no track is selected or there is no previous
     *                               track
     */
    public void previous() throws IllegalStateException {
        if (!hasItems())
            throw new IllegalStateException("Cannot go to previous track because the playlist is empty");
        if (!current.isPresent())
            throw new IllegalStateException("Cannot go to previous track because there is no current track");

        int index = tracks.indexOf(current.get());
        if (index == 0 && mode != Mode.LOOPING)
            throw new IllegalStateException("Cannot go to previous track because the current track is the first "
                    + "and looping is disabled");

        if (index == 0) current = Optional.of(tracks.get(tracks.size() - 1));
        else current = Optional.of(tracks.get(index - 1));

        LOG.info("Skipped to previous track in playlist");

        notifyTrackChangedListener();

    }

    /**
     * Select the first track in the playlist.
     * <p>Notifies the {@link Playlist#setTrackChangedListener(Runnable) TrackChangedListener}.
     *
     * @throws IllegalStateException Playlist is empty
     */
    public void toStart() throws IllegalStateException {
        if (!hasItems())
            throw new IllegalStateException("Cannot go to start of playlist because it is empty.");

        current = Optional.of(tracks.get(0));

        LOG.info("Started playlist from the start");

        notifyTrackChangedListener();
    }

    /**
     * Set the mode of the playlist.
     *
     * @param mode New mode
     */
    public void setMode(Mode mode) {
        LOG.info("Changed playlist mode to " + mode.name());
        this.mode = mode;
    }

    /**
     * Add a track to the playlist.
     *
     * @param track New track
     */
    public void addTrack(Track track) {
        tracks.add(track);
        LOG.info("Added '" + track.url + "' to playlist");
    }

    /**
     * Set a listener that is notified when the current track changes.
     *
     * @param trackChangedListener The new listener
     */
    public void setTrackChangedListener(Runnable trackChangedListener) {
        this.trackChangedListener = trackChangedListener;
    }

    private void notifyTrackChangedListener() {
        if (trackChangedListener != null) trackChangedListener.run();
    }

    /**
     * An immutable track.
     */
    public static class Track {
        public final String url;

        /**
         * @param url The url to the track
         */
        public Track(String url) {
            this.url = url;
        }
    }

    /**
     * Modes the playlist can use.
     */
    public enum Mode {
        /**
         * End playlist at the last track, cannot call {@link Playlist#previous()} successfully from the beginning
         * or {@link Playlist#next()} at the end.
         */
        NORMAL,
        /**
         * Start from the beginning if the end of the playlist is reached or skip to the end if
         * {@link Playlist#previous()} is called on the first track.
         */
        LOOPING;

        /**
         * Try to get a {@link Mode} from a string.
         * <p>This function is case- and leading / trailing whitespace insensitive.
         *
         * @param mode The string to parse
         * @return Found mode
         * @throws IllegalArgumentException Could not find a mode
         */
        public static Mode tryParse(String mode) throws IllegalArgumentException {
            String normalized = mode.toUpperCase().trim();
            return Mode.valueOf(normalized);
        }
    }
}
