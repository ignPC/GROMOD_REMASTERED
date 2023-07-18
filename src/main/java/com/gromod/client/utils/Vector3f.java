package com.gromod.client.utils;

public class Vector3f {

    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f set(Vector3f vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        return this;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(float value) {
        x = value;
        y = value;
        z = value;
        return this;
    }

    public Vector3f add(Vector3f vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
        return this;
    }

    public Vector3f add(float x, float y) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f add(float value) {
        x += value;
        y += value;
        z += value;
        return this;
    }

    public Vector3f subtract(Vector3f vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
        return this;
    }

    public Vector3f subtract(float x, float y) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3f subtract(float value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }

    public Vector3f multiply(Vector3f vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
        return this;
    }

    public Vector3f multiply(float x, float y) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3f multiply(float value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }
}
