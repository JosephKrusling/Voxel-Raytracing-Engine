package com.crosslink.battleprism.server.events.impl.outgoing;

import com.crosslink.battleprism.server.events.OutgoingEvent;
import com.crosslink.battleprism.server.world.nodes.RootChunk;
import com.crosslink.battleprism.server.world.nodes.serialization.NodeSerializer;
import com.crosslink.battleprism.server.events.Events;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Joseph on 5/31/2014.
 */
public class OutgoingSendChunk implements OutgoingEvent {

    private long timestamp;


    public OutgoingSendChunk() {
        timestamp = System.currentTimeMillis();
    }

    @Override
    public int getEventType() {
        return Events.Outgoing.SEND_CHUNK;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean hasByteData() {
        return true;
    }

    @Override
    public ByteBuf toByteData() {
        return null;
    }

    @Override
    public void release() {

    }
}
