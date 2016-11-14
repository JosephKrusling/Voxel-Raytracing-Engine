package com.crosslink.battleprism.client.engine;


import com.crosslink.battleprism.client.world.nodes.*;
import com.crosslink.battleprism.core.image.BufferTexture;
import com.crosslink.battleprism.core.math.Vec3I;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;

/**
 * Created by Thomas on 1/7/14.
 */
public class VoxelEncoder {
    static ByteBuffer octreeBuffer = BufferUtils.createByteBuffer(9665536);
    static Queue<NodeContainer> q = new ArrayBlockingQueue<>(ChunkMap.OT_MAX_VOXELS + 1);
    static int nextAvailableLoc = 0;

    public static void upLoadOctreeFromRootChunk(RootChunk chunk, BufferTexture texture) {
        octreeBuffer.clear();

        if(beenInit == false) initOctreeUploader(ChunkMap.OT_N_LEVELS);
//        octreeBuffer.position(0);
//        octreeBuffer.limit((powOf8[maxLevel] + octreeLevelOffset[maxLevel])/8 + 1);
//        for(int i = 0; i < (powOf8[maxLevel] + octreeLevelOffset[maxLevel])/8 + 1; i++) {
//            octreeBuffer.put((byte)0);
//        }
        encodeNodeAndChildren(chunk, new Vec3I(0), 0);
        octreeBuffer.position(0);
        octreeBuffer.limit(100);
        System.out.println("encoding: ");
        while(octreeBuffer.position() < 10) {
            System.out.print(Integer.toBinaryString(octreeBuffer.get() & 0xFF).replace(' ', '0') + " | ");
        }

        octreeBuffer.position(0);
        octreeBuffer.limit((powOf8[maxLevel] + octreeLevelOffset[maxLevel])/8 + 1);

        texture.gpuBuffer.bind(GL_TEXTURE_BUFFER);
        texture.gpuBuffer.submitData(0, octreeBuffer);     //   System.out.println("error: " + glGetError());

    }
    static Boolean beenInit = false;
    public static void initOctreeUploader(int numOfLevels) {
        maxLevel = numOfLevels-1;
        powOf8 = new int[numOfLevels];
        powOf2 = new int[numOfLevels];
        octreeLevelOffset = new int[numOfLevels];
        powOf8[0] = 1;
        for(int i = 1; i < numOfLevels; i++) {
            powOf8[i] = powOf8[i-1] * 8;
        }
        powOf2[0] = 1;
        for(int i = 1; i < numOfLevels; i++) {
            powOf2[i] = powOf2[i-1] * 2;
        }
        octreeLevelOffset[0] = 0;
        for(int i = 1; i < numOfLevels; i++) {
            octreeLevelOffset[i] = octreeLevelOffset[i-1] + powOf8[i-1];
        }
        beenInit = true;
    }
    static int maxLevel;
    static int powOf8[];
    static int powOf2[];
    static int octreeLevelOffset[];

    public static void clearPartOfOctree(Vec3I coord, int level) {
        int coord1d = convert3DcoordTo1D(coord, powOf2[level]) + octreeLevelOffset[level];
        writeBit0(coord1d);
        if(level < maxLevel) {
            Vec3I newCoord[] = new Vec3I[8];


            newCoord[0] = Vec3I.mul(coord, 2);
            newCoord[1] = new Vec3I(newCoord[0]); newCoord[1].z++;
            newCoord[2] = new Vec3I(newCoord[0]); newCoord[2].y++;
            newCoord[3] = new Vec3I(newCoord[0]); newCoord[3].z++; newCoord[3].y++;
            newCoord[4] = new Vec3I(newCoord[0]); newCoord[4].x++;
            newCoord[5] = new Vec3I(newCoord[0]); newCoord[5].x++; newCoord[5].z++;
            newCoord[6] = new Vec3I(newCoord[0]); newCoord[6].x++; newCoord[6].y++;
            newCoord[7] = new Vec3I(newCoord[0]); newCoord[7].x++; newCoord[7].y++; newCoord[7].z++;

            for(int i =0; i < 8; i++) {
                int newLevel = level +1;
                clearPartOfOctree(newCoord[i], newLevel);
            }
        }
    }

