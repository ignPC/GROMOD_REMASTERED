package com.gromod.client.utils;

public class BVector3d {

    public double x, y, z;

    public BVector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BVector3d set(BVector3d vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        return this;
    }

    public BVector3d set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public BVector3d set(double value) {
        x = value;
        y = value;
        z = value;
        return this;
    }

    public BVector3d add(BVector3d vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
        return this;
    }

    public BVector3d add(double x, double y) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public BVector3d add(double value) {
        x += value;
        y += value;
        z += value;
        return this;
    }

    public BVector3d subtract(BVector3d vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
        return this;
    }

    public BVector3d subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public BVector3d subtract(double value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }

    public BVector3d multiply(BVector3d vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
        return this;
    }

    public BVector3d multiply(double x, double y) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public BVector3d multiply(double value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }
}
