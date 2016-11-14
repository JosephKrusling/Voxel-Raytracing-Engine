package com.crosslink.battleprism.core.math.serialization;

import com.crosslink.battleprism.core.math.Vec3I;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 5/24/2014.
 */
public class VectorSerializer {

    public static ByteBuf marshalVec3I(ByteBuf buffer, Vec3I vector) {
        buffer.retain();

        buffer.writeInt(vector.x);
        buffer.writeInt(vector.y);
        buffer.writeInt(vector.z);

        buffer.release();
        return buffer;
    }

    public static Vec3I unmarshalVec3I(ByteBuf buffer) {
        buffer.retain();

        if (buffer.readableBytes() < 12)
            return null;
        Vec3I newVector = new Vec3I(buffer.readInt(), buffer.readInt(), buffer.readInt());

        buffer.release();
        return newVector;
    }

}