    static HashMap<Integer, Integer> vtest = new HashMap<>();

    public static void writeBit1t(int coord1d, int level) {
        if(vtest.get(coord1d) != null) {
            vtest.put(3,8);
        }
        if(coord1d == 8) {
            vtest.put(3,5);
        }
        vtest.put(coord1d, level);
        octreeBuffer.put(coord1d / 8, (byte) ( octreeBuffer.get(coord1d/8) | (1 << (coord1d % 8))));
    }
    public static void writeBit1(int coord1d) {

        octreeBuffer.put(coord1d / 8, (byte) ( octreeBuffer.get(coord1d/8) | (1 << (coord1d % 8))));
    }
    public static void writeBit1s(int coord1d) {
        octreeBuffer.put(0, ((byte) 1));

    }
    public static void writeBit0(int coord1d) {
        octreeBuffer.put(coord1d / 8, (byte) ( octreeBuffer.get(coord1d/8) & (~(1 << (coord1d % 8)))));
    }
    public static int convert3DcoordTo1D(Vec3I coord, int cubeWidth) {
        return coord.x * (cubeWidth * cubeWidth) + coord.y * cubeWidth + coord.z;
    }
    public static void encodeNodeAndChildren(Node node, Vec3I coord, int level)
    {
//        if(coord.x == 1 && coord.y == 1 && coord.z == 1 && level == 1) {
//            return;
//        }
        if(node != null) {
            int coord1d = convert3DcoordTo1D(coord, powOf2[level]) + octreeLevelOffset[level];
            writeBit1(coord1d);
            if(level < maxLevel) {
                Vec3I newCoord[] = new Vec3I[8];

                newCoord[0] = Vec3I.mul(coord, 2);
                newCoord[1] = new Vec3I(newCoord[0]); newCoord[1].z++;
                newCoord[2] = new Vec3I(newCoord[0]); newCoord[2].y++;
                newCoord[3] = new Vec3I(newCoord[0]); newCoord[3].z++; newCoord[3].y++;
                newCoord[4] = new Vec3I(newCoord[0]); newCoord[4].x++;
                newCoord[5] = new Vec3I(newCoord[0]); newCoord[5].x++; newCoord[5].z++;
                newCoord[6] = new Vec3I(newCoord[0]); newCoord[6].x++; newCoord[6].y++;
                newCoord[7] = new Vec3I(newCoord[0]); newCoord[7].x++; newCoord[7].y++; newCoord[7].z++;

                for(int i =0; i < 8; i++) {
                    int newLevel = level +1;
                    encodeNodeAndChildren(((Chunk) node).children[i], newCoord[i], newLevel);
                }
            }
        }
        else {
            clearPartOfOctree(coord, level);
        }
    }


    public void resetWorkBuffer() { //doesn't need to be used right now
        octreeBuffer.reset();
    }
    public static class NodeContainer {
        Node node;
        int parentLoc;
        int octantOfParent;
        public NodeContainer(Node node, int parent, int octant) {
            this.node = node;
            this.parentLoc = parent;
            this.octantOfParent = octant;
        }
    }
    public static class NodePool {
        static Stack<NodeContainer> stack = new Stack();

        public static NodeContainer newContainer(Node node, int parent, int octant) {
            NodeContainer x;
            if(stack.empty()) {
                x = new NodeContainer(node, parent, octant);
                return x;
            }
            x = stack.pop();
            x.node = node;
            x.parentLoc = parent;
            x.octantOfParent = octant;
            return x;
        }
        public static void preserveContainer(NodeContainer nc) {
            stack.push(nc);
        }

    }


}
