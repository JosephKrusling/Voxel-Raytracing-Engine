package com.crosslink.battleprism.client.states;

import com.crosslink.battleprism.client.Game;
import com.crosslink.battleprism.client.interfaces.GameComponent;
import com.crosslink.battleprism.client.networking.events.impl.outgoing.OutgoingDestroyVoxel;
import com.crosslink.battleprism.client.engine.Camera3D;
import com.crosslink.battleprism.client.engine.WorldRenderer;
import com.crosslink.battleprism.client.world.World;
import com.crosslink.battleprism.client.world.nodes.NodeSearchResult;
import com.crosslink.battleprism.core.containers.Ray;
import com.crosslink.battleprism.core.math.Vec2;
import com.crosslink.battleprism.core.math.Vec4I;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Created by Joseph on 8/8/2014.
 */
public class PlayState implements GameComponent {

    private World activeWorld;

    private Camera3D camera;


    @Override
    public void initialize() {
        activeWorld = new World();
        activeWorld.initialize();
        camera = new Camera3D();
    }

    @Override
    public void update(float lastFrameDuration) {

        float thrustAmount = 0.01f * lastFrameDuration;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.Thrust(thrustAmount);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.Thrust(-thrustAmount);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.StrafeHorz(-thrustAmount);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.StrafeHorz(thrustAmount);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            camera.StrafeVert(thrustAmount);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            camera.StrafeVert(-thrustAmount);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
            camera.Pitch(-thrustAmount * 500);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            camera.Pitch(thrustAmount * 500);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            camera.Yaw(thrustAmount * 500);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            camera.Yaw(-thrustAmount * 500);
        }

        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.getEventButton() == 0) {
                    if (Mouse.isButtonDown(0)) {
                        Ray mouseRay = camera.getRayFromScreen(new Vec2(Mouse.getX(), Mouse.getY()));

                        NodeSearchResult result = activeWorld.map.getNearestVoxelInRay(mouseRay);

                        if (result == null) {
                            System.out.println("didn't hit anything");
                        } else {
                            OutgoingDestroyVoxel event = new OutgoingDestroyVoxel(result.node.position.root.worldIndex, result.node.position.relPosition, (byte) 0);
                            Game.net.channel.writeAndFlush(event);
                        }
                        //Vec3 intersectionPoint = result.node.getIntersectionPoint(mouseRay, result.rayResult);

                        //Voxel v = (Voxel) result.node;
                        //v.subtract(result.rayResult.facet, intersectionPoint, brushLevel);
                    }
                }
            }
        }
    }

    @Override
    public void draw(float lastFrameDuration) {
        WorldRenderer.RenderScene(activeWorld, camera);
    }

    @Override
    public void resized(int width, int height) {
        camera.viewPort = new Vec4I(0, 0, width, height);
        WorldRenderer.resizeFrameBuffer();
        System.out.println("resized");
    }
}
