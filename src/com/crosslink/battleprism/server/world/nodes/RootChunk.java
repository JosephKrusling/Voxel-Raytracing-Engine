package com.crosslink.battleprism.server.world.nodes;

import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RootChunk extends Chunk {

    public RootChunk(Vec3I rootChunkIndex) {
        super(null, Octant.PARENT, ChunkMap.OT_ROOT_LEVEL);
        position.rootIndex = rootChunkIndex;
    }

//    @Override
//    public void calculateOcclusion() {
//        for (int index = 0; index < 8; index++) {
//            if(children[index] != null)
//                children[index].calculateOcclusion();
//        }
//    }
}
