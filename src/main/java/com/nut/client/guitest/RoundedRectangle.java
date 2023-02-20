package com.nut.client.guitest;

import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;

public class RoundedRectangle extends AbstractShape {

    private float radius;
    private float shade;
    private final ShapeType shapeType = ShapeType.RRECTANGLE;

    public RoundedRectangle(float x, float y, float width, float height, Color color) {
        super(x, y, width, height, color);
    }

    public RoundedRectangle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public RoundedRectangle shade(float shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public void pushToPipeline() {
        RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
        for (int i = 0; i < 4; i++) {
            RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
            RenderPipeline.queueData(RenderPipeline.shapePositions, x, y);
            RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
            RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
            RenderPipeline.queueData(RenderPipeline.shadeFloats, shade);
            RenderPipeline.queueData(RenderPipeline.haloFloats, 0);
            RenderPipeline.queueData(RenderPipeline.shapeTypeFloats, shapeType.ordinal());
        }
    }
}
