package com.crosslink.battleprism.client.engine;

import com.crosslink.battleprism.client.world.World;
import com.crosslink.battleprism.client.engine.shaders.ShaderPrograms;
import com.crosslink.battleprism.core.framework.GPUBuffer;
import com.crosslink.battleprism.core.math.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import com.crosslink.battleprism.core.debug.Benchmark;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class WorldRenderer {


    public static void initialize() {
        { //the following needs to be managed more if there is to be mulitple worlds because the light / projection buffer needs to be swapped for each different draw call
            // Setup our Uniform Buffers
//            FeedBackManager.initializeObjects();
//            FeedBackManager.startFeedbackWithFirst();
            viewProjectionBuffer = new GPUBuffer(GL_DYNAMIC_DRAW);
            viewProjectionBuffer.bind(GL_UNIFORM_BUFFER);
            viewProjectionBuffer.allocate(4*4*4*3 + 4*4);

            cameraBuffer = new GPUBuffer(GL_DYNAMIC_DRAW);
            cameraBuffer.bind(GL_UNIFORM_BUFFER);
            cameraBuffer.allocate(4 * 16 + 8);

            //bind viewprojection uniform buffer to a uniform binding point corresponding to the shader block
            ShaderPrograms.bindViewProjectionBlock(0);
            glBindBufferRange(GL_UNIFORM_BUFFER, ShaderPrograms.viewProjBindingPoint, viewProjectionBuffer.bufferID, 0, viewProjectionBuffer.size);

            ShaderPrograms.bindCameraBlock(3);
            glBindBufferRange(GL_UNIFORM_BUFFER, ShaderPrograms.cameraBlockBindingPint, cameraBuffer.bufferID, 0, cameraBuffer.size);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

        }
        //worldframe fbo
        // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.

        worldFrame = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, worldFrame);

        worldColor = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, worldColor);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Display.getWidth(), Display.getHeight(),0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);

        // The depth buffer
        worldDepth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, worldDepth);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, Display.getWidth(), Display.getHeight(), 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_NONE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP );

//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
//            glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_LUMINANCE);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);

        // bind attachments
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, worldColor, 0);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, worldDepth, 0);

        // Always check that our framebuffer is ok
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("yo frame buffer is broken bro");

        { //lightframe fbo
            // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
            lightFrame = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, lightFrame);

            lightMap = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, lightMap);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Display.getWidth(), Display.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null); //this is stupid wrong
            // Poor filtering. Needed !
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);

            // The depth buffer
//            int depthrenderbuffer = glGenRenderbuffers();
//            glBindRenderbuffer(GL_RENDERBUFFER, depthrenderbuffer);
//            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, Display.getWidth(), Display.getHeight());
//            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthrenderbuffer);

            // Set "lightMap" as our colour attachement #0
            glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, lightMap, 0); //           glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, worldDepth, 0);
            glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, worldDepth, 0);
            // Set the list of draw buffers.
            glDrawBuffers(GL_COLOR_ATTACHMENT0); // "1" is the size of DrawBuffers
            // Always check that our framebuffer is ok
            if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
                throw new RuntimeException("yo frame buffer is broken bro");
        }

//        {
//            depthFrame = glGenFramebuffers();
//            glBindFramebuffer(GL_FRAMEBUFFER, depthFrame);
//            glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, worldColor, 0);
//            glDrawBuffer(GL_NONE); //needed to know to scan fragments for depth texture and with a typical depth buffer, errors will happen without this
//            if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
//                throw new RuntimeException("yo frame buffer is broken bro");
//        }

        {
            hiZFrame = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, hiZFrame);
            hiZColor = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, hiZColor);

            //glTexImage2D(GL_TEXTURE_2D, 0, GL_RG, Display.getWidth(), Display.getHeight(), 0, GL_RG, GL_UNSIGNED_BYTE, (ByteBuffer)null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
            //glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, worldDepth, 0);
            //glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, worldColor, 0);
            glDrawBuffer(GL_COLOR_ATTACHMENT0); //needed to know to scan fragments for depth texture and with a typical depth buffer, errors will happen without this
//            if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
//                throw new RuntimeException("yo frame buffer is broken bro");
        }
    }


    static int lightFrame;
    static int lightMap;
    static int worldFrame;
    static int worldColor;
    static int worldDepth;
    static int hiZFrame;
    static int hiZColor;

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private static FloatBuffer mat4Buffer 	= BufferUtils.createFloatBuffer(Mat4.SIZE);
    private static FloatBuffer fbuffer 	= BufferUtils.createFloatBuffer(4 * 16 + 8);
    private static IntBuffer intBuffer = BufferUtils.createIntBuffer(4 * 25 * 4);
    //private ByteBuffer hizbuffer = BufferUtils.createByteBuffer(4 * 25 * 2);

    static GPUBuffer viewProjectionBuffer;
    public static GPUBuffer lightBuffer;
    static GPUBuffer hizbuffer;
    static GPUBuffer cameraBuffer;
    public static void submitProjectionBlock(Camera3D camera) {
        glBindBuffer(GL_UNIFORM_BUFFER, viewProjectionBuffer.bufferID);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, camera.calcViewMatrix().fillAndFlipBuffer(mat4Buffer));
        glBufferSubData(GL_UNIFORM_BUFFER, 4 * 4 * 4, camera.calcProjectionMatrix().fillAndFlipBuffer(mat4Buffer));
        glBufferSubData(GL_UNIFORM_BUFFER, 4 * 4 * 4 * 2, Glm.inverse(camera.calcViewProjection()).fillAndFlipBuffer(mat4Buffer));
        glBufferSubData(GL_UNIFORM_BUFFER, 4 * 4 * 4 * 3, camera.viewPort.fillAndFlipBuffer(intBuffer));
        //System.out.print(camera.viewPort.toString());
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

    }
    public static void submitCameraBlock(Camera3D camera) {
        fbuffer.clear();
        glBindBuffer(GL_UNIFORM_BUFFER, cameraBuffer.bufferID);
        camera.eyePosition.fillBuffer(fbuffer);
        fbuffer.put(0f);
        camera.Forward.fillBuffer(fbuffer);
        fbuffer.put(0f);
        camera.Up.fillBuffer(fbuffer);
        fbuffer.put(0f);
        Glm.cross(camera.Up, camera.Forward).fillBuffer(fbuffer);
        fbuffer.put(0f);
        camera.viewPlaneApothem.fillBuffer(fbuffer);
        fbuffer.flip();
        glBufferSubData(GL_UNIFORM_BUFFER, 0, fbuffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

    }
    static int numLevels = 1;
    static ArrayList<Vec2I> depthDims = new ArrayList<Vec2I>();
    static ArrayList<Vec2I> coverage = new ArrayList<Vec2I>();
    public static void resizeFrameBuffer() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightMap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Display.getWidth(), Display.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);
        glBindTexture(GL_TEXTURE_2D, worldColor);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Display.getWidth(), Display.getHeight(),0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glBindTexture(GL_TEXTURE_2D, worldDepth);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, Display.getWidth(), Display.getHeight(), 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer)null);
    }

    public static void RenderScene(World world, Camera3D camera)
    {
        submitProjectionBlock(camera);

        submitCameraBlock(camera);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDisable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderPrograms.RayTracer.theProgram);
        //glBindTexture(GL_TEXTURE_2D, worldColor);

        Benchmark.GPUstart();
        world.map.draw();
        Benchmark.GPUstop();
        //Quad2D.renderFullScreenNDCquad();
        //System.out.println(camera.viewPort);
    }
}