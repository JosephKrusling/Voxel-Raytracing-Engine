package com.crosslink.battleprism.client.engine.frame;

import com.crosslink.battleprism.client.Game;
import com.crosslink.battleprism.client.interfaces.GameComponent;
import com.crosslink.battleprism.client.engine.Quad2D;
import com.crosslink.battleprism.client.engine.WorldRenderer;
import com.crosslink.battleprism.client.engine.shaders.ShaderPrograms;
import com.crosslink.battleprism.client.states.PlayState;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Joseph on 7/10/2014.
 */
public class Engine implements GameComponent, Runnable {

    public enum DisplayType {
        Windowed,
        WindowedFullScreen,
        Fullscreen
    }

    private DisplayType mode;
    private GameComponent activeState;

    @Override
    public void run() {
        initialize();
    }

    @Override
    public void initialize() {
        createWindow(DisplayType.Windowed, 1440 - 200, 900 - 200);

        ShaderPrograms.initialize();
        WorldRenderer.initialize();
        Quad2D.initialize();

        activeState = new PlayState();
        activeState.initialize();

        long lastFrameStartTime = System.nanoTime();

        while (!Display.isCloseRequested()) {

            float lastFrameDuration = (float) (System.nanoTime() - lastFrameStartTime) / 1000000.0f;
            lastFrameStartTime = System.nanoTime();

            if (Display.wasResized())
                resized(Display.getWidth(), Display.getHeight());
            update(lastFrameDuration);
            draw(lastFrameDuration);

            Display.update();
        }

        Display.destroy();
        Game.terminate();
    }

    @Override
    public void update(float lastFrameDuration) {
        Display.sync(60);

        activeState.update(lastFrameDuration);
    }

    @Override
    public void draw(float lastFrameDuration) {
        glClearColor(.4f, .5f, .6f, 1f);
        glClearDepth(1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        activeState.draw(lastFrameDuration);
    }

    @Override
    public void resized(int width, int height) {
        glViewport(0, 0, width, height);
        activeState.resized(width, height);
    }

    public void createWindow(DisplayType mode, int width, int height) {
        try {
            Display.setTitle("Crosslink Development Build");
            switch (mode) {
                case Windowed:
                    Display.setDisplayMode(new DisplayMode(width, height));
                    Display.setResizable(true);
                    Display.create(new PixelFormat(8,8,8));
                    break;
                case WindowedFullScreen: // hack
                    System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                    Display.setDisplayMode(new DisplayMode(width, height));
                    Display.setResizable(false);
                    Display.setLocation(0, 0);
                    Display.create(new PixelFormat(8,8,8));
                    break;
                case Fullscreen:
                    // TODO: implement fullscreen mode
                    break;
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
