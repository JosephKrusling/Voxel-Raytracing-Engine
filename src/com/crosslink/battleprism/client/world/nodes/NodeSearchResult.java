package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.core.containers.Ray;

import java.util.Comparator;

/**
 * Created by Joseph on 8/9/2014.
 */
public class NodeSearchResult {
    public Node node;
    public Ray.RayResult rayResult;

    public static class NodeDistanceComparator implements Comparator<NodeSearchResult>
    {
        @Override
        public int compare(NodeSearchResult x, NodeSearchResult y) {
            if (x.rayResult.distance < y.rayResult.distance)
                return -1;
            if (x.rayResult.distance > y.rayResult.distance)
                return 1;
            return 0;
        }
    }
}