package com.nut.client.gui;

public abstract class AbstractShape1 {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected Color color;

    public AbstractShape1(float x, float y, float width, float height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract void pushToPipeline();
}