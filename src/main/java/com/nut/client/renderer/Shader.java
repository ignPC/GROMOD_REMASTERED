package com.nut.client.renderer;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    @Getter private final int shaderProgram;
    private final HashMap<String, Integer> uniforms;

    public Shader(ResourceLocation vertexShaderLocation, ResourceLocation fragmentShaderLocation, String... strings) {
        boolean vertexShaderPresent = vertexShaderLocation != null;
        boolean fragmentShaderPresent = fragmentShaderLocation != null;

        shaderProgram = glCreateProgram();
        int vertexShaderID = 0;
        if (vertexShaderPresent) {
            String vertexShader = readShader(vertexShaderLocation);
            vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertexShaderID, vertexShader);
            glCompileShader(vertexShaderID);
            if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
                System.out.println(glGetShaderInfoLog(vertexShaderID, 500));
                glDeleteShader(vertexShaderID);
            }
            glAttachShader(shaderProgram, vertexShaderID);
            System.out.println(vertexShaderID + " attached to shader program: " + shaderProgram);
        }
        int fragmentShaderID = 0;
        if (fragmentShaderPresent) {
            String fragmentShader = readShader(fragmentShaderLocation);
            fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentShaderID, fragmentShader);
            glCompileShader(fragmentShaderID);
            if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
                System.out.println(glGetShaderInfoLog(fragmentShaderID, 500));
                glDeleteShader(fragmentShaderID);
            }
            glAttachShader(shaderProgram, fragmentShaderID);
            System.out.println(fragmentShaderID + " attached to shader program: " + shaderProgram);
        }
        glLinkProgram(shaderProgram);
        if (vertexShaderPresent) glDeleteShader(vertexShaderID);
        if (fragmentShaderPresent) glDeleteShader(fragmentShaderID);

        uniforms = new HashMap<>(strings.length);
        for (String uniform : strings) {
            uniforms.put(uniform, glGetUniformLocation(shaderProgram, uniform));
        }
    }

    public int getUniform(String uniform) {
        return uniforms.get(uniform);
    }

    @SneakyThrows
    private String readShader(ResourceLocation shader) {
        try {
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(shader).getInputStream();
            String inputStreamString = IOUtils.toString(inputStream);
            IOUtils.closeQuietly(inputStream);
            return inputStreamString;
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static void setColorRGBA(Shader shader, int color) {
        glUniform4f(shader.getUniform("color"), (color >> 16 & 0xFF) / 255f, (color >> 8 & 0xFF) / 255f, (color & 0xFF) / 255f, (color >> 24 & 0xFF) / 255f);
    }

    public static void setColorRGB(Shader shader, int color) {
        glUniform3f(shader.getUniform("color"), (color >> 16 & 0xFF) / 255f, (color >> 8 & 0xFF) / 255f, (color & 0xFF) / 255f);
    }
}
