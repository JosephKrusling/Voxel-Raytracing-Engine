package com.crosslink.battleprism.client.networking;

import com.crosslink.battleprism.client.networking.pipeline.PipelineManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Joseph on 5/26/2014.
 */
public class NetClient implements Runnable {

    public static final String defaultHost = "74.140.132.67";
    public static final int defaultPort = 13371;

    public static boolean shouldTryConnect = true;

    public Channel channel;

    public void connect(String host, int port, int attemptTimeout) throws InterruptedException {

        while(shouldTryConnect) {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        PipelineManager manager = new PipelineManager();
                        manager.addHandlers(ch.pipeline());
                        channel = ch;
                    }

                });
                ChannelFuture future = bootstrap.connect(host, port).sync();
                future.channel().closeFuture().sync();
            }
            catch (Exception ex) {
                //System.out.println("Could not connect to server on port " + defaultPort);
                Thread.sleep(attemptTimeout);
            }
            finally {
                //System.out.println("Destroying worker group");
                workerGroup.shutdownGracefully();
            }
        }
    }

    @Override
    public void run() {
        try {
            connect(defaultHost, defaultPort, 500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void requestTerminate() {
        shouldTryConnect = false;
        // TODO: doesn't break connection, just stops future attempts
    }
}
