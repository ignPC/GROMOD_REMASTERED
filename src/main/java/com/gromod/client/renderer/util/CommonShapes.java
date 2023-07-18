package com.gromod.client.renderer.util;

public class CommonShapes {

    public static float[] cube(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        float[] data = new float[72];
        int index = 0;
        data[index++] = minX;
        data[index++] = minY;
        data[index++] = minZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = minX;
        data[index++] = minY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = minX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = maxZ;
        data[index++] = maxX;
        data[index++] = maxY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = minZ;
        data[index++] = maxX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = minY;
        data[index++] = maxZ;
        data[index++] = minX;
        data[index++] = minY;
        data[index] = minZ;
        return data;
    }

    public static float[] rectangle(float minX, float minY, float maxX, float maxY, int repeatAmount) {
        float[] data = new float[8 * repeatAmount];
        for (int i = 0; i < 8 * repeatAmount;) {
            data[i++] = maxX;
            data[i++] = minY;
            data[i++] = maxX;
            data[i++] = maxY;
            data[i++] = minX;
            data[i++] = maxY;
            data[i++] = minX;
            data[i++] = minY;
        }
        return data;
    }
}
