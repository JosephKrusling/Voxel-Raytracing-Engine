package com.crosslink.battleprism.client.world.nodes;

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

    public RootChunk root;
    public Vec3I relPosition;

    public NodePosition() {
        this.root = null;
        this.relPosition = new Vec3I();
    }

    public NodePosition(NodePosition nodePosition) {
        this.root = nodePosition.root;
        this.relPosition = new Vec3I(nodePosition.relPosition);
    }

    public NodePosition(RootChunk root, Vec3I relPosition) {
        this.root = root;
        this.relPosition = relPosition;
    }

    //-------------------------------------------------------------------------------
    //  Determines the absolute render position relative to the world center root node.
    //-------------------------------------------------------------------------------
    public Vec3 getRenderPosition() {
        Vec3I relRootIndex = root.worldIndex;
        return new Vec3(relRootIndex.x * ChunkMap.OT_ROOT_DIAMETER + relPosition.x,
                relRootIndex.y * ChunkMap.OT_ROOT_DIAMETER + relPosition.y,
                relRootIndex.z * ChunkMap.OT_ROOT_DIAMETER + relPosition.z);
    }

    public void clampRelPosition() {
        Vec3I rootIndex;
        if (root == null)
            rootIndex = new Vec3I(0);
        else
            rootIndex = new Vec3I(root.worldIndex);

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
        return String.format("{Index:%s, RelPos:%s}", root.toString(), relPosition.toString());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + root.worldIndex.hashCode();
        result = result * 31 + relPosition.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof NodePosition))
            return false;
        NodePosition npos = (NodePosition) obj;
        if (!this.root.worldIndex.equals(npos.root.worldIndex))
            return false;
        if (!this.relPosition.equals(npos.relPosition))
            return false;
        return true;

    }

}
