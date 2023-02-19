package com.nut.client.renderer;

import lombok.Getter;

import static org.lwjgl.opengl.GL15.*;

public class VBO implements GLBindableObject<VBO> {

    @Getter
    private final int vboID;
    @Getter private final int bufferTarget;

    public VBO(int bufferTarget) {
        vboID = glGenBuffers();
        this.bufferTarget = bufferTarget;
    }

    @Override
    public VBO bind() {
        glBindBuffer(bufferTarget, vboID);
        return this;
    }

    @Override
    public VBO unbind() {
        glBindBuffer(bufferTarget, 0);
        return this;
    }
}
