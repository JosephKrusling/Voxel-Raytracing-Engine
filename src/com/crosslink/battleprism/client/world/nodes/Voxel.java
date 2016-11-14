package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.core.containers.AxialDirection;
import com.crosslink.battleprism.core.containers.Color;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3;
import com.crosslink.battleprism.core.math.Vec3I;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Voxel extends Node {

    //region Constructors
    public Voxel(Chunk parent, Octant relativePosition){
        super(parent, relativePosition, parent.level - 1);
    }

    public Voxel(Chunk parent, Octant relativePosition, Color tint){
        super(parent, relativePosition, parent.level - 1);
    }
    //endregion

    //region Public Methods
    public Chunk split() {
        Chunk chunk = parent.createChildChunk(octant);
        chunk.createChildVoxel(Octant.TOP_FRONT_RIGHT);
        chunk.createChildVoxel(Octant.TOP_BACK_RIGHT);
        chunk.createChildVoxel(Octant.TOP_BACK_LEFT);
        chunk.createChildVoxel(Octant.TOP_FRONT_LEFT);
        chunk.createChildVoxel(Octant.BOTTOM_FRONT_RIGHT);
        chunk.createChildVoxel(Octant.BOTTOM_BACK_RIGHT);
        chunk.createChildVoxel(Octant.BOTTOM_BACK_LEFT);
        chunk.createChildVoxel(Octant.BOTTOM_FRONT_LEFT);
        return chunk;
    }

    public void delete() {
        parent.deleteChild(octant);
        invalidateBuffer();
    }
}
