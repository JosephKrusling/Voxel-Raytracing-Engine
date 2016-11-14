package com.crosslink.battleprism.server.events;

import com.crosslink.battleprism.server.session.Session;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 5/24/2014.
 */
public interface IncomingEvent {

    public Session getSession();
    public int getEventType();
    public long getTimestamp();
    public ByteBuf getData();
    public void release();
}
