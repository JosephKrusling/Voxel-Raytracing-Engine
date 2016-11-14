package com.crosslink.battleprism.client.networking.events.impl.incoming;

import com.crosslink.battleprism.client.networking.events.Events;
import com.crosslink.battleprism.client.networking.events.IncomingEvent;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 8/10/2014.
 */
public class IncomingDestroyVoxel implements IncomingEvent {

    private long timestamp;
    private ByteBuf data;
    private boolean dataParsed;
    private Vec3I rootIndex;
    private Vec3I relativePosition;
    private Byte level;

    public IncomingDestroyVoxel(ByteBuf data) {
        this.timestamp = System.currentTimeMillis();
        this.data = data.copy();
    }


    @Override
    public int getEventType() {
        return Events.Incoming.DESTROY_VOXEL;
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

    public Vec3I getRootIndex() {
        if (!dataParsed)
            parseData();
        return rootIndex;
    }

    public Vec3I getRelativePosition() {
        if (!dataParsed)
            parseData();
        return relativePosition;
    }

    public byte getLevel() {
        if (!dataParsed)
            parseData();
        return level;
    }

    private void parseData() {
        if (dataParsed)
            return;
        rootIndex = VectorSerializer.unmarshalVec3I(data);
        relativePosition = VectorSerializer.unmarshalVec3I(data);
        level = data.readByte();
        dataParsed = true;
    }
}
