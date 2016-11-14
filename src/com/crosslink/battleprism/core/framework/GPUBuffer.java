package com.crosslink.battleprism.core.framework;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferRange;

/**
 * Created by Thomas on 1/7/14.
 */
public class GPUBuffer {
    public int bufferID;
    public int size;
    int usage;
    public int target;
    public GPUBuffer(int usage) {
        bufferID = glGenBuffers();
        this.usage = usage;
    }
    public GPUBuffer() {
        bufferID = glGenBuffers();
        this.usage = GL_DYNAMIC_DRAW;
    }
    public void bind(int target) {
        this.target = target;
        glBindBuffer(target, bufferID);
    }
    public void bindBufferRange(int target, int index, int offset, int size) {
        glBindBufferRange(target, index, bufferID, offset, size);
    }
    public void bindBufferRange(int target, int index) {
        glBindBufferRange(target, index, bufferID, 0, size);
    }
    public void allocate(int size)
    {
        glBufferData(target, size, usage);
        this.size = size;
    }
    public void submitData(int offsetGPU, ByteBuffer data) {
        if(size >= (data.remaining() + offsetGPU))
        {
            glBufferSubData(target, offsetGPU, data);
        }
        else
        {
            if(offsetGPU > 0) throw new RuntimeException("you may be deleting data at the beginning of the buffer");
            this.size = data.remaining();
            glBufferData(target, data, usage);
        }

    }
    public void submitData(int offsetGPU, FloatBuffer data) {
        if(size >= (data.remaining() + offsetGPU))
        {
            glBufferSubData(target, offsetGPU, data);
        }
        else
        {
            if(offsetGPU > 0) throw new RuntimeException("you may be deleting data at the beginning of the buffer");
            this.size = data.remaining();
            glBufferData(target, data, usage);
        }

    }
    public void submitData(int offsetGPU, IntBuffer data) {
        if(size >= (data.remaining() + offsetGPU))
        {
            glBufferSubData(target, offsetGPU, data);
        }
        else
        {
            if(offsetGPU > 0) throw new RuntimeException("you may be deleting data at the beginning of the buffer");
            this.size = data.remaining();
            glBufferData(target, data, usage);
        }

    }
    public void Delete() {
        glDeleteBuffers(bufferID);
    }
}