package com.gromod.client.gui.shape;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.RenderUtils;

public class RRectangle extends Shape<RRectangle> {

    private float radius;
    private float shade;

    public RRectangle(GuiComponent guiComponent, int width, int height, BColor color) {
        super(guiComponent, width, height, color);
    }

    public RRectangle(GuiComponent guiComponent, BColor color) {
        super(guiComponent, color);
    }

    public RRectangle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public RRectangle shade(float shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public void push() {
        RenderUtils.drawRoundedRectangle(x, y, width, height, radius, shade, color);
    }
}
