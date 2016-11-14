package com.crosslink.battleprism.core.containers.serialization;

import com.crosslink.battleprism.core.containers.Color;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 5/24/2014.
 */
public class ColorSerializer {

    public static ByteBuf marshalColor(ByteBuf buffer, Color color) {
        buffer.retain();

        buffer.writeByte(color.r);
        buffer.writeByte(color.g);
        buffer.writeByte(color.b);
        buffer.writeByte(color.a);

        buffer.release();
        return buffer;
    }

    public static Color unmarshalColor(ByteBuf buffer) {
        buffer.retain();

        Color c = new Color(buffer.readByte(), buffer.readByte(), buffer.readByte(), buffer.readByte());

        buffer.release();
        return c;
    }

}
