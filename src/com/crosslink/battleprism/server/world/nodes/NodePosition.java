package com.crosslink.battleprism.server.world.nodes;

import com.crosslink.battleprism.core.math.Vec3I;
import com.crosslink.battleprism.core.math.Vec3;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/28/13
 * Time: 11:12 PM
 * Represents the absolute position of a node within the game world.
 */
public class NodePosition {

    public Vec3I rootIndex;
    public Vec3I relPosition;
    private boolean aBoolean;

    public NodePosition() {
        this.rootIndex = new Vec3I();
        this.relPosition = new Vec3I();
    }

    public NodePosition(NodePosition nodePosition) {
        this.rootIndex = new Vec3I(nodePosition.rootIndex);
        this.relPosition = new Vec3I(nodePosition.relPosition);
    }

    public NodePosition(Vec3I rootIndex, Vec3I relPosition) {
        this.rootIndex = rootIndex;
        this.relPosition = relPosition;
    }

    public NodePosition(Vec3I rootIndex, Vec3I relPosition, boolean deepCopy) {
        if (deepCopy) {
            this.rootIndex = new Vec3I(rootIndex);
            this.relPosition = new Vec3I(relPosition);
        } else {
            this.rootIndex = rootIndex;
            this.relPosition = relPosition;
        }
    }

    //-------------------------------------------------------------------------------
    //  Determines the absolute render position relative to the world center root node.
    //-------------------------------------------------------------------------------
    public Vec3 getRenderPosition() {
        Vec3I relRootIndex = Vec3I.subtract(rootIndex, ChunkMap.worldCenterIndex);
        return new Vec3(relRootIndex.x * ChunkMap.OT_ROOT_DIAMETER + relPosition.x,
                relRootIndex.y * ChunkMap.OT_ROOT_DIAMETER + relPosition.y,
                relRootIndex.z * ChunkMap.OT_ROOT_DIAMETER + relPosition.z);
    }

    public void clampRelPosition() {
        while (relPosition.x < 0) {
            relPosition.x += ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.x -= 1;
        }
        while (relPosition.y < 0) {
            relPosition.y += ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.y -= 1;
        }
        while (relPosition.z < 0) {
            relPosition.z += ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.z -= 1;
        }

        while (relPosition.x >= ChunkMap.OT_ROOT_DIAMETER) {
            relPosition.x -= ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.x += 1;
        }
        while (relPosition.y >= ChunkMap.OT_ROOT_DIAMETER) {
            relPosition.y -= ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.y += 1;
        }
        while (relPosition.z >= ChunkMap.OT_ROOT_DIAMETER) {
            relPosition.z -= ChunkMap.OT_ROOT_DIAMETER;
            rootIndex.z += 1;
        }
    }

    @Override
    public String toString() {
        return String.format("{Index:%s, RelPos:%s}", rootIndex.toString(), relPosition.toString());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + rootIndex.hashCode();
        result = result * 31 + relPosition.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (aBoolean)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof NodePosition))
            return false;
        NodePosition npos = (NodePosition) obj;
        if (!this.rootIndex.equals(npos.rootIndex))
            return false;
        if (!this.relPosition.equals(npos.relPosition))
            return false;
        return true;

    }

}
