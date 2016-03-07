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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Format log output in one line
 */
public class LogFormatter extends Formatter {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        Optional<Throwable> thrown = Optional.ofNullable(record.getThrown());

        return new StringBuilder()
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(record.getMillis())))
                .append(" ")
                .append(record.getLevel().getName())
                .append(" (")
                .append(Thread.currentThread().getName())
                .append("): ")
                .append(formatMessage(record))
                .append(LINE_SEPARATOR)
                .append(thrown.map(t -> {
                    StringWriter sw = new StringWriter();
                    t.printStackTrace(new PrintWriter(sw));
                    return sw.toString() + LINE_SEPARATOR;
                }).orElse(""))
                .toString();
    }
}
