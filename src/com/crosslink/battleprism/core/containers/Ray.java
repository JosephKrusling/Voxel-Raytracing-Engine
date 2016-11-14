package com.crosslink.battleprism.core.containers;

import com.crosslink.battleprism.core.math.Vec3;

/**
 * Created by Thomas on 1/16/14.
 */
public class Ray
{
    public Vec3 startPoint;
    public Vec3 direction;

    public Ray(Vec3 start, Vec3 direction)
    {
        startPoint = start;
        this.direction = direction;
    }

    public boolean IsIntersectingBox(BoundingBox box)
    {
        Vec3 inverseDir = Vec3.div(new Vec3(1), direction);
        float t1 = (box.getMin_X() - startPoint.x) * inverseDir.x;
        float t2 = (box.getMax_X() - startPoint.x) * inverseDir.x;

        float tmin = Math.min(t1, t2);
        float tmax = Math.max(t1, t2);

        t1 = (box.getMin_Y() - startPoint.y) * inverseDir.y;
        t2 = (box.getMax_Y() - startPoint.y) * inverseDir.y;

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        t1 = (box.getMin_Z() - startPoint.z) * inverseDir.z;
        t2 = (box.getMax_Z() - startPoint.z) * inverseDir.z;

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        return (tmax >= tmin);
    }

    public static class RayResult {
        public float distance;
        public AxialDirection facet;
    }

    public boolean IsIntersectingBox(BoundingBox box, RayResult outputs)
    {
        Vec3 inverseDir = Vec3.div(new Vec3(1), direction);
        float t1 = (box.getMin_X() - startPoint.x) * inverseDir.x;
        float t2 = (box.getMax_X() - startPoint.x) * inverseDir.x;

        float tminX = Math.min(t1, t2);
        outputs.facet = tminX == t1 ? AxialDirection.NegX : AxialDirection.PosX;
        float tmaxX = Math.max(t1, t2);

        t1 = (box.getMin_Y() - startPoint.y) * inverseDir.y;
        t2 = (box.getMax_Y() - startPoint.y) * inverseDir.y;

        float tminY = Math.min(t1, t2);
        float tmin = Math.max(tminX, tminY);
        if ( tmin == tminY)
        {
            outputs.facet = tminY == t1 ? AxialDirection.NegY : AxialDirection.PosY;
        }

        float tmaxY = Math.min(tmaxX, Math.max(t1, t2));

        t1 = (box.getMin_Z() - startPoint.z) * inverseDir.z;
        t2 = (box.getMax_Z() - startPoint.z) * inverseDir.z;

        float tminZ = Math.min(t1, t2);
        tmin = Math.max(tmin, tminZ);
        if (tmin == tminZ)
        {
            outputs.facet = tminZ == t1 ? AxialDirection.NegZ : AxialDirection.PosZ;
        }

        float tmaxZ = Math.min(tmaxY, Math.max(t1, t2));

        if (tmaxZ >= tmin)
        {
            outputs.distance = tmin;
            return true;
        }
        //facet = null; // does not work
        return false;
    }

    //    public boolean IsIntersectingBox(BoundingBox box, ref float distance)
//    {
//        float t1 = (box.getMin_X() - startPoint.x) * inverseDir.x;
//        float t2 = (box.getMax_X() - startPoint.x) * inverseDir.x;
//
//        float tmin = Math.min(t1, t2);
//        float tmax = Math.max(t1, t2);
//
//        t1 = (box.getMin_Y() - startPoint.y) * inverseDir.y;
//        t2 = (box.getMax_Y() - startPoint.y) * inverseDir.y;
//
//        tmin = Math.max(tmin, Math.min(t1, t2));
//        tmax = Math.min(tmax, Math.max(t1, t2));
//
//        t1 = (box.getMin_Z() - startPoint.z) * inverseDir.z;
//        t2 = (box.getMax_Z() - startPoint.z) * inverseDir.z;
//
//        tmin = Math.max(tmin, Math.min(t1, t2));
//        tmax = Math.min(tmax, Math.max(t1, t2));
//
//        if (tmax >= tmin)
//        {
//            distance = tmin;
//            return true;
//        }
//        return false;
//    }

}