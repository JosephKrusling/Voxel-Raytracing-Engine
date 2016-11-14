package com.crosslink.battleprism.client.networking.events.impl.outgoing;

import com.crosslink.battleprism.client.networking.events.Events;
import com.crosslink.battleprism.client.networking.events.OutgoingEvent;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Joseph on 8/12/2014.
 */
public class OutgoingRequestChunk implements OutgoingEvent {

    private long timestamp;
    private Vec3I desiredChunk;
    private byte desiredDetail;

    private ByteBuf data;

    public OutgoingRequestChunk(Vec3I desiredChunk, byte desiredDetail) {
        this.timestamp = System.currentTimeMillis();
        this.desiredChunk = desiredChunk;
        this.desiredDetail = desiredDetail;
    }

    @Override
    public int getEventType() {
        return Events.Outgoing.REQUEST_CHUNK;
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
        data = Unpooled.buffer();
        VectorSerializer.marshalVec3I(data, desiredChunk);
        data.writeByte(desiredDetail);
        return data;
    }

    @Override
    public void release() {
        if (data != null && data.refCnt() > 0)
            data.release();
        data = null;
    }
}
