package com.nut.client.renderer;

import lombok.Getter;

import static org.lwjgl.opengl.GL30.*;

public class VAO implements GLBindableObject<VAO> {

    @Getter
    private final int vaoID;

    public VAO() {
        vaoID = glGenVertexArrays();
    }

    @Override
    public VAO bind() {
        glBindVertexArray(vaoID);
        return this;
    }

    @Override
    public VAO unbind() {
        glBindVertexArray(0);
        return this;
    }
}
