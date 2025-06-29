package me.vrekt.shared.packet.server.player;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import me.vrekt.shared.packet.GamePacket;
import me.vrekt.shared.protocol.Packets;

/**
 * Notify clients to construct a new player within their game instance
 */
public final class S2CNetworkCreatePlayer extends GamePacket {

    private String username;
    private int entityId;
    private float x, y;

    /**
     * Initialize
     *
     * @param username username or {@code null}
     *                 If username is null, username string will be an empty string
     * @param entityId ID
     * @param x        starting X
     * @param y        starting Y
     */
    public S2CNetworkCreatePlayer(String username, int entityId, float x, float y) {
        // empty string if no username!
        this.username = (username == null ? StringUtil.EMPTY_STRING : username);
        this.entityId = entityId;
        this.x = x;
        this.y = y;
    }

    public S2CNetworkCreatePlayer(ByteBuf buffer) {
        super(buffer);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return ID
     */
    public int getEntityId() {
        return entityId;
    }

    /**
     * @return X
     */
    public float getX() {
        return x;
    }

    /**
     * @return Y
     */
    public float getY() {
        return y;
    }

    @Override
    public int getId() {
        return Packets.S2C_CREATE_PLAYER;
    }

    @Override
    public void encode() {
        writeId();
        writeString(username);
        buffer.writeInt(entityId);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
    }

    @Override
    public void decode() {
        username = readString();
        entityId = buffer.readInt();
        x = buffer.readFloat();
        y = buffer.readFloat();
    }
}
