package com.nut.client.utils;

public class Vector2i {

    public int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2i vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int value) {
        x = value;
        y = value;
    }

    public void add(Vector2i vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void add(int value) {
        x += value;
        y += value;
    }
}
