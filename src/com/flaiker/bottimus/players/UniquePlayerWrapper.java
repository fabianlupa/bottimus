package com.flaiker.bottimus.players;

/**
 * Internal helper class to compare {@link EventPlayer EventPlayers} based on their concrete type.
 */
public class UniquePlayerWrapper {
    public final EventPlayer eventPlayer;

    public UniquePlayerWrapper(EventPlayer eventPlayer) {
        this.eventPlayer = eventPlayer;
    }

    @Override
    public boolean equals(Object obj) {
        return UniquePlayerWrapper.class.isInstance(obj)
                && ((UniquePlayerWrapper) obj).getClass().getName().equals(eventPlayer.getClass().getName());
    }
}
