package com.nut.client.gui.shape;

import com.nut.client.utils.BColor;
import com.nut.client.utils.Vector2i;

public abstract class Shape<T> {

    public int x;
    public int y;
    public int width;
    public int height;
    public BColor color;
    public final Margin margin = new Margin(0, 0, 0, 0);

    public Shape(int x, int y, int width, int height, BColor color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Shape(int width, int height, BColor color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract void push();

    public T margin(int left, int top, int right, int bottom) {
        margin.set(left, top, right, bottom);
        return (T) this;
    }

    public T setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return (T) this ;
    }

    public T setPosition(Vector2i vector) {
        x = vector.x;
        y = vector.y;
        return (T) this;
    }

    public T setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    public T setSize(Vector2i vector) {
        width = vector.x;
        height = vector.y;
        return (T) this;
    }
}
