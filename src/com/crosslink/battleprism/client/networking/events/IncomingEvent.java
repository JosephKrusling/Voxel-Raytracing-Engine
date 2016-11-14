package com.crosslink.battleprism.client.networking.events;

import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 5/24/2014.
 */
public interface IncomingEvent {

    public int getEventType();
    public long getTimestamp();
    public ByteBuf getData();
    public void release();
}
