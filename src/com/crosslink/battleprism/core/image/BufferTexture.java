package com.crosslink.battleprism.core.image;

import com.crosslink.battleprism.core.framework.GPUBuffer;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.glTexBuffer;

/**
 * Created by Thomas on 1/7/14.
 */
public class BufferTexture extends Texture {


    public GPUBuffer gpuBuffer;

    public BufferTexture(int internalFormat, ByteBuffer buffer){
        gpuBuffer = new GPUBuffer();
        gpuBuffer.bind(GL_TEXTURE_BUFFER);
        gpuBuffer.allocate(buffer.remaining());
        gpuBuffer.submitData(0, buffer);
        glBindTexture(GL_TEXTURE_BUFFER, TextureID);
        glTexBuffer(GL_TEXTURE_BUFFER, internalFormat, gpuBuffer.bufferID);
    }
    public BufferTexture(int internalFromat){
        gpuBuffer = new GPUBuffer(GL_STREAM_DRAW);
        gpuBuffer.bind(GL_TEXTURE_BUFFER);
        gpuBuffer.allocate(50);
        glBindTexture(GL_TEXTURE_BUFFER, TextureID);
        glTexBuffer(GL_TEXTURE_BUFFER, internalFromat, gpuBuffer.bufferID);
    }


}
