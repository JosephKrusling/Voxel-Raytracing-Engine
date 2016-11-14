package com.crosslink.battleprism.client.networking.events.impl.outgoing;

import com.crosslink.battleprism.client.networking.events.Events;
import com.crosslink.battleprism.client.networking.events.OutgoingEvent;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Joseph on 8/9/2014.
 */
public class OutgoingDestroyVoxel implements OutgoingEvent {

    private long timestamp;
    private Vec3I rootIndex;
    private Vec3I relativePosition;
    private byte level;

    private ByteBuf data;

    public OutgoingDestroyVoxel(Vec3I rootIndex, Vec3I relativePosition, byte level) {
        this.timestamp = System.currentTimeMillis();
        this.rootIndex = new Vec3I(rootIndex);
        this.relativePosition = new Vec3I(relativePosition);
        this.level = level;
    }

    @Override
    public int getEventType() {
        return Events.Outgoing.DESTROY_VOXEL;
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
        VectorSerializer.marshalVec3I(data, rootIndex);
        VectorSerializer.marshalVec3I(data, relativePosition);
        data.writeByte(level);
        return data;
    }

    @Override
    public void release() {
        if (data != null && data.refCnt() > 0)
            data.release();
        data = null;
    }
}
