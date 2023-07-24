package com.gromod.client.gui.shape;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.RenderUtils;

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

    public Circle centerY() {
        int height = this.height;
        int parentCenterY = parent.y + parent.height / 2;
        this.y = parentCenterY - (height / 2);
        return this;
    }

    @Override
    public void push() {
        RenderUtils.drawCircle(x, y, width, height, radius, shade, color);
    }

}
