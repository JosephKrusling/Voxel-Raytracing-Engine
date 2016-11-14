package com.crosslink.battleprism.client.engine.shaders;

import com.crosslink.battleprism.core.framework.Framework;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.*;

/**
 * Created by Joseph on 8/5/2014.
 */
public class ShaderPrograms {

    public static void initialize() {
        AlreadyClipData.loadProgram("alreadyClip.vert", "justTexture.frag");
        RayTracer.loadProgram("alreadyClip.vert", "rayTraceOctree.frag");
    }

    public static class AlreadyClipData {
        public static int theProgram;
        public static class UniformLocations {
            public static int colorTextureUnif;
            public static int projectionBlock;
        }
        public static class Cache {
            public static int colorTextureBindingIndex;
        }

        public static void loadProgram(String vertexShaderFilename, String fragmentShaderFilename) {
            ShaderDebugger.DebugCompile(GL_FRAGMENT_SHADER, fragmentShaderFilename);
            ArrayList<Integer> shaderList = new ArrayList<>();
            shaderList.add(Framework.loadShader(GL_VERTEX_SHADER, vertexShaderFilename));
            shaderList.add(Framework.loadShader(GL_FRAGMENT_SHADER,	fragmentShaderFilename));

            theProgram = Framework.createAndLinkProgram(shaderList);
            UniformLocations.projectionBlock = glGetUniformBlockIndex(theProgram, "Projection");


            UniformLocations.colorTextureUnif = glGetUniformLocation(theProgram, "diffuseColorTex");
            glUseProgram(theProgram);
            glUniform1i(UniformLocations.colorTextureUnif, 1);
            Cache.colorTextureBindingIndex = 1;
            glUseProgram(0);
        }
    }

    public static class RayTracer {
        public static int theProgram;
        public static class UniformLocations {
            public static int bufferTex;
            public static int cameraBlock;
            public static int projectionBlock;
        }
        public static class Cache {
            public static int bufferTexBindingIndex;
        }

        public static void loadProgram(String vertexShaderFilename, String fragmentShaderFilename) {
            ShaderDebugger.DebugCompile(GL_FRAGMENT_SHADER, fragmentShaderFilename);
            ArrayList<Integer> shaderList = new ArrayList<>();
            shaderList.add(Framework.loadShader(GL_VERTEX_SHADER, vertexShaderFilename));
            shaderList.add(Framework.loadShader(GL_FRAGMENT_SHADER, fragmentShaderFilename));

            theProgram = Framework.createAndLinkProgram(shaderList);
            UniformLocations.cameraBlock = glGetUniformBlockIndex(theProgram, "Camera");
            UniformLocations.projectionBlock = glGetUniformBlockIndex(theProgram, "Projection");


            UniformLocations.bufferTex = glGetUniformLocation(theProgram, "bufferTex");
            glUseProgram(theProgram);
            glUniform1i(UniformLocations.bufferTex, 1);
            Cache.bufferTexBindingIndex = 1;
            glUseProgram(0);

        }
    }

    public static int viewProjBindingPoint;
    public static void bindViewProjectionBlock(int projectionBindingIndex) {
        viewProjBindingPoint = projectionBindingIndex;
        glUniformBlockBinding(RayTracer.theProgram, RayTracer.UniformLocations.projectionBlock, projectionBindingIndex);
//        glUniformBlockBinding(QuadData.theProgram, QuadData.UniformLocations.projectionBlock, projectionBindingIndex);
//        glUniformBlockBinding(QuadAmbientData.theProgram, QuadAmbientData.UniformLocations.projectionBlock, projectionBindingIndex);
//        glUniformBlockBinding(QuadShadowData.theProgram, QuadShadowData.UniformLocations.projectionBlock, projectionBindingIndex);
        //glUniformBlockBinding(alreadyClipData.theProgram, alreadyClipData.UniformLocations.projectionBlock, projectionBindingIndex); //put this back in if you use the block in the shader


    }

    public static int lightBlockBindingPoint;
    public static void bindLightBlock(int lightBindingIndex) {
        lightBlockBindingPoint = lightBindingIndex;
//        glUniformBlockBinding(QuadData.theProgram, QuadData.UniformLocations.lightBlock, lightBindingIndex);
//
//        glUniformBlockBinding(QuadAmbientData.theProgram, QuadAmbientData.UniformLocations.lightBlock, lightBindingIndex);

        //System.out.print(glGetInteger64(GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS));

    }

    public static int cameraBlockBindingPint;
    public static void bindCameraBlock(int cameraBindingIndex) {
        cameraBlockBindingPint = cameraBindingIndex;
        glUniformBlockBinding(RayTracer.theProgram, RayTracer.UniformLocations.cameraBlock, cameraBindingIndex);

    }

}
