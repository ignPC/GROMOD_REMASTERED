package com.nut.client.renderer.util;

import java.nio.FloatBuffer;

public class Matrix4f {

    float m00, m01, m02, m03;
    float m10, m11, m12, m13;
    float m20, m21, m22, m23;
    float m30, m31, m32, m33;

    public Matrix4f() {
        m00 = 1;
        m11 = 1;
        m22 = 1;
        m33 = 1;
    }

    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar) {
        m00 = 2.0f / (right - left);
        m11 = 2.0f / (top - bottom);
        m22 = (2.0f) / (zNear - zFar);
        m30 = (right + left) / (left - right);
        m31 = (top + bottom) / (bottom - top);
        m32 = ((zFar + zNear)) / (zNear - zFar);
        return this;
    }

    public Matrix4f setIdentity() {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    public Matrix4f set(float[] data) {
        m00 = data[0];
        m01 = data[1];
        m02 = data[2];
        m03 = data[3];
        m10 = data[4];
        m11 = data[5];
        m12 = data[6];
        m13 = data[7];
        m20 = data[8];
        m21 = data[9];
        m22 = data[10];
        m23 = data[11];
        m30 = data[12];
        m31 = data[13];
        m32 = data[14];
        m33 = data[15];
        return this;
    }

    private float fma(double a, double b, double c) {
        double result = a * b + c;
        return (float) result;
    }

    private float fma(float a, float b, float c) {
        return a * b + c;
    }

    public Matrix4f mul(Matrix4f matrix) {
        float nm00 = fma(m00, matrix.m00, fma(m10, matrix.m01, fma(m20, matrix.m02, m30 * matrix.m03)));
        float nm01 = fma(m01, matrix.m00, fma(m11, matrix.m01, fma(m21, matrix.m02, m31 * matrix.m03)));
        float nm02 = fma(m02, matrix.m00, fma(m12, matrix.m01, fma(m22, matrix.m02, m32 * matrix.m03)));
        float nm03 = fma(m03, matrix.m00, fma(m13, matrix.m01, fma(m23, matrix.m02, m33 * matrix.m03)));
        float nm10 = fma(m00, matrix.m10, fma(m10, matrix.m11, fma(m20, matrix.m12, m30 * matrix.m13)));
        float nm11 = fma(m01, matrix.m10, fma(m11, matrix.m11, fma(m21, matrix.m12, m31 * matrix.m13)));
        float nm12 = fma(m02, matrix.m10, fma(m12, matrix.m11, fma(m22, matrix.m12, m32 * matrix.m13)));
        float nm13 = fma(m03, matrix.m10, fma(m13, matrix.m11, fma(m23, matrix.m12, m33 * matrix.m13)));
        float nm20 = fma(m00, matrix.m20, fma(m10, matrix.m21, fma(m20, matrix.m22, m30 * matrix.m23)));
        float nm21 = fma(m01, matrix.m20, fma(m11, matrix.m21, fma(m21, matrix.m22, m31 * matrix.m23)));
        float nm22 = fma(m02, matrix.m20, fma(m12, matrix.m21, fma(m22, matrix.m22, m32 * matrix.m23)));
        float nm23 = fma(m03, matrix.m20, fma(m13, matrix.m21, fma(m23, matrix.m22, m33 * matrix.m23)));
        float nm30 = fma(m00, matrix.m30, fma(m10, matrix.m31, fma(m20, matrix.m32, m30 * matrix.m33)));
        float nm31 = fma(m01, matrix.m30, fma(m11, matrix.m31, fma(m21, matrix.m32, m31 * matrix.m33)));
        float nm32 = fma(m02, matrix.m30, fma(m12, matrix.m31, fma(m22, matrix.m32, m32 * matrix.m33)));
        float nm33 = fma(m03, matrix.m30, fma(m13, matrix.m31, fma(m23, matrix.m32, m33 * matrix.m33)));
        m00 = nm00;
        m01 = nm01;
        m02 = nm02;
        m03 = nm03;
        m10 = nm10;
        m11 = nm11;
        m12 = nm12;
        m13 = nm13;
        m20 = nm20;
        m21 = nm21;
        m22 = nm22;
        m23 = nm23;
        m30 = nm30;
        m31 = nm31;
        m32 = nm32;
        m33 = nm33;
        return this;
    }

    public Matrix4f translate(double x, double y, double z) {
        m30 = fma(m00, x, fma(m10, y, fma(m20, z, m30)));
        m31 = fma(m01, x, fma(m11, y, fma(m21, z, m31)));
        m32 = fma(m02, x, fma(m12, y, fma(m22, z, m32)));
        m33 = fma(m03, x, fma(m13, y, fma(m23, z, m33)));
        return this;
    }

    public Matrix4f store(FloatBuffer buffer) {
        buffer.put(m00);
        buffer.put(m01);
        buffer.put(m02);
        buffer.put(m03);
        buffer.put(m10);
        buffer.put(m11);
        buffer.put(m12);
        buffer.put(m13);
        buffer.put(m20);
        buffer.put(m21);
        buffer.put(m22);
        buffer.put(m23);
        buffer.put(m30);
        buffer.put(m31);
        buffer.put(m32);
        buffer.put(m33);
        buffer.flip();
        return this;
    }
}
