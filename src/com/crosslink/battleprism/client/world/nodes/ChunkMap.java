package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.client.engine.Quad2D;
import com.crosslink.battleprism.client.engine.shaders.ShaderPrograms;
import com.crosslink.battleprism.core.containers.Ray;
import com.crosslink.battleprism.core.math.Vec3I;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChunkMap {

    // Global static constants
    public static final int OT_N_LEVELS = 6;       // Number of possible levels in the octree.
    public static final int OT_ROOT_LEVEL = OT_N_LEVELS - 1;
    public static final int OT_ROOT_DIAMETER = (2 << OT_ROOT_LEVEL) / 2;
    public static final int OT_MAX_VOXELS = OT_ROOT_DIAMETER * OT_ROOT_DIAMETER * OT_ROOT_DIAMETER;

    public Vec3I worldCenterIndex = new Vec3I(0);      // The index of the root chunk that represents the center of the world.
    public HashMap<Vec3I, RootChunk> chunks = new HashMap<>();

    public RootChunk getRootChunk(Vec3I index) {
        return chunks.get(index);
    }

    public RootChunk getRootChunk(int x, int y, int z) {
        return getRootChunk(new Vec3I(x, y, z));
    }

    public void putRootChunk(RootChunk root) {
        chunks.put(root.worldIndex, root);
    }

    public void draw() {
        RootChunk testChunk = new RootChunk(new Vec3I(0,0,0));
        int i = 0;
        for (RootChunk chunk : chunks.values()) {
            if (chunk != null) {
                chunk.sendToGPU();
                testChunk = chunk;
            }
        }
        glActiveTexture(GL_TEXTURE0 + ShaderPrograms.RayTracer.Cache.bufferTexBindingIndex);
        glBindTexture(GL_TEXTURE_BUFFER, testChunk.chunkOctreeTex.TextureID );
        Quad2D.renderFullScreenNDCquad();
    }

    public NodeSearchResult getNearestVoxelInRay(Ray ray) {
        Comparator<NodeSearchResult> comparator = new NodeSearchResult.NodeDistanceComparator();
        PriorityQueue<NodeSearchResult> queue = new PriorityQueue<>(10, comparator);

        for (RootChunk child : chunks.values()) {
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

            RootChunk childChunk = (RootChunk) result.node;
            NodeSearchResult childTestResult = childChunk.getNearestVoxelInRay(ray);
            if (childTestResult != null)
                return childTestResult;
        }
        return null;
    }
}
