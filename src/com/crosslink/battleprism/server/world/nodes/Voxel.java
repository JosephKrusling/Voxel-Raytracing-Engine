package com.crosslink.battleprism.server.world.nodes;

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
        chunk.createChildVoxel(Octant.TOP_FRONT_RIGHT, false);
        chunk.createChildVoxel(Octant.TOP_BACK_RIGHT, false);
        chunk.createChildVoxel(Octant.TOP_BACK_LEFT, false);
        chunk.createChildVoxel(Octant.TOP_FRONT_LEFT, false);
        chunk.createChildVoxel(Octant.BOTTOM_FRONT_RIGHT, false);
        chunk.createChildVoxel(Octant.BOTTOM_BACK_RIGHT, false);
        chunk.createChildVoxel(Octant.BOTTOM_BACK_LEFT, false);
        chunk.createChildVoxel(Octant.BOTTOM_FRONT_LEFT, false);
        return chunk;
    }

    public Voxel extrude(AxialDirection direction) {
        return createVoxel(getAdjacentNodeLoc(direction), level, false);
    }

    public Voxel extrude(final AxialDirection direction, final Vec3 startFrom, final int level)
    {
        Vec3 searchPoint = new Vec3(startFrom);     // Create new reference so we don't affect original.
        searchPoint.x += direction.getX() * 0.5f;
        searchPoint.y += direction.getY() * 0.5f;
        searchPoint.z += direction.getZ() * 0.5f;

        Vec3I searchPointI = new Vec3I((int) Math.floor(searchPoint.x), (int) Math.floor(searchPoint.y), (int) Math.floor(searchPoint.z));
        NodePosition creationPoint = new NodePosition(new Vec3I(0), searchPointI);

        creationPoint.clampRelPosition();

        return createVoxel(creationPoint, level, true);
    }

    public boolean subtract(final AxialDirection direction, final Vec3 startFrom, final int level) {
        System.out.println("start subtract");
        Vec3 searchPoint = new Vec3(startFrom);     // Create new reference so we don't affect original.
        searchPoint.x -= direction.getX() * 0.5f;
        searchPoint.y -= direction.getY() * 0.5f;
        searchPoint.z -= direction.getZ() * 0.5f;

        Vec3I searchPointI = new Vec3I((int) Math.floor(searchPoint.x), (int) Math.floor(searchPoint.y), (int) Math.floor(searchPoint.z));
        NodePosition deletionPoint = new NodePosition(new Vec3I(0), searchPointI);

        deletionPoint.clampRelPosition();

        return deleteVoxel(deletionPoint, level, true);
    }

    //endregion

//    @Override
//    public void resetOcclusion() {
//        for (int i = 0; i < 6; i++)
//            occlusionAmount[i] = 0;
//    }
//
//    @Override
//    public void calculateOcclusion() {
//        for (int i = 0; i < 6; i++) {
//            AxialDirection searchDirection = AxialDirection.fromInteger(i);
//            NodePosition searchPosition = getAdjacentNodeLoc(searchDirection);
//            Node searchResult = startNodeSearch(searchPosition, level);
//            if (searchResult == null)
//                continue;
//            if (searchResult instanceof Chunk)
//                continue;
//            Voxel foundVoxel = (Voxel) searchResult;
//            if (foundVoxel.level >= level)
//            {
//                occlusionAmount[searchDirection.getValue()] += foundVoxel.diameter * foundVoxel.diameter;
//                foundVoxel.occlusionAmount[AxialDirection.getOppositeFace(searchDirection).getValue()] += diameter * diameter;
//            }
//       }
//    }

//    public boolean isSideOccluded(int sideIndex){
//        return (occlusionAmount[sideIndex] >= diameter*diameter);
//    }
}
