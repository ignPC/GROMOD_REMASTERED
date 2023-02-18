package com.nut.client.gui.shapes;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape {
    private int x;
    private int y;
    private int width;
    private int height;

    protected List<AbstractShape> subclassList = new ArrayList<>();


    public AbstractShape(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(){
        for(AbstractShape shape : subclassList){
            shape.draw();
        }
    }

    public void add(AbstractShape shapeObject){
        subclassList.add(shapeObject);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

