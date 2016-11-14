package com.crosslink.battleprism.server.pipeline;

import com.crosslink.battleprism.server.events.OutgoingEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by Joseph on 5/26/2014.
 */
public class EventEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("encoding event");
        OutgoingEvent event = (OutgoingEvent) msg;

        int messageCode = event.getEventType();

        ByteBuf encoded = Unpooled.buffer();
        encoded.writeByte(messageCode);
        if (event.hasByteData())
            encoded.writeBytes(event.toByteData());

        ctx.write(encoded);
    }
}
