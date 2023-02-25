package com.nut.client.gui.testshape;

import com.nut.client.gui.shape.ShapeType;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
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
        int width = (int) (this.width * xScale);
        int height = (int) (this.height * yScale);
        int x = (int) (this.x * xScale);
        int y = (int) ((Display.getHeight() - this.y * yScale - height));
        float radius = this.radius * xScale;
        float shade = this.shade * xScale;
        RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
        for (int i = 0; i < 4; i++) {
            RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
            RenderPipeline.queueData(RenderPipeline.shapePositions, x, y);
            RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
            RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
            RenderPipeline.queueData(RenderPipeline.shadeFloats, shade);
            RenderPipeline.queueData(RenderPipeline.haloFloats, 0);
            RenderPipeline.queueData(RenderPipeline.shapeTypeFloats, ShapeType.ROUNDEDRECTANGLE.ordinal());
            RenderPipeline.queueData(RenderPipeline.textureCoordFloats, 0, 0);
        }
        RenderPipeline.shapes++;
    }
}
