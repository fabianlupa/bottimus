package com.flaiker.bottimus;

import com.flaiker.bottimus.listeners.JoinMessageListener;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting");

        try {
            new JDABuilder()
                    .setEmail(Configuration.EMAIL)
                    .setPassword(Configuration.PASSWORD)
                    .addListener(new JoinMessageListener())
                    .buildAsync();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
