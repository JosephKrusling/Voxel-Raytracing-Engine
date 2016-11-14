package com.crosslink.battleprism.client.networking.events.impl.incoming;

import com.crosslink.battleprism.client.networking.events.Events;
import com.crosslink.battleprism.client.networking.events.IncomingEvent;
import com.crosslink.battleprism.client.world.nodes.RootChunk;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 7/14/2014.
 */
public class IncomingSendChunk implements IncomingEvent {

    private long timestamp;
    private ByteBuf data;
    private boolean dataParsed;
    private RootChunk rootChunk;

    public IncomingSendChunk(ByteBuf data) {
        this.timestamp = System.currentTimeMillis();
        this.data = data.copy();
    }

    @Override
    public int getEventType() {
        return Events.Incoming.SEND_CHUNK;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public ByteBuf getData() {
        return data;
    }

    @Override
    public void release() {
        if (data != null && data.refCnt() > 0)
            data.release();
        data = null;
    }

    public RootChunk getRootChunk() {
        if (!dataParsed)
            parseData();
        return rootChunk;
    }

    private void parseData() {
        if (dataParsed)
            return;
    }
}
