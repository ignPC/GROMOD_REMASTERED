package com.nut.client.gui.shape;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape {
    private int originalX;
    private int originalY;
    private int originalWidth;
    private int originalHeight;

    private int x;
    private int y;
    private int width;
    private int height;
    private int margin = 0;
    private int padding = 0;
    private FloatDir floatDir = FloatDir.NONE;
    private AbstractShape parent;
    protected List<AbstractShape> childShapeList = new ArrayList<>();

    public AbstractShape(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);

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

    /***========================================BUILDER========================================***/

    public static class ShapeBuilder {
        private final AbstractShape shape;

        public int padding = 0;
        public int margin = 0;
        private FloatDir sFloat;

        public ShapeBuilder(AbstractShape shape) {
            this.shape = shape;
        }

        public ShapeBuilder withPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public ShapeBuilder withMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public ShapeBuilder withFloat(FloatDir sFloat){
            this.sFloat = sFloat;
            return this;
        }

        public AbstractShape build() {
            shape.setPadding(padding);
            shape.setMargin(margin);
            shape.setFloat(sFloat);
            return shape;
        }
    }


    /***========================================SETTERS========================================***/

    public void setX(int x) {
        if(parent != null) {
            switch (floatDir) {
                case NONE:
                case TOP:
                case BOTTOM:
                    break;
                case RIGHT:
                case LEFT:
                    floatX(x);
                    break;
                default:
                    throw new IllegalArgumentException("Shape Float not set correctly.");
            }
        }

        this.x = originalX + margin;
    }

    public void setY(int y) {
        if(parent != null) {
            switch (floatDir) {
                case NONE:
                case RIGHT:
                case LEFT:
                    break;
                case TOP:
                case BOTTOM:
                    floatY(y);
                    break;
                default:
                    throw new IllegalArgumentException("Shape Float not set correctly.");
            }
        }

        this.y = originalY + margin;
    }

    public void setWidth(int width) {
        if (height < 0) {
            throw new IllegalArgumentException("Width cannot be negative.");
        }

        if (floatDir != FloatDir.NONE && parent != null) {
            if (floatDir == FloatDir.LEFT) {
                int maxX = parent.getInnerX();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && sibling.getFloat() != FloatDir.RIGHT) {
                        maxX = Math.max(maxX, sibling.getOuterX() + sibling.getOuterWidth());
                    }
                }
                this.width = maxX - x - margin;
            } else if (floatDir == FloatDir.RIGHT) {
                int minX = parent.getInnerX() + parent.getInnerWidth();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && sibling.getFloat() != FloatDir.LEFT) {
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
            if (floatDir == FloatDir.BOTTOM) {
                int maxY = parent.getInnerY();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && sibling.getFloat() != FloatDir.TOP) {
                        maxY = Math.max(maxY, sibling.getOuterY() + sibling.getOuterHeight());
                    }
                }
                this.height = maxY - y - margin;
            } else if (floatDir == FloatDir.TOP) {
                int minY = parent.getInnerY() + parent.getInnerHeight();
                for (AbstractShape sibling : parent.childShapeList) {
                    if (sibling != this && sibling.getFloat() != FloatDir.BOTTOM) {
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
        if (floatDir == FloatDir.RIGHT) {
            int minX = parent.getInnerX() + parent.getInnerWidth();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && sibling.getFloat() != FloatDir.LEFT) {
                    minX = Math.min(minX, sibling.getOuterX());
                }
            }
            this.x = Math.min(minX - width, x - margin);
        } else if (floatDir == FloatDir.LEFT) {
            int maxX = parent.getInnerX();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && sibling.getFloat() != FloatDir.RIGHT) {
                    maxX = Math.max(maxX, sibling.getOuterX() + sibling.getOuterWidth());
                }
            }
            this.x = Math.max(maxX, x + margin);
        }
    }

    public void floatY(int y){
        if (floatDir == FloatDir.BOTTOM) {
            assert parent != null;
            int minY = parent.getInnerY() + parent.getInnerHeight();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && sibling.getFloat() != FloatDir.TOP) {
                    minY = Math.min(minY, sibling.getOuterY());
                }
            }
            this.y = Math.min(minY - height, y - margin);
        } else if (floatDir == FloatDir.TOP) {
            assert parent != null;
            int maxY = parent.getInnerY();
            for (AbstractShape sibling : parent.childShapeList) {
                if (sibling != this && sibling.getFloat() != FloatDir.BOTTOM) {
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

    private FloatDir getFloat() {
        return floatDir;
    }

    public AbstractShape getParent() {
        return parent;
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

