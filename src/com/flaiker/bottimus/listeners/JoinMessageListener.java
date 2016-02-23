package com.flaiker.bottimus.listeners;

import com.flaiker.bottimus.configuration.Configuration;
import com.flaiker.bottimus.services.AudioService;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.voice.VoiceJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Listener to send a greeting on startup
 */
public class JoinMessageListener extends ListenerAdapter {
    private static final Logger LOG = Logger.getLogger(JoinMessageListener.class.getName());
    private final AudioService audioService;

    public JoinMessageListener(AudioService audioService) {
        this.audioService = audioService;
    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
    }

    @Override
    public void onVoiceJoin(VoiceJoinEvent event) {
        MessageBuilder mb = new MessageBuilder();
        Optional<TextChannel> channel =
                event.getJDA().getTextChannelsByName(Configuration.MAIN_CHANNEL).stream().findFirst();

        if (channel.isPresent()) {
            channel.get().sendMessageAsync(mb.appendString(Configuration.GREETING).build(),
                    c -> LOG.info("Send greeting message to '" + Configuration.MAIN_CHANNEL + "'"));
        } else {
            LOG.warning("Could not find channel '" + Configuration.MAIN_CHANNEL + "'");
        }

        audioService.sendGreeting();

        super.onVoiceJoin(event);
    }
}
