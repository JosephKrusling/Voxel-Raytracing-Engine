package com.crosslink.battleprism.client.networking.pipeline;

import com.crosslink.battleprism.client.networking.events.IncomingEvent;
import com.crosslink.battleprism.client.networking.events.impl.incoming.IncomingDestroyVoxel;
import com.crosslink.battleprism.client.world.World;
import com.crosslink.battleprism.client.world.nodes.AbstractNodePosition;
import com.crosslink.battleprism.client.world.nodes.RootChunk;
import com.crosslink.battleprism.client.world.serialization.NodeSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Joseph on 7/14/2014.
 */
public class EventDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decoding event" + in.readableBytes());
        if (!in.isReadable())
            return;

        int eventCode = in.readUnsignedByte();
        ByteBuf buffer = in.readBytes(in.readableBytes());

        switch (eventCode) {
            case (0):
                System.out.println("got it");

                IncomingDestroyVoxel destroyVoxelEvent = new IncomingDestroyVoxel(buffer);
                System.out.println(destroyVoxelEvent.getRootIndex());
                System.out.println(destroyVoxelEvent.getRelativePosition());
                System.out.println(destroyVoxelEvent.getLevel());
                World.map.getRootChunk(destroyVoxelEvent.getRootIndex()).deleteVoxel(new AbstractNodePosition(destroyVoxelEvent.getRootIndex(), destroyVoxelEvent.getRelativePosition()), destroyVoxelEvent.getLevel(), false);
                destroyVoxelEvent.release();
                break;
            default:
                break;
        }
    }
}
