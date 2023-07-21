package com.gromod.client.gui.shape;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.RenderUtils;

public class Rectangle extends Shape<Rectangle> {

    private float radius;
    private float shade;

    public Rectangle(GuiComponent guiComponent, int width, int height, BColor color) {
        super(guiComponent, width, height, color);
    }

    public Rectangle(GuiComponent guiComponent, BColor color) {
        super(guiComponent, color);
    }

    public Rectangle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public Rectangle shade(float shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public void push() {
        RenderUtils.drawRoundedRectangle(x, y, width, height, radius, shade, color);
    }
}
