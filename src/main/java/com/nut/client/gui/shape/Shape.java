package com.nut.client.gui.shape;

import com.nut.client.gui.guicomponent.GuiComponent;
import com.nut.client.utils.BColor;

public abstract class Shape<T> {

    public int x;
    public int y;
    public int width;
    public int height;

    public BColor color;
    public GuiComponent parent;

    public Shape(GuiComponent guiComponent, int width, int height, BColor color) {
        x = guiComponent.x;
        y = guiComponent.y;
        this.width = width;
        this.height = height;
        this.color = color;
        parent = guiComponent;
        parent.shapes.add(this);
    }

    public Shape(GuiComponent guiComponent, BColor color) {
        x = guiComponent.x;
        y = guiComponent.y;
        width = guiComponent.width;
        height = guiComponent.height;
        this.color = color;
        parent = guiComponent;
        parent.shapes.add(this);
    }

    public abstract void push();

    public T offset(int offsetX, int offsetY) {
        x = parent.x + offsetX;
        y = parent.y + offsetY;
        return (T) this;
    }

    public T setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }
}
