package com.crosslink.battleprism.core.math;

import com.crosslink.battleprism.core.framework.BufferableData;

import java.nio.IntBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/28/13
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vec4I extends BufferableData<IntBuffer>{

    public int x, y, z, w;

    public Vec4I() {}

    public Vec4I(Vec4I vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = vector.w;
    }

    public Vec4I(int amplitutde) {
        this.x = amplitutde;
        this.y = amplitutde;
        this.z = amplitutde;
        this.w = amplitutde;
    }

    public Vec4I(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vec4I add(Vec4I lhs, int rhs) {
        return new Vec4I(lhs.x + rhs, lhs.y + rhs, lhs.z + rhs, lhs.w + rhs);
    }

    public static Vec4I add(Vec4I lhs, Vec4I rhs){
        return new Vec4I(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z, lhs.w + rhs.w);
    }

    public static Vec4I subtract(Vec4I lhs, Vec4I rhs) {
        return new Vec4I(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z, lhs.w - rhs.w);
    }

    @Override
    public String toString() {
        return String.format("{X:%s, Y:%s, Z:%s, W%s}", String.valueOf(x), String.valueOf(y), String.valueOf(z), String.valueOf(w));
    }


    @Override
    public IntBuffer fillBuffer(IntBuffer buffer) {
        buffer.put(x); buffer.put(y); buffer.put(z); buffer.put(w);
        return buffer;
    }
}
