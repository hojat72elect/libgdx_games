package me.vrekt.shared.packet.client.interior;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import me.vrekt.oasis.world.interior.Interior;
import me.vrekt.shared.packet.GamePacket;
import me.vrekt.shared.protocol.Packets;

/**
 * Client is requesting to enter an interior
 */
public final class C2STryEnterInteriorWorld extends GamePacket {

    private Interior interior;

    public C2STryEnterInteriorWorld(ByteBuf buffer) {
        super(buffer);
    }

    public C2STryEnterInteriorWorld(Interior type) {
        Preconditions.checkNotNull(type);
        this.interior = type;
    }

    /**
     * @return the interior type trying to enter.
     */
    public Interior type() {
        return interior;
    }

    @Override
    public int getId() {
        return Packets.C2S_TRY_ENTER_INTERIOR;
    }

    @Override
    public void encode() {
        writeId();
        buffer.writeInt(interior.ordinal());
    }

    @Override
    public void decode() {
        if (buffer.isReadable(4)) interior = Interior.of(buffer.readInt());
    }
}
