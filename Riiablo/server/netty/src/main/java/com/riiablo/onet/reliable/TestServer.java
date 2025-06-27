package com.riiablo.onet.reliable;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

import com.riiablo.codec.Animation;
import com.riiablo.net.packet.netty.Netty;
import com.riiablo.net.packet.netty.NettyData;
import com.riiablo.onet.Endpoint;
import com.riiablo.onet.EndpointedChannelHandler;
import com.riiablo.onet.PacketProcessor;

public class TestServer extends ApplicationAdapter implements PacketProcessor {
  private static final String TAG = "Server";

  static final int PORT = 6114;

  public static void main(String[] args) {
    HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
    config.updatesPerSecond = (int) Animation.FRAMES_PER_SECOND;
    new HeadlessApplication(new TestServer(), config);
  }

  private Endpoint<?> endpoint;
  private EventLoopGroup group;

  @Override
  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap()
          .group(group)
          .channel(NioDatagramChannel.class)
          .option(ChannelOption.SO_BROADCAST, true)
          .handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) {
              ReliableEndpoint endpoint = new ReliableEndpoint(ch, TestServer.this);
              TestServer.this.endpoint = endpoint;
              ch.pipeline()
                  .addLast(new EndpointedChannelHandler<>(DatagramPacket.class, endpoint))
                  ;
            }
          })
          ;

      ChannelFuture f = b.bind(PORT).sync();
    } catch (Throwable t) {
      Gdx.app.error(TAG, t.getMessage(), t);
      Gdx.app.exit();
    }
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

    ByteBuffer nioBuffer = bb.nioBuffer();
    Netty netty = Netty.getRootAsNetty(nioBuffer);

    byte dataType = netty.dataType();
    if (0 <= dataType && dataType < NettyData.names.length) {
      Gdx.app.debug(TAG, "dataType=" + NettyData.name(dataType));
    }
  }
}
