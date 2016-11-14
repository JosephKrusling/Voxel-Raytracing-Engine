package com.crosslink.battleprism.client.engine;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class Quad2D {
    static int vbuffer;
    static int ebuffer;
    static int vao;
    static int eleoffset;


    public static void initialize() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbuffer = glGenBuffers();
        float[] pos = new float[]
                {
                        -1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        -1.0f, 1.0f, 1.0f
                };
        float[] norm = new float[]
                {
                        0f,0f,1f,
                        0f,0f,1f,
                        0f,0f,1f,
                        0f,0f,1f
                };
        float[] texCoord = new float[]
                {
                        0f,0f,
                        1f,0f,
                        1f,1f,
                        0f,1f
                };
        byte[] ele = new byte[]
                {
                        0,1,2,3
                };
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer((pos.length + norm.length + texCoord.length) * 4 + ele.length);
        for(float f : pos)
        {
            byteBuffer.putFloat(f);
        }
        for(float f : norm)
        {
            byteBuffer.putFloat(f);
        }
        for(float f : texCoord)
        {
            byteBuffer.putFloat(f);
        }
        for(byte b : ele)
        {
            byteBuffer.put(b);
        }
        byteBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbuffer);
        glBufferData(GL_ARRAY_BUFFER, byteBuffer, GL_STREAM_DRAW);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(5);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 4 * 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 4 * pos.length);
        glVertexAttribPointer(5, 2, GL_FLOAT, false, 0, 4 * (pos.length + norm.length));

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbuffer);
        eleoffset = (pos.length + norm.length + texCoord.length)*4;
        glBindVertexArray(0);

    }
    public static void render(int instanceBuffer, int instanceOffset, int instances)
    {

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 4 * 0);
        glBindBuffer(GL_ARRAY_BUFFER, instanceBuffer);
        glVertexAttribDivisor(6, 1);
        glVertexAttribDivisor(7,1);
        glVertexAttribDivisor(8,1);
        glVertexAttribDivisor(9,1);
        glVertexAttribPointer(6, 4, GL_FLOAT, false, quadInstanceSize, instanceOffset);
        glVertexAttribIPointer(7, 1, GL_INT, quadInstanceSize, 4*4 + instanceOffset);
        glVertexAttribPointer(8, 4, GL_UNSIGNED_BYTE, true, quadInstanceSize, 4*5 + instanceOffset );
        glVertexAttribPointer(9, 1, GL_UNSIGNED_BYTE, true, quadInstanceSize, 4*6 + instanceOffset );
        glDrawElementsInstanced(GL_QUADS, 4, GL_UNSIGNED_BYTE, eleoffset, instances);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public static void renderFullScreenNDCquad() {
        glBindVertexArray(vao);
        glDrawElementsInstanced(GL_QUADS, 4, GL_UNSIGNED_BYTE, eleoffset, 1);
    }

    public static final int quadInstanceSize = 25;






}
