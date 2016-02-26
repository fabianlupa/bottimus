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
