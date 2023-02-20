package com.nut.client.gui.shape;

import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractShape {
    protected final int originalX;
    protected final int originalY;
    protected final int originalWidth;
    protected final int originalHeight;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Color color;
    protected int margin = 0;
    protected int padding = 0;
    protected FloatDir floatDir = FloatDir.NONE;
    protected AbstractShape parent = null;
    protected List<AbstractShape> childShapeList = new ArrayList<>();

    public AbstractShape(int x, int y, int width, int height, Color color) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setColor(color);

        this.originalX = x;
        this.originalY = y;
        this.originalWidth = width;
        this.originalHeight = height;
    }

    public void draw() {
        for (AbstractShape shape : childShapeList) {
            shape.draw();
        }
    }

    public void add(AbstractShape shapeObject){
        shapeObject.setParent(this);
        childShapeList.add(shapeObject);
    }

    public abstract void pushToPipeline();

    /***========================================BUILDER========================================***/

    public AbstractShape withPadding(int padding) {
        setPadding(padding);
        return this;
    }

    public AbstractShape withMargin(int margin) {
        setMargin(margin);
        return this;
    }

    public AbstractShape withFloat(FloatDir sFloat){
        setFloat(sFloat);
        return this;
    }

    /***========================================SETTERS========================================***/

    public void setX(int x) {
        if(parent != null) {
            this.x = originalX + getParent().getPadding() + getMargin();
            switch (floatDir) {
                case NONE:
                case TOP:
                case BOTTOM:
                    break;
                case RIGHT:
                case TOP_RIGHT:
                case BOTTOM_RIGHT:
                case LEFT:
                case TOP_LEFT:
                case BOTTOM_LEFT:
                    floatX(x);
                    break;
                default:
                    throw new IllegalArgumentException("Shape Float not set correctly.");
            }
            return;
        }

        this.x = originalX + margin;
    }

    public void setY(int y) {
        if(parent != null) {
            this.y = originalY + getParent().getPadding() + getMargin();
            switch (floatDir) {
                case NONE:
                case RIGHT:
                case LEFT:
                    break;
                case TOP:
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                    floatY(y);
                    break;
                default:
                    throw new IllegalArgumentException("Shape Float not set correctly.");
            }
            return;
        }

        this.y = originalY + margin;
    }

    public void setWidth(int width) {
        if (height < 0) {
            throw new IllegalArgumentException("Width cannot be negative.");
        }

        if (floatDir != FloatDir.NONE && parent != null) {
            if (FloatDir.getLeft().contains(floatDir)) {
                int maxX = parent.getInnerX();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && FloatDir.getRight().contains(sibling.getFloat())) {
                        maxX = Math.max(maxX, sibling.getOuterX() + sibling.getOuterWidth());
                    }
                }
                this.width = maxX - x - margin;
            } else if (FloatDir.getRight().contains(floatDir)) {
                int minX = parent.getInnerX() + parent.getInnerWidth();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && FloatDir.getLeft().contains(sibling.getFloat())) {
                        minX = Math.min(minX, sibling.getOuterX());
                    }
                }
                this.width = minX - x - margin;
            } else {
                this.width = width;
            }
        } else {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative.");
        }

        if (floatDir != FloatDir.NONE && parent != null) {
            if (FloatDir.getBottom().contains(floatDir)) {
                int maxY = parent.getInnerY();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && FloatDir.getTop().contains(sibling.getFloat())) {
                        maxY = Math.max(maxY, sibling.getOuterY() + sibling.getOuterHeight());
                    }
                }
                this.height = maxY - y - margin;
            } else if (FloatDir.getTop().contains(floatDir)) {
                int minY = parent.getInnerY() + parent.getInnerHeight();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && FloatDir.getBottom().contains(sibling.getFloat())) {
                        minY = Math.min(minY, sibling.getOuterX());
                    }
                }
                this.height = minY - y - margin;
            } else {
                this.height = height;
            }
        } else {
            this.height = height;
        }
    }

    public void floatX(int x){
        if (FloatDir.getRight().contains(floatDir)) {
            int minX = parent.getInnerX() + parent.getInnerWidth();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && FloatDir.getLeft().contains(sibling.getFloat())) {
                    minX = Math.min(minX, sibling.getOuterX());
                }
            }
            this.x = Math.min(minX - width, x - margin);
        } else if (FloatDir.getLeft().contains(floatDir)) {
            int maxX = parent.getInnerX();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && FloatDir.getRight().contains(sibling.getFloat())) {
                    maxX = Math.max(maxX, sibling.getOuterX() + sibling.getOuterWidth());
                }
            }
            this.x = Math.max(maxX, x + margin);
        }
    }

    public void floatY(int y){
        if (FloatDir.getBottom().contains(floatDir)) {
            assert parent != null;
            int minY = parent.getInnerY() + parent.getInnerHeight();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && FloatDir.getTop().contains(sibling.getFloat())) {
                    minY = Math.min(minY, sibling.getOuterY());
                }
            }
            this.y = Math.min(minY - height, y - margin);
        } else if (FloatDir.getTop().contains(floatDir)) {
            assert parent != null;
            int maxY = parent.getInnerY();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && FloatDir.getBottom().contains(sibling.getFloat())) {
                    maxY = Math.max(maxY, sibling.getOuterY() + sibling.getOuterHeight());
                }
            }
            this.y = Math.max(maxY, y + margin);
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        for (AbstractShape shape : childShapeList) {
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
        }
    }

    private void setFloat(FloatDir sFloat) {
        this.floatDir = sFloat;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        for (AbstractShape shape : childShapeList) {
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
        }
    }

    public void setPadding(int padding) {
        this.padding = padding;
        for (AbstractShape shape : childShapeList) {
            shape.setX(x);
            shape.setY(y);
            shape.setWidth(width);
            shape.setHeight(height);
        }
    }

    private void setColor(Color color) {
        this.color = color;
    }

    public void setParent(AbstractShape parent) {
        this.parent = parent;
    }

    /***========================================GETTERS========================================***/

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

    private FloatDir getFloat() {
        return floatDir;
    }

    /***========================================DEBUG========================================***/

    public String getInformation() {
        return "AbstractShape{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", margin=" + margin +
                ", padding=" + padding +
                ", floatDir=" + floatDir +
                ", parent=" + parent +
                ", childShapeList=" + childShapeList +
                '}';

    }
}

