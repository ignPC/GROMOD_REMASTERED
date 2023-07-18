package com.gromod.client.renderer.util;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

@Component
public class ProjectionUtils {

    public static FloatBuffer guiProjection = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer worldProjection = BufferUtils.createFloatBuffer(16);
    private static final Matrix4f guiMatrix = new Matrix4f();
    private static final Matrix4f projectionMatrix = new Matrix4f();
    private static final Matrix4f modelViewMatrix = new Matrix4f();

    @AutoInit
    public ProjectionUtils() {
        setOrthoProjection(0, 1920, 0, 1080, 0, 1);
    }

    public static void setOrthoProjection(float left, float right, float bottom, float top, float zNear, float zFar) {
        guiMatrix
                .setIdentity()
                .setOrtho(left, right, bottom, top, zNear, zFar)
                .store(guiProjection);
    }

    public static void update3DMatrix(float[] projectionMatrixBuffer, float[] modelViewMatrixBuffer) {
        projectionMatrix.set(projectionMatrixBuffer);
        modelViewMatrix.set(modelViewMatrixBuffer);
        projectionMatrix
                .mul(modelViewMatrix)
                .store(worldProjection);
    }
}
