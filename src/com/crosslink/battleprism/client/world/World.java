package com.crosslink.battleprism.client.world;

import com.crosslink.battleprism.client.interfaces.GameComponent;
import com.crosslink.battleprism.client.world.nodes.*;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3I;

import java.util.ArrayList;

/**
 * Created by Joseph on 8/5/2014.
 */
public class World implements GameComponent {

    public static ChunkMap map;

    @Override
    public void initialize() {
        map = new ChunkMap();

        ArrayList<Voxel> splitQueue = new ArrayList<Voxel>();
        RootChunk root = new RootChunk(new Vec3I(0));

        for (int i = 0; i < 8; i++) {
            splitQueue.add(root.createChildVoxel(Octant.fromValue(i)));
        }
        while (splitQueue.size() > 0) {
            Voxel voxel = splitQueue.get(0);
            if (voxel.level > 0) {
                Chunk chunk = voxel.split();
                for (int i = 0; i < 8; i++) {
                    splitQueue.add((Voxel) chunk.children[i]);
                }
            }
            splitQueue.remove(voxel);
        }
        map.putRootChunk(root);
    }

    @Override
    public void update(float lastFrameDuration) {
    }

    @Override
    public void draw(float lastFrameDuration) {
    }

    @Override
    public void resized(int width, int height) {
    }

}
