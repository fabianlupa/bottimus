package com.flaiker.bottimus.Listeners;

import com.flaiker.bottimus.Configuration;
import com.flaiker.bottimus.tools.ResourcePlayer;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.voice.VoiceJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
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
            channel.get().sendMessageAsync(mb.appendString(Configuration.GREETING).build(),
                    c -> System.out.println("Send greeting message to '" + Configuration.MAIN_CHANNEL + "'"));
        } else {
            System.out.println("Could not find channel '" + Configuration.MAIN_CHANNEL + "'");
        }

        try {
            Player player = new ResourcePlayer(getClass().getResource("/hello.mp3"));
            event.getJDA().getAudioManager().setSendingHandler(player);
            Thread.sleep(2000);
            player.play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not read audio file");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
        }

        super.onVoiceJoin(event);
    }
}
