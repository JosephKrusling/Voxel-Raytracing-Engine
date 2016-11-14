package com.crosslink.battleprism.server.events.impl.incoming;

import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import com.crosslink.battleprism.server.events.IncomingEvent;
import com.crosslink.battleprism.server.session.Session;
import io.netty.buffer.ByteBuf;

import java.util.Vector;

/**
 * Created by Joseph on 8/10/2014.
 */
public class IncomingDestroyVoxel implements IncomingEvent {

    private Session session;
    private ByteBuf data;

    private boolean dataParsed;
    private Vec3I rootIndex;
    private Vec3I relativePosition;
    private Byte level;

    public IncomingDestroyVoxel(Session session, ByteBuf data) {
        this.session = session;
        this.data = data.copy();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public int getEventType() {
        return 0;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public ByteBuf getData() {
        return data;
    }

    @Override
    public void release() {
        if (data != null && data.refCnt() > 0)
            data.release();
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
