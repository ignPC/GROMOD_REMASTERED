package com.nut.client.gui.shape;

import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
import com.nut.client.utils.ShapeType;
import org.lwjgl.opengl.Display;

public class RRectangle extends Shape<RRectangle> {

    private float radius;
    private float shade;

    public RRectangle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    public RRectangle(int width, int height, Color color) {
        super(width, height, color);
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
        float xScale = BaseGui.scaled.getXScale();
        float yScale = BaseGui.scaled.getYScale();

        int width = (int) (this.width * xScale);
        int height = (int) (this.height * yScale);
        int x = (int) (this.x * xScale);
        int y = (int) ((Display.getHeight() - this.y * yScale - height));
        float radius = this.radius * xScale;
        float shade = this.shade * xScale;

        RenderPipeline.queueGuiData(
                x, y + height, color.r, color.g, color.b, color.a, x, y, width, height, radius, shade, 0, ShapeType.ROUNDEDRECTANGLE.ordinal(), 0, 0,
                x, y, color.r, color.g, color.b, color.a, x, y, width, height, radius, shade, 0, ShapeType.ROUNDEDRECTANGLE.ordinal(), 0, 0,
                x + width, y, color.r, color.g, color.b, color.a, x, y, width, height, radius, shade, 0, ShapeType.ROUNDEDRECTANGLE.ordinal(), 0, 0,
                x + width, y + height, color.r, color.g, color.b, color.a, x, y, width, height, radius, shade, 0, ShapeType.ROUNDEDRECTANGLE.ordinal(), 0, 0
        );
        RenderPipeline.guiShapes++;
    }
}
