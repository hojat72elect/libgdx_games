package de.golfgl.lightblocks.multiplayer;

/**
 * To this listener multiplayer rooms send their messages
 * <p>
 * IMPORTANT: Methods are most likely not be called on render thread!
 * <p>
 * <p>
 * Created by Benjamin Schulte on 26.02.2017.
 */

public interface IRoomListener {

    /**
     * called when a room was joined or left
     */
    void multiPlayerRoomStateChanged(MultiPlayerObjects.RoomState roomState);

    /**
     * called when inhabitants of the room changed.
     *
     * @param mpo PlayerChanged object with further information
     */
    void multiPlayerRoomInhabitantsChanged(MultiPlayerObjects.PlayerChanged mpo);

    void multiPlayerGotErrorMessage(Object o);

    /**
     * called for every message that is not managed by the room. Probably it is a message for the game model
     *
     * @param o the message
     */
    void multiPlayerGotModelMessage(Object o);

    void multiPlayerGotRoomMessage(Object o);

    void multiPlayerRoomEstablishingConnection();
}
