package com.crosslink.battleprism.client.world.serialization;

import com.crosslink.battleprism.client.world.nodes.Chunk;
import com.crosslink.battleprism.client.world.nodes.Node;
import com.crosslink.battleprism.client.world.nodes.RootChunk;
import com.crosslink.battleprism.client.world.nodes.Voxel;
import com.crosslink.battleprism.core.containers.Color;
import com.crosslink.battleprism.core.containers.serialization.ColorSerializer;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.serialization.VectorSerializer;
import io.netty.buffer.ByteBuf;

/**
 * Created by Joseph on 7/17/2014.
 */
public class NodeSerializer {

    public static final byte VOXEL = 0x01;
    public static final byte CHILDCHUNK = 0x02;
    public static final byte ROOTCHUNK = 0x03;

    public static Node unmarshalNode(ByteBuf buffer, Chunk parent) {
        buffer.retain();
        if (!buffer.isReadable())
            return null;

        Byte type = buffer.readByte();

        if (type.equals(VOXEL)) {
            byte octant = buffer.readByte();
            Color color = ColorSerializer.unmarshalColor(buffer);
            Voxel voxel = parent.createChildVoxel(Octant.fromValue(octant), color);
            buffer.release();
            return voxel;
        } else if (type.equals(CHILDCHUNK)) {
            byte octant = buffer.readByte();
            byte childCount = buffer.readByte();
            Chunk chunk = parent.createChildChunk(Octant.fromValue(octant));
            for (int i=0; i < childCount; i++)
                unmarshalNode(buffer, chunk);
            buffer.release();
            return chunk;
        }
        System.out.println("node type " + type + " is unknown");
        buffer.release();
        return null;
    }

    public static RootChunk unmarshalRoot(ByteBuf buffer) {
        buffer.retain();
        if (!buffer.isReadable())
            return null;

        Byte type = buffer.readByte();
        if (type != ROOTCHUNK) {
            System.out.println("Invalid rootchunk code " + type);
            return null;
        }

        byte childCount = buffer.readByte();
        Vec3I index = VectorSerializer.unmarshalVec3I(buffer);
        RootChunk root = new RootChunk(index);
        for (int i =0; i < childCount; i++)
            unmarshalNode(buffer, root);

        buffer.release();
        return root;
    }

}
