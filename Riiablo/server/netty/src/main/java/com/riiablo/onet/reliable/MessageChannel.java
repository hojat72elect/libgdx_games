package com.riiablo.onet.reliable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import java.net.SocketAddress;

public abstract class MessageChannel implements ReliablePacketController.PacketListener {
  protected final PacketTransceiver packetTransceiver;
  protected final ReliableConfiguration config;
  protected final ReliablePacketController packetController;

  protected int sequence;

  public MessageChannel(ReliableConfiguration config, PacketTransceiver packetTransceiver) {
    this.packetTransceiver = packetTransceiver;
    this.config = config;
    this.packetController = new ReliablePacketController(config, this);
  }

  public int nextSequence() {
    return sequence;
  }

  protected int incSequence() {
    return sequence = (sequence + 1) & Packet.USHORT_MAX_VALUE;
  }

  protected ReliablePacketController controller() {
    return packetController;
  }

  public abstract void reset();
  public abstract void update(float delta, int channelId, DatagramChannel ch);
  public abstract void sendMessage(int channelId, DatagramChannel ch, ByteBuf bb);
  public abstract void onMessageReceived(ChannelHandlerContext ctx, DatagramPacket packet);

  public interface PacketTransceiver {
    void sendPacket(ByteBuf bb);
    void receivePacket(ChannelHandlerContext ctx, SocketAddress from, ByteBuf bb);
  }
}
