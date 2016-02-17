package com.flaiker.bottimus.configuration;

/**
 * Configuration file for the application.
 * <p>
 * Options are static fields annotated with {@link Option} to be loaded from a config file and changed using reflection
 * in {@link ConfigurationBuilder}. Default values for each option are provided here.
 */
public class Configuration {
    @Option("main_channel")
    public static String MAIN_CHANNEL = "general";
    @Option("voice_channel")
    public static String VOICE_CHANNEL = "General";
    @Option("email")
    public static String EMAIL = "email@email.email";
    @Option("password")
    public static String PASSWORD = "password!";
    @Option("greeting")
    public static String GREETING = "Guten Tag";
}
