package com.nut.client.gui.shape;

import com.nut.client.gui.guicomponent.GuiComponent;
import com.nut.client.utils.BColor;
import com.nut.client.utils.RenderUtils;

public class Circle extends Shape<Circle> {

    private float radius;
    private float shade;

    public Circle(GuiComponent guiComponent, int width, int height, BColor color) {
        super(guiComponent, width, height, color);
    }

    public Circle(GuiComponent guiComponent, BColor color) {
        super(guiComponent, color);
    }

    public Circle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public Circle shade(float shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public void push() {
        RenderUtils.drawCircle(x, y, width, height, radius, shade, color);
    }
}
