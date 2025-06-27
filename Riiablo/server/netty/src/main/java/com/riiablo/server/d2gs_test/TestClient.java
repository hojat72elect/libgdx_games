package com.riiablo.server.d2gs_test;

import com.google.flatbuffers.FlatBufferBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.SocketAddress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;

import com.riiablo.codec.Animation;
import com.riiablo.net.packet.netty.Connection;
import com.riiablo.net.packet.netty.Disconnect;
import com.riiablo.net.packet.netty.Netty;
import com.riiablo.net.packet.netty.NettyData;
import com.riiablo.onet.EndpointedChannelHandler;
import com.riiablo.onet.PacketProcessor;
import com.riiablo.onet.UnicastEndpoint;
import com.riiablo.onet.reliable.QoS;
import com.riiablo.onet.tcp.TcpEndpoint;

public class TestClient extends ApplicationAdapter implements PacketProcessor {
  private static final String TAG = "Client";

  public static void main(String[] args) throws Exception {
    Thread.sleep(1000);
    HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
    config.updatesPerSecond = (int) Animation.FRAMES_PER_SECOND;
    new HeadlessApplication(new TestClient(), config);
  }

  private UnicastEndpoint<?> endpoint;
  private EventLoopGroup group;
  private long pingSent;

  @Override
  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              UnicastEndpoint<ByteBuf> endpoint = new TcpEndpoint(ch, TestClient.this);
              TestClient.this.endpoint = endpoint;
              ch.pipeline()
                  .addLast(new EndpointedChannelHandler<>(ByteBuf.class, endpoint))
              ;
            }
          });

      ChannelFuture f = b.connect("localhost", Main.PORT).sync();
      sendConnectionPacket();
//      sendConnectionPacket();
//      sendDisconnectPacket();
    } catch (Throwable t) {
      Gdx.app.error(TAG, t.getMessage(), t);
      Gdx.app.exit();
    }
  }

  private void sendConnectionPacket() {
    SocketAddress remoteAddress = endpoint.channel().remoteAddress();
    Gdx.app.log(TAG, "Sending Connection packet to " + remoteAddress);

    long salt = MathUtils.random.nextLong();
    FlatBufferBuilder builder = new FlatBufferBuilder();
    Connection.startConnection(builder);
    Connection.addSalt(builder, salt);
    int dataOffset = Connection.endConnection(builder);
    int offset = Netty.createNetty(builder, salt, NettyData.Connection, dataOffset);
    Netty.finishSizePrefixedNettyBuffer(builder, offset);

    endpoint.sendMessage(QoS.Unreliable, builder.dataBuffer());
  }

  private void sendDisconnectPacket() {
    SocketAddress remoteAddress = endpoint.channel().remoteAddress();
    Gdx.app.log(TAG, "Sending Disconnect packet to " + remoteAddress);

    FlatBufferBuilder builder = new FlatBufferBuilder();
    Disconnect.startDisconnect(builder);
    int dataOffset = Disconnect.endDisconnect(builder);
    int offset = Netty.createNetty(builder, 0L, NettyData.Disconnect, dataOffset);
    Netty.finishSizePrefixedNettyBuffer(builder, offset);

    endpoint.sendMessage(QoS.Unreliable, builder.dataBuffer());
  }

  @Override
  public void render() {
    endpoint.update(Gdx.graphics.getDeltaTime());
  }

  @Override
  public void dispose() {
    if (!group.isShuttingDown()) group.shutdownGracefully();
  }

  @Override
  public void processPacket(ChannelHandlerContext ctx, SocketAddress from, ByteBuf bb) {
    Gdx.app.debug(TAG, "Processing packet...");
    Gdx.app.log(TAG, ByteBufUtil.hexDump(bb));
  }
}
