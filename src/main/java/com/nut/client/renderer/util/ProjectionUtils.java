package com.nut.client.renderer.util;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

@Component
public class ProjectionUtils {

    public static FloatBuffer orthoProjection = BufferUtils.createFloatBuffer(16);

    @AutoInit
    public ProjectionUtils() {
        setOrthoProjection(0, 1920, 0, 1080, 0, 1);
    }

    public static void setOrthoProjection(float left, float right, float bottom, float top, float zNear, float zFar) {
        Matrix4f ortho = new Matrix4f();
        ortho.m00 = 2.0f / (right - left);
        ortho.m11 = 2.0f / (top - bottom);
        ortho.m22 = (2.0f) / (zNear - zFar);
        ortho.m30 = (right + left) / (left - right);
        ortho.m31 = (top + bottom) / (bottom - top);
        ortho.m32 = ((zFar + zNear)) / (zNear - zFar);
        ortho.store(orthoProjection);
        orthoProjection.flip();
    }
}
