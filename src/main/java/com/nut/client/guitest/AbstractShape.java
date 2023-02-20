package com.nut.client.guitest;

import com.nut.client.utils.Color;

public abstract class AbstractShape {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected Color color;

    public AbstractShape(float x, float y, float width, float height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract void pushToPipeline();
}
