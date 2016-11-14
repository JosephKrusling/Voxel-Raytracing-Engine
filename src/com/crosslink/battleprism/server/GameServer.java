package com.crosslink.battleprism.server;

import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.server.session.SessionPipelineManager;
import com.crosslink.battleprism.server.session.Session;
import com.crosslink.battleprism.server.world.nodes.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;

/**
 * Created by Joseph on 5/3/2014.
 */
public class GameServer implements Runnable {

    private String host;
    private int port;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            new SessionPipelineManager(new Session()).addHandlers(ch.pipeline());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            //World.initialize();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        ArrayList<Voxel> splitQueue = new ArrayList<Voxel>();
        RootChunk root = new RootChunk(new Vec3I(0));

        for (int i = 0; i < 8; i++) {
            splitQueue.add(root.createChildVoxel(Octant.fromValue(i), false));
        }
        while (splitQueue.size() > 0) {
            Voxel voxel = splitQueue.get(0);
            if (voxel.level > 0) {
                Chunk chunk = voxel.split();
                for (int i = 0; i < 8; i++) {
                    splitQueue.add((Voxel) chunk.children[i]);
                }
            }
            splitQueue.remove(voxel);
        }
        ChunkMap.putRootChunk(root);
        try {
            new GameServer(13371).start();
        } catch (Exception ignored) {System.out.println(ignored.toString());}
    }

    @Override
    public void run() {

    }
}
