package com.crosslink.battleprism.server.world.nodes.serialization;

import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.server.world.nodes.*;
import com.crosslink.battleprism.core.containers.Color;
import com.crosslink.battleprism.core.containers.serialization.ColorSerializer;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import io.netty.buffer.ByteBuf;

/**
* Created by Joseph on 5/24/2014.
*/
public class NodeSerializer {

    public static final byte VOXEL = 0x01;
    public static final byte CHILDCHUNK = 0x02;
    public static final byte ROOTCHUNK = 0x03;

    public static ByteBuf marshalNode(ByteBuf buffer, Node node) {
        if (node instanceof Voxel)
            return marshalVoxel(buffer, (Voxel) node);
        else
            return marshalChunk(buffer, (Chunk) node);

    }

    public static ByteBuf marshalVoxel(ByteBuf buffer, Voxel voxel) {
        buffer.retain();

        buffer.writeByte(VOXEL);
        buffer.writeByte(voxel.octant.getValue());
        ColorSerializer.marshalColor(buffer, voxel.getColor()); // TODO: this probably doesnt work. fix marshal and unmarshal

        buffer.release();
        return buffer;
    }

    public static ByteBuf marshalChunk(ByteBuf buffer, Chunk chunk) {
        buffer.retain();

        buffer.writeByte(CHILDCHUNK);
        buffer.writeByte(chunk.octant.getValue());
        buffer.writeByte(chunk.childCount());
        for (int i=0; i<8; i++) {
            if (chunk.children[i] == null)
                continue;
            if (chunk.children[i] instanceof Voxel)
                marshalVoxel(buffer, (Voxel) chunk.children[i]);
            else
                marshalChunk(buffer, (Chunk) chunk.children[i]);
        }

        buffer.release();
        return buffer;
    }

    public static ByteBuf marshalRootChunk(ByteBuf buffer, RootChunk chunk) {
        buffer.retain();

        buffer.writeByte(ROOTCHUNK);
        buffer.writeByte(chunk.childCount());
        VectorSerializer.marshalVec3I(buffer, chunk.position.rootIndex);
        for (int i=0; i<8; i++) {
            if (chunk.children[i] == null)
                continue;
            if (chunk.children[i] instanceof Voxel)
                marshalVoxel(buffer, (Voxel) chunk.children[i]);
            else
                marshalChunk(buffer, (Chunk) chunk.children[i]);
        }

        buffer.release();
        return buffer;
    }
}
