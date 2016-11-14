package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.core.containers.Color;
import com.crosslink.battleprism.core.containers.Ray;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */

public class Chunk extends Node {
    public Node[] children = new Node[8];

    public Chunk(Chunk parent, Octant octant, int level){
        super(parent, octant, level);
    }

    public Node searchForNode(NodePosition searchPosition, int minLevel, boolean maySearchUp) {
        if (searchPosition.relPosition.isInBounds(position.relPosition, Vec3I.add(position.relPosition, diameter))) {
            if (minLevel == this.level)
                return this; // We found the exact node.
            for (Node child : children) {
                if (child == null)
                    continue;
                Node result = child.searchForNode(searchPosition, minLevel, false);
                if (result != null)
                    return result;
            }
            return this; // We found a containing node.
        } else {
            if (maySearchUp)
                return parent.searchForNode(searchPosition, minLevel, true);
            else
                return null;
        }
    }

    //-------------------------------------------------------------------------------
    //  Creates a new child chunk for this chunk in the specified octant.
    //-------------------------------------------------------------------------------
    public Chunk createChildChunk(Octant octant) {
        Chunk child = new Chunk(this, octant, level - 1);
        children[octant.getValue()] = child;
        return child;
    }

    //-------------------------------------------------------------------------------
    //  Creates a new voxel in this chunk in the specified octant.
    //-------------------------------------------------------------------------------
    public Voxel createChildVoxel(Octant octant) {
        Voxel child = new Voxel(this, octant);
        children[octant.getValue()] = child;
        return child;
    }

    public Voxel createChildVoxel(Octant octant, Color tint) {
        Voxel child = new Voxel(this, octant, tint);
        children[octant.getValue()] = child;
        return child;
    }

    public boolean deleteChild(Octant octant) {
        if (children[octant.getValue()] == null)
            return false;
        children[octant.getValue()] = null;
        invalidateBuffer();
        return true;
    }

    public int childCount() {
        int count = 0;
        for (int i = 0; i < 8; i++)
            if (children[i] != null)
                count ++;
        return count;
    }

    public NodeSearchResult getNearestVoxelInRay(Ray ray) {
        Comparator<NodeSearchResult> comparator = new NodeSearchResult.NodeDistanceComparator();
        PriorityQueue<NodeSearchResult> queue = new PriorityQueue<>(10, comparator);


        for (Node child : children) {
            if (child == null)
                continue;
            Ray.RayResult out = new Ray.RayResult();
            if (child.isIntersectingRay(ray, out)) {
                NodeSearchResult searchResult = new NodeSearchResult();
                searchResult.node = child;
                searchResult.rayResult = out;
                queue.add(searchResult);
            }
        }

        while (queue.size() != 0) {
            NodeSearchResult result = queue.remove();

            if (result.node instanceof Voxel) {
                if(result.rayResult.distance >= 0)
                {
                    if (result.rayResult.distance >=0)
                        return result;
                    else
                        continue;
                }
            }
            else {
                Chunk childChunk = (Chunk) result.node;
                NodeSearchResult childTestResult = childChunk.getNearestVoxelInRay(ray);
                if (childTestResult != null)
                    return childTestResult;
            }
        }
        return null;
    }
}
