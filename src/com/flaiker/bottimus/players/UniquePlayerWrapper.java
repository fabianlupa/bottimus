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
