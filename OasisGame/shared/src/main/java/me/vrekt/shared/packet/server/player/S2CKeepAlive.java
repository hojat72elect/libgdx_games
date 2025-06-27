package me.vrekt.shared.packet.server.player;

import io.netty.buffer.ByteBuf;
import me.vrekt.shared.packet.GamePacket;
import me.vrekt.shared.protocol.Packets;

/**
 * Check client status
 */
public final class S2CKeepAlive extends GamePacket {

    public S2CKeepAlive(ByteBuf buffer) {
        super(buffer);
    }

    public S2CKeepAlive() {
    }

    @Override
    public int getId() {
        return Packets.S2C_KEEP_ALIVE;
    }

    @Override
    public void encode() {
        writeId();
    }
}
