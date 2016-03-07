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

package com.flaiker.bottimus.listeners;

import com.flaiker.bottimus.configuration.Configuration;
import com.flaiker.bottimus.services.AudioService;
import com.flaiker.bottimus.services.Playlist;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.logging.Logger;

/**
 * Listener to process commands sent in {@link Configuration#MAIN_CHANNEL}.
 */
public class CommandListener extends ListenerAdapter {
    private static final Logger LOG = Logger.getLogger(CommandListener.class.getName());
    private final AudioService audioService;

    public CommandListener(AudioService audioService) {
        this.audioService = audioService;
    }

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // Check if message was received on correct channel
        if (event.getChannel().getName().equals(Configuration.MAIN_CHANNEL)) {
            audioService.init(event.getJDA());
            String message = event.getMessage().getContent();

            // Check if message is valid / targeted at bottimus
            if (!message.startsWith("!") || message.split(" ").length == 0) return;

            // Parse and process the command
            String[] args = message.split(" ");
            String command = args[0].toLowerCase();
            switch (command) {
                case "!hello":
                    event.getChannel().sendMessageAsync("hello " + event.getAuthor().getUsername(), null);
                    break;
                case "!greeting":
                    audioService.sendGreeting();
                    break;
                case "!play":
                    audioService.addToPlaylist(new Playlist.Track(args[1]));
                    break;
                case "!start":
                    audioService.startPlaylist();
                    break;
                case "!pause":
                    //audio.pause();
                    break;
                case "!unpause":
                    //audio.resume();
                    break;
                case "!volume":
                    audioService.setVolume(Float.parseFloat(args[1]));
                    break;
                case "!next":
                    audioService.nextInPlaylist();
                    break;
                case "!previous":
                    audioService.previousInPlaylist();
                    break;
                case "!mode":
                    try {
                        audioService.setPlayListMode(Playlist.Mode.tryParse(args[1]));
                    } catch (IllegalArgumentException e) {
                        LOG.warning("Could not parse '" + args[1] + "', valid modes are LOOPING and NORMAL");
                    }
                    break;
                case "!status":
                    event.getChannel().sendMessageAsync(audioService.getPlaylistStatus(), null);
                    break;
                default:
                    LOG.warning("Could not recognize command '" + command + "'");
            }
        }
    }
}
