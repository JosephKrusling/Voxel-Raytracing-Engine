package com.crosslink.battleprism.core.containers;

import com.crosslink.battleprism.core.math.Vec3;

/**
 * Created by Thomas on 1/16/14.
 */
public class BoundingBox
{
    public Vec3 Position;
    public Vec3 Apothem;
    public Vec3 getMinPoint() {
         return Position.sub(Apothem); 
    }
    public float Volume() {
        return (float)(Apothem.x * 2 * Apothem.y * 2 * Apothem.z * 2);
    } 

    public BoundingBox(Vec3 pos, Vec3 apothem)
    {
        Position = pos;
        Apothem = apothem;
    }
    public float getMax_X() {
        return Position.x + Apothem.x;
    }
    public float getMin_X() {
        return Position.x - Apothem.x;
    }
    public float getMax_Y() {
        return Position.y + Apothem.y;
    }
    public float getMin_Y() {
        return Position.y - Apothem.y;
    }
    public float getMax_Z() {
        return Position.z + Apothem.z;
    }
    public float getMin_Z() {
        return Position.z - Apothem.z;
    }
    public Vec3 GetAdjacentBoxPosition(AxialDirection adjacentFacet, Vec3 otherApothem)
    {
        Vec3 newPos = Position;
        switch (adjacentFacet)
        {
            case NegY:
                newPos.y = getMin_Y() - otherApothem.y;
                break;

            case PosY:
                newPos.y = getMax_Y() + otherApothem.y;
                break;

            case NegZ:
                newPos.z = getMin_Z() - otherApothem.z;
                break;

            case PosZ:
                newPos.z = getMax_Z() + otherApothem.z;
                break;

            case NegX:
                newPos.x = getMin_X() - otherApothem.x;
                break;

            case PosX:
                newPos.x = getMax_X() + otherApothem.x;
                break;
        }
        return newPos;
    }

    public boolean IsCollidingWith(BoundingBox otherBox)
    {
        if (Math.abs(otherBox.Position.x - Position.x) < otherBox.Apothem.x + Apothem.x)
        {
            if (Math.abs(otherBox.Position.y - Position.y) < otherBox.Apothem.y + Apothem.y)
            {
                if (Math.abs(otherBox.Position.z - Position.z) < otherBox.Apothem.z + Apothem.z)
                {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean IsFullyEnclosing(BoundingBox otherBox)
    {
        if (Math.abs(otherBox.Position.x - Position.x) + otherBox.Apothem.x <= Apothem.x)
        {
            if (Math.abs(otherBox.Position.y - Position.y) + otherBox.Apothem.y <= Apothem.y)
            {
                if (Math.abs(otherBox.Position.z - Position.z) + otherBox.Apothem.z <= Apothem.z)
                {
                    return true;
                }
            }
        }

        return false;
    }

}