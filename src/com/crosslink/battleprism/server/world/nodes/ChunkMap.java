package com.crosslink.battleprism.server.world.nodes;

import com.crosslink.battleprism.core.math.Vec3I;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChunkMap {
    public static final int OT_N_LEVELS = 6;       // Number of possible levels in the octree.
    public static final int OT_ROOT_LEVEL = OT_N_LEVELS - 1;
    public static final int OT_ROOT_DIAMETER = (2 << OT_ROOT_LEVEL) / 2;
    public static final int OT_MAX_VOXELS = OT_ROOT_DIAMETER * OT_ROOT_DIAMETER * OT_ROOT_DIAMETER;

    public static Vec3I worldCenterIndex = new Vec3I(0);      // The index of the root chunk that represents the center of the world.

    public static HashMap<Vec3I, RootChunk> chunks = new HashMap<>();

    public static void putRootChunk(RootChunk chunk) {
        chunks.put(chunk.position.rootIndex, chunk);
    }

    public static RootChunk getRootChunk(Vec3I index) {
        return chunks.get(index);
    }

    public static RootChunk getRootChunk(int x, int y, int z) {
        return getRootChunk(new Vec3I(x, y, z));
    }
}
