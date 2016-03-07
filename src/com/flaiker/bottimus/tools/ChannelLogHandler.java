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
