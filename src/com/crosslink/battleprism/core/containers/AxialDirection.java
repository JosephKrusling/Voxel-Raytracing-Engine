package com.crosslink.battleprism.core.containers;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public enum AxialDirection {
    PosX(0, 1, 0, 0),
    NegX(1, -1, 0, 0),
    PosY(2, 0, 1, 0),
    NegY(3, 0, -1, 0),
    PosZ(4, 0, 0, 1),
    NegZ(5, 0, 0, -1),
    Zero(6, 0,0,0);
    private final int value;
    private final int x;
    private final int y;
    private final int z;
    private AxialDirection(int value, int x, int y, int z) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static AxialDirection fromInteger(int x) {
        switch(x) {
            case 0:
                return PosX;
            case 1:
                return NegX;
            case 2:
                return PosY;
            case 3:
                return NegY;
            case 4:
                return PosZ;
            case 5:
                return NegZ;
            case 6:
                return Zero;
        }
        return null;
    }

    public static AxialDirection getOppositeFace(int x) {
        return getOppositeFace(fromInteger(x));
    }

    public static AxialDirection getOppositeFace(AxialDirection direction) {
        switch (direction) {
            case PosX:
                return NegX;
            case NegX:
                return PosX;
            case PosY:
                return NegY;
            case NegY:
                return PosY;
            case PosZ:
                return NegZ;
            case NegZ:
                return PosZ;
            case Zero:
                return Zero;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
