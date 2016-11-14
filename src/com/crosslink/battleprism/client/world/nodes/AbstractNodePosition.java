package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.core.math.Vec3I;

/**
 * Created by Joseph on 8/7/2014.
 */
public class AbstractNodePosition {

    public Vec3I rootIndex;
    public Vec3I relPosition;

    public AbstractNodePosition()
    {
        rootIndex = new Vec3I();
        relPosition = new Vec3I();
    }

    public AbstractNodePosition(AbstractNodePosition nodePosition)
    {
        rootIndex = new Vec3I(nodePosition.rootIndex);
        relPosition = new Vec3I(nodePosition.relPosition);
    }

    public AbstractNodePosition(Vec3I rootIndex, Vec3I relPosition) {
        this.rootIndex = rootIndex;
        this.relPosition = relPosition;
    }

}
