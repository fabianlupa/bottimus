package com.flaiker.bottimus.listeners;

import com.flaiker.bottimus.configuration.Configuration;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Listener to process commands sent in {@link Configuration#MAIN_CHANNEL}.
 */
public class CommandListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // Check if message was received on correct channel
        if (event.getChannel().getName().equals(Configuration.MAIN_CHANNEL)) {
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
                default:
                    System.out.println("Could not recognize command '" + command + "'");
            }
        }
    }
}
