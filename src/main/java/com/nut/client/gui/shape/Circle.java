package com.nut.client.gui.shape;

import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.utils.Color;
import com.nut.client.utils.RenderUtils;
import org.lwjgl.opengl.Display;

public class Circle extends Shape<Circle> {

    private float radius;
    private float shade;
    private float halo;

    public Circle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    public Circle(int width, int height, Color color) {
        super(width, height, color);
    }

    public Circle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public Circle shade(float shade) {
        this.shade = shade;
        return this;
    }

    public Circle halo(float halo) {
        this.halo = halo;
        return this;
    }

    @Override
    public void push() {
        float xScale = BaseGui.scaled.getXScale();
        float yScale = BaseGui.scaled.getYScale();

        int width = (int) (this.width * xScale);
        int height = (int) (this.height * yScale);
        int x = (int) (this.x * xScale);
        int y = (int) ((Display.getHeight() - this.y * yScale - height));
        float radius = this.radius * xScale;
        float shade = this.shade * xScale;

        RenderUtils.drawCircle(x, y, width, height, radius, shade, color);
    }
}
