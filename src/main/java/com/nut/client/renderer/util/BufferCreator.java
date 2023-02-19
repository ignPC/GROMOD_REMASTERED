package com.nut.client.renderer.util;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class BufferCreator {

    public static FloatBuffer createBuffer(float[] data) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    public static ByteBuffer createBuffer(byte[] data) {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        return byteBuffer;
    }
}
