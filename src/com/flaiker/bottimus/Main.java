package com.flaiker.bottimus;

import com.flaiker.bottimus.configuration.Configuration;
import com.flaiker.bottimus.configuration.ConfigurationBuilder;
import com.flaiker.bottimus.listeners.JoinMessageListener;
import net.dv8tion.jda.JDABuilder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting");

        try {
            // Load configuration
            new ConfigurationBuilder(new File("config.properties")).build();

            // Start JDA
            new JDABuilder()
                    .setEmail(Configuration.EMAIL)
                    .setPassword(Configuration.PASSWORD)
                    .addListener(new JoinMessageListener())
                    .buildAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
