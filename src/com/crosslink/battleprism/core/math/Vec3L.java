package com.crosslink.battleprism.core.math;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/29/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vec3L {
    public long x;
    public long y;
    public long z;

    public Vec3L() {}

    public Vec3L(Vec3L vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vec3L(long amplitutde) {
        this.x = amplitutde;
        this.y = amplitutde;
        this.z = amplitutde;
    }

    public Vec3L(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
