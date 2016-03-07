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

package com.flaiker.bottimus.audioprovider;

import java.io.File;
import java.util.logging.Logger;

/**
 * Thread for periodically checking if a file has been created and if its size is larger than zero.
 */
public class CheckDownloadThread extends Thread {
    private static final Logger LOG = Logger.getLogger(CheckDownloadThread.class.getName());
    private static final long WAIT_INTERVAL = 1000L;
    private static final long MAX_WAIT_TIME = 4000L;
    private long elapsedTime = 0L;
    private File toBeCheckFile;
    private Runnable mediaPlayerCallback;
    private Runnable retryCallback;

    /**
     * @param toBeCheckFile       The file to be checked
     * @param mediaPlayerCallback Callback when the check returns success
     * @param retryCallback       Callback when the check still fails after the maximum amount of time
     */
    public CheckDownloadThread(File toBeCheckFile, Runnable mediaPlayerCallback, Runnable retryCallback) {
        this.toBeCheckFile = toBeCheckFile;
        this.mediaPlayerCallback = mediaPlayerCallback;
        this.retryCallback = retryCallback;
    }

    @Override
    public void run() {
        while (true) {
            // Abort if maximum wait time has been reached
            if (elapsedTime > MAX_WAIT_TIME) {
                LOG.info("Reached maximum wait time for download");
                retryCallback.run();
                break;
            }

            // Check file
            if (toBeCheckFile.exists() && toBeCheckFile.length() > 0) {
                mediaPlayerCallback.run();
                break;
            } else try {
                LOG.info("Checked for downloaded file, nothing there yet, elapsed wait time so far: " + elapsedTime
                        + "ms");

                // Sleep if there is nothing yet
                Thread.sleep(WAIT_INTERVAL);
                elapsedTime += WAIT_INTERVAL;
            } catch (InterruptedException e) {
                LOG.info("Download check thread got interrupted (possibly by new download)");
                break;
            }
        }
    }
}
