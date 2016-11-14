package com.crosslink.battleprism.server.pipeline;

import com.crosslink.battleprism.server.events.Events;
import com.crosslink.battleprism.server.events.IncomingEvent;
import com.crosslink.battleprism.server.events.impl.incoming.IncomingDestroyVoxel;
import com.crosslink.battleprism.server.session.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Joseph on 5/16/2014.
 */
public class EventDecoder extends ByteToMessageDecoder {

    public static ByteBuf test = null;
    Session session;

    public EventDecoder(Session session) {
        this.session = session;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable())
            return;

        int eventCode = in.readUnsignedByte();
        ByteBuf buffer = in.readBytes(in.readableBytes());

        IncomingEvent event;
        switch (eventCode) {
            case 0:
                event = new IncomingDestroyVoxel(session, buffer);
                break;
            default:
                event = null;
                break;
        }
        Events.event(event);
    }

}
