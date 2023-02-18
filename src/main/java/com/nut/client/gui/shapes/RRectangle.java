package com.nut.client.gui.shapes;

public class RRectangle extends AbstractShape {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int radius;

    public RRectangle(int x, int y, int width, int height, int radius){
        super(x, y, width, height);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }

    public void draw(){

    }
}
