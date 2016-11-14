package com.crosslink.battleprism.core.image;

import com.crosslink.battleprism.core.math.Vec2I;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;

/**
 * Created by Thomas on 1/7/14.
 */
public class Texture {
    public int TextureID;
    public Vec2I size;
    public Texture(int textureID){
        TextureID = textureID;
    }
    public Texture(int textureID, int sizex, int sizey){
        TextureID = textureID;
        size = new Vec2I(sizex, sizey);
    }
    public Texture() {
        TextureID = glGenTextures();
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAX_LEVEL, 0);

    }

    public Texture(String initializeDimension) {
        TextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_1D, TextureID);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAX_LEVEL, 0);
    }

}