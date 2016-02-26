package com.flaiker.bottimus.configuration;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Builds the {@link Configuration}-file.
 * <p>
 * Loads a properties file and initializes the static configuration fields from it. Falls back to default values if they
 * are not set. Writes the config file back immediately after load.
 */
public class ConfigurationBuilder {
    private static final Logger LOG = Logger.getLogger(Configuration.class.getName());
    private final File configFile;
    private final Properties properties;

    /**
     * Constructor
     * @param configFile Reference to the location of the properties-file
     */
    public ConfigurationBuilder(File configFile) {
        this.configFile = configFile;
        this.properties = new Properties();
    }


    /**
     * Loads the configuration
     * @throws IOException
     */
    public void build() throws IOException {
        if (configFile == null) throw new IllegalStateException("File not initialized");

        Reflections reflections = new Reflections(new org.reflections.util.ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClass(Configuration.class))
                .addScanners(new FieldAnnotationsScanner()));
        Set<Field> options = reflections.getFieldsAnnotatedWith(Option.class);

        // Load config file if it exists
        if (configFile.exists()) properties.load(new FileInputStream(configFile));

        // Load configuration fields
        options.forEach(o -> {
            Option option = o.getAnnotation(Option.class);
            try {
                Object value = configFile.exists()
                        ? properties.getOrDefault(option.value(), o.get(null))
                        : o.get(null);
                o.setAccessible(true);
                o.set(null, value);
                if (value.getClass().isAssignableFrom(String.class)) {
                    properties.setProperty(option.value(), (String) value);
                }
            } catch (IllegalAccessException e) {
                LOG.severe("Could not load configuration, IllegalAccessException");
                System.exit(-1);
            }
        });

        // Write the full configuration file
        properties.store(new FileOutputStream(configFile), null);
    }
}
