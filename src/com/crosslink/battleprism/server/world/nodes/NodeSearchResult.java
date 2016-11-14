package com.crosslink.battleprism.server.world.nodes;

import com.crosslink.battleprism.core.containers.Ray;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 1/26/14
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
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
