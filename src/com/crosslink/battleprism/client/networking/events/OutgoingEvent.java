package com.crosslink.battleprism.client.networking.events;

import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 5/17/2014.
 */
public interface OutgoingEvent {

    public int getEventType();
    public long getTimestamp();
    public boolean hasByteData();
    public ByteBuf toByteData();
    public void release();

}
