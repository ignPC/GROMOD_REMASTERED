package com.nut.client.renderer;

import lombok.Getter;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glGenBuffers;

@Getter
public class VBO {

    private final int vboID;
    private final int bufferTarget;

    public VBO(int bufferTarget) {
        vboID = glGenBuffers();
        this.bufferTarget = bufferTarget;
    }

    public VBO bind() {
        glBindBuffer(bufferTarget, vboID);
        return this;
    }

    public VBO unbind() {
        glBindBuffer(bufferTarget, 0);
        return this;
    }
}
