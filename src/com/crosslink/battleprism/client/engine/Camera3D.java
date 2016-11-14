package com.crosslink.battleprism.client.engine;

import org.lwjgl.opengl.Display;
import com.crosslink.battleprism.core.math.*;
import com.crosslink.battleprism.core.containers.Ray;

/**
 * Created by Thomas on 1/13/14.
 */
public class Camera3D {
    //TODO: make projection matrix a property that will be recalculated when screen size changes
    public Vec3 eyePosition;
    public Vec3 Up;
    public Vec3 Forward;
    private Mat4 projection;
    private Mat4 view;
    public Vec4I viewPort;
    public Vec2 viewPlaneApothem;
    /// <summary>
    /// Construct a view matrix corresponding to this camera.
    /// </summary>
    public Mat4 calcViewProjection()
    {
        return calcProjectionMatrix().mul(calcViewMatrix());
    }

    public Mat4 calcProjectionMatrix() {
        projection = Glm.perspective(60.0f, viewPort.z / (float)viewPort.w, 1, 1000);
        return projection;
    }
    public Mat4 calcViewMatrix()
    {
        view = Glm.lookAt(eyePosition, Vec3.add(eyePosition, Forward), Up); //I think this will work even though Up in the position and forward is Z
        return view;
    }

    public Camera3D()
    {
        viewPort = new Vec4I(0, 0, Display.getWidth(), Display.getHeight());
        eyePosition = new Vec3(0,0,1);
        Up = new Vec3(0,0,1);
        Forward = new Vec3(1,0,0);
        projection = Glm.perspective(60.0f, Display.getWidth() / (float) Display.getHeight(), 1, 1000); //IF WINDOW SIZE CHANGES THIS MUST CHANGE
        viewPlaneApothem = new Vec2(1f,1f); //TODO: fix this to be correct based on projection, and make a projection / view frustrum changing function
}

    public Camera3D(Vec3 position, Vec3 forward, Vec3 up)
    {
        viewPort = new Vec4I(0,0 , Display.getWidth(), Display.getHeight());
        eyePosition = position;
        Forward = forward;
        Up = up;
        projection = Glm.perspective(60.0f, Display.getWidth() / (float) Display.getHeight(), 1, 1000); //IF WINDOW SIZE CHANGES THIS MUST CHANGE
        viewPlaneApothem = new Vec2(1f,1f);
    }

    public void Thrust(float amount)
    {
        Forward = Glm.normalize(Forward);
        eyePosition.add(Vec3.scale(Forward, amount));
    }

    public void StrafeHorz(float amount)
    {
        Vec3 left = Glm.cross(Up, Forward);
        left = Glm.normalize(left);
        eyePosition = eyePosition.add(left.scale(amount));
    }

    public void StrafeVert(float amount)
    {
        Up = Glm.normalize(Up);
        eyePosition = eyePosition.add(Vec3.scale(Up, amount));
    }

    public void Roll(float amount)
    {
        Up = Glm.normalize(Up);
        Mat4 rot = new Mat4(1);
        Up = new Vec3(Mat4.mul(Glm.rotate(rot, (float) Math.toRadians(amount), Forward), new Vec4(Up, 1)));

    }

    public void Yaw(float amount)
    {
        Forward = Glm.normalize(Forward);
        Mat4 rot = new Mat4(1);
        Forward = new Vec3(Mat4.mul(Glm.rotate(rot, (float) Math.toRadians(amount), Up), new Vec4(Forward, 1)));
    }

    public void Pitch(float amount)
    {
        Forward = Glm.normalize(Forward);
        Vec3 left = Glm.cross(Up, Forward);
        left = Glm.normalize(left);
        Mat4 rot = new Mat4(1);
        Forward = new Vec3(Mat4.mul(Glm.rotate(rot, (float) Math.toRadians(amount), left), new Vec4(Forward, 1)));
        Up = new Vec3(Mat4.mul(Glm.rotate(rot, (float) Math.toRadians(amount), left), new Vec4(Up, 1)));


    }
    public Ray getRayFromScreen(Vec2 screenPoint)
    {

        Vec3 clip = new Vec3((((screenPoint.x * 2) - Display.getWidth())) / Display.getWidth(), ((screenPoint.y * 2) - Display.getHeight()) / Display.getHeight(), -1f);
        // assume Z-near is 1, so undoing perspective divide can be skipped since were getting a point on z-near plane, it would only be times 1
        Vec3 world = new Vec3(Mat4.mul(Glm.inverse(calcViewProjection()), new Vec4(clip, 1)));

//        Vec3 clip2 = new Vec3((((screenPoint.x * 2) - Display.getWidth())) / Display.getWidth(), ((screenPoint.y * 2) - Display.getHeight()) / Display.getHeight(), -1f);
//        // assume Z-near is 1, so undoing perspective dividxe can be skipped since were getting a point on z-near plane, it would only be times 1
//        Vec3 world2 = new Vec3(Mat4.mul(Glm.inverse(calcViewProjection()), new Vec4(clip, 1)));

        Vec3 dir =  Vec3.sub(world, eyePosition);
        dir = Glm.normalize(dir);
        return new Ray(eyePosition, dir);
    }


}
