package com.flaiker.bottimus.audioprovider;

import com.flaiker.bottimus.configuration.Configuration;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import java.io.File;
import java.util.logging.Logger;

/**
 * A wrapper class for managing a VLCJ {@link MediaPlayer} instance.
 */
public class VlcClient {
    private static final Logger LOG = Logger.getLogger(VlcClient.class.getName());
    private static final String[] VLC_ARGS = {
            "--intf", "dummy",
            "--vout", "dummy",
            "--no-audio",
            "--no-video-title-show",
            "--no-stats",
            "--no-sub-autodetect-file",
            "--no-inhibit",
            "--no-disable-screensaver",
            "--no-snapshot-preview",
            "--sout=#transcode{vcodec=none,acodec=mp3,ab=128,channels=2,samplerate=44100}:std{access=file,"
                    + "mux=mp3,dst='" + Configuration.TEMP_FILE + "'}"
    };
    private final HeadlessMediaPlayer mediaPlayer;
    private final File tempFile;
    private Thread lastDownloadCheckTask;

    public VlcClient() {
        // Initialize temp file
        tempFile = new File(Configuration.TEMP_FILE);

        // Configure player factory
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);

        // Create player
        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        mediaPlayer.setPlaySubItems(true);
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                // Play sub items immediately when added (for YouTube videos)
                mediaPlayer.playMedia(mediaPlayer.subItems().get(mediaPlayer.subItems().size() - 1));
            }
        });
    }

    /**
     * Download the specified url to the temporary file ({@link Configuration#TEMP_FILE}).
     *
     * @param url                The url to download
     * @param mediaReadyCallback Callback to be called when the download has started
     */
    public void download(String url, Runnable mediaReadyCallback) {
        // Cancel the last download check task if it is still active
        if (lastDownloadCheckTask != null && lastDownloadCheckTask.isAlive()) lastDownloadCheckTask.interrupt();

        // Delete temporary file before starting the download
        if (tempFile.exists()) tempFile.delete();

        mediaPlayer.stop();

        // Start download
        mediaPlayer.playMedia(url);

        mediaPlayer.start();

        // Periodically check if the download has begun (and therefore is ready to be played)
        lastDownloadCheckTask = new CheckDownloadThread(tempFile, mediaReadyCallback, () -> {
            LOG.info("Restarting download of '" + url + "'");
            download(url, mediaReadyCallback);
        });
        lastDownloadCheckTask.start();
    }
}
