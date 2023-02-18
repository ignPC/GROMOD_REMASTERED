package com.nut.client.gui.shapes;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape {
    private int x;
    private int y;
    private int width;
    private int height;
    private int margin;
    private int padding;
    private AbstractShape parent;
    protected List<AbstractShape> subclassList = new ArrayList<>();

    public AbstractShape(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
    }

    public void draw() {
        for (AbstractShape shape : subclassList) {
            shape.draw();
        }
    }

    public void add(AbstractShape shapeObject){
        shapeObject.setParent(this);
        subclassList.add(shapeObject);
    }

    /***========================================BUILDER========================================***/

    public static class ShapeBuilder {
        private final Class<? extends AbstractShape> clazz;
        private int padding;
        private int margin;

        public ShapeBuilder(Class<? extends AbstractShape> clazz) {
            this.clazz = clazz;
        }

        public ShapeBuilder withPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public ShapeBuilder withMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public AbstractShape build(int x, int y, int width, int height) throws IllegalAccessException, InstantiationException {
            AbstractShape shape = clazz.newInstance();
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
            shape.setPadding(padding);
            shape.setMargin(margin);
            return shape;
        }
    }

    /***========================================SETTERS========================================***/

    public void setX(int x) {
        this.x = x + this.margin;
    }

    public void setY(int y) {
        this.y = y + this.margin;
    }

    public void setWidth(int width) {
        if (parent != null && x + width + margin > parent.width - parent.margin - x) {
            this.width = parent.width - parent.margin - x - margin;
        } else {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        if (parent != null && y + height + margin > parent.height - parent.margin - y) {
            this.height = parent.height - parent.margin - y - margin;
        } else {
            this.height = height;
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public void setPadding(int padding) {
        this.padding = padding;
        for (AbstractShape shape : subclassList) {
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
        }
    }

    public void setParent(AbstractShape parent) {
        this.parent = parent;
    }

    /***========================================GETTERS========================================***/

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMargin() {
        return margin;
    }

    public int getPadding() {
        return padding;
    }

    public int getInnerX() {
        return x + padding;
    }

    public int getInnerY() {
        return y + padding;
    }

    public int getInnerWidth() {
        return width - padding * 2;
    }

    public int getInnerHeight() {
        return height - padding * 2;
    }

    public int getOuterX() {
        return x - margin;
    }

    public int getOuterY() {
        return y - margin;
    }

    public int getOuterWidth() {
        return width + margin * 2;
    }

    public int getOuterHeight() {
        return height + margin * 2;
    }

    public AbstractShape getParent() {
        return parent;
    }
}

