package com.nut.client.utils;

public class Vector3d {

    public double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d set(Vector3d vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        return this;
    }

    public Vector3d set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3d set(double value) {
        x = value;
        y = value;
        z = value;
        return this;
    }

    public Vector3d add(Vector3d vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
        return this;
    }

    public Vector3d add(double x, double y) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3d add(double value) {
        x += value;
        y += value;
        z += value;
        return this;
    }

    public Vector3d subtract(Vector3d vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
        return this;
    }

    public Vector3d subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3d subtract(double value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }

    public Vector3d multiply(Vector3d vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
        return this;
    }

    public Vector3d multiply(double x, double y) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3d multiply(double value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }
}
