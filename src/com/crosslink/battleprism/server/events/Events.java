package com.crosslink.battleprism.server.events;

import com.crosslink.battleprism.server.session.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Joseph on 5/17/2014.
 */
public class Events {

    public static class Incoming {
        public static final byte DESTROY_VOXEL = 0x00;

        public static final byte REQUEST_CHUNK = 0x02;
    }

    public static class Outgoing {
        public static final byte DESTROY_VOXEL = 0x00;
        public static final byte SEND_CHUNK = 0x01;
    }

    public static void event(IncomingEvent event) {
        event.getSession().event(event);
    }
}
