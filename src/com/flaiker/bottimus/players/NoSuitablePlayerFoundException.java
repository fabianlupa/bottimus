package com.flaiker.bottimus.players;

public class NoSuitablePlayerFoundException extends RuntimeException {
    public NoSuitablePlayerFoundException() {
        super("Could not find the specified player.");
    }

    public NoSuitablePlayerFoundException(String message) {
        super(message);
    }
}
