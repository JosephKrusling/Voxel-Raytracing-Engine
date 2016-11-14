package com.crosslink.battleprism.client.engine.shaders;

import com.crosslink.battleprism.core.framework.Framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class ShaderDebugger {
    private static String loadShaderFile(String shaderFilepath) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.class.getResourceAsStream(shaderFilepath)));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return text.toString();
    }
    public static void DebugCompile(int shaderType, String vertexShaderFilename) {
        System.out.println(glGetString(GL_VERSION));
        String filepath = Framework.findFileOrThrow(vertexShaderFilename);
        String shaderCode = loadShaderFile(filepath);

        int shader = glCreateShader(shaderType);

        glShaderSource(shader, shaderCode);
        glCompileShader(shader);

        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (status == GL_FALSE) { System.out.println("Shader did NOT compile");}
        printInfoLog(shader);
        //glDeleteShader(shader);
    }
    public static void printInfoLog(int shader)
    {

        String e = glGetShaderInfoLog(shader,20000);
        System.out.println(e);
    }
}