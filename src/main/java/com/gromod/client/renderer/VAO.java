package com.gromod.client.renderer;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VAO {

    private final int vaoID;

    public VAO() {
        vaoID = glGenVertexArrays();
    }

    public VAO bind() {
        glBindVertexArray(vaoID);
        return this;
    }

    public VAO unbind() {
        glBindVertexArray(0);
        return this;
    }
}
