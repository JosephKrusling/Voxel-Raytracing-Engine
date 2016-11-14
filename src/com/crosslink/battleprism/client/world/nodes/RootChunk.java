package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.client.engine.VoxelEncoder;
import com.crosslink.battleprism.core.image.BufferTexture;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;

import static org.lwjgl.opengl.GL30.GL_R8I;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RootChunk extends Chunk {

    public Vec3I worldIndex;
    public ChunkMap parentMap;

    public boolean bufferValid = false;
    public BufferTexture chunkOctreeTex;

    public RootChunk(Vec3I rootChunkIndex) {
        super(null, Octant.PARENT, ChunkMap.OT_ROOT_LEVEL);
        worldIndex = new Vec3I(rootChunkIndex);
        position = new NodePosition(this, new Vec3I(0));
    }

    public void sendToGPU() {
        if (chunkOctreeTex == null)
            chunkOctreeTex = new BufferTexture(GL_R8I);
        if (!bufferValid) {
            VoxelEncoder.upLoadOctreeFromRootChunk(this, chunkOctreeTex);
            bufferValid = true;
        }
    }

    @Override
    public void invalidateBuffer() {
        bufferValid = false;
    }

//    @Override
//    public void calculateOcclusion() {
//        for (int index = 0; index < 8; index++) {
//            if(children[index] != null)
//                children[index].calculateOcclusion();
//        }
//    }

}
