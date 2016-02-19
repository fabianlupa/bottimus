package com.flaiker.bottimus.tools;

import com.flaiker.bottimus.configuration.Configuration;
import net.dv8tion.jda.JDA;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * LogHandler for sending log output to the log channel ({@link Configuration#LOG_CHANNEL})
 */
public class ChannelLogHandler extends StreamHandler {
    private final JDA jda;

    public ChannelLogHandler(JDA jda, Formatter formatter) {
        this(jda);
        setFormatter(formatter);
    }

    public ChannelLogHandler(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void publish(LogRecord record) {
        jda.getTextChannelsByName(Configuration.LOG_CHANNEL).stream().findFirst()
                .ifPresent(c -> c.sendMessage(getFormatter().format(record)));
    }
}
