package com.nut.client.gui.shapes;

public class RRectangle extends AbstractShape {
    public final int radius;

    public RRectangle(int x, int y, int width, int height, int radius){
        super(x, y, width, height);

        this.radius = radius;
    }

    public void draw(){
        System.out.println("Drawing Rect");
    }
}
