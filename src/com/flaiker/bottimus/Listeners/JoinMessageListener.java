package com.flaiker.bottimus.Listeners;

import com.flaiker.bottimus.Configuration;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.voice.VoiceJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.Optional;

/**
 * Listener to send a greeting on startup
 */
public class JoinMessageListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Optional<VoiceChannel> voiceChannel =
                event.getJDA().getVoiceChannelByName(Configuration.VOICE_CHANNEL).stream().findFirst();

        if (voiceChannel.isPresent()) {
            event.getJDA().getAudioManager().openAudioConnection(voiceChannel.get());
            System.out.println("Joined voice channel '" + Configuration.VOICE_CHANNEL + "'");
        } else {
            System.out.println("Could not find voice channel '" + Configuration.VOICE_CHANNEL + "'");
        }

        super.onReady(event);
    }

    @Override
    public void onVoiceJoin(VoiceJoinEvent event) {
        MessageBuilder mb = new MessageBuilder();
        Optional<TextChannel> channel =
                event.getJDA().getTextChannelsByName(Configuration.MAIN_CHANNEL).stream().findFirst();

        if (channel.isPresent()) {
            channel.get().sendMessageAsync(mb.appendString(Configuration.GREETING).setTTS(true).build(),
                    c -> System.out.println("Send greeting message to '" + Configuration.MAIN_CHANNEL + "'"));
        } else {
            System.out.println("Could not find channel '" + Configuration.MAIN_CHANNEL + "'");
        }

        super.onVoiceJoin(event);
    }
}
