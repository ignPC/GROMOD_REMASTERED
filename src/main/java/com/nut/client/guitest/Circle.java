package com.nut.client.guitest;

import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;

public class Circle extends AbstractShape {

    private float radius;
    private float shade;
    private float halo;
    private final ShapeType shapeType = ShapeType.CIRCLE;

    public Circle(float x, float y, float width, float height, Color color) {
        super(x, y, width, height, color);
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
    public void pushToPipeline() {
        RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
        for (int i = 0; i < 4; i++) {
            RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
            RenderPipeline.queueData(RenderPipeline.shapePositions, x, y);
            RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
            RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
            RenderPipeline.queueData(RenderPipeline.shadeFloats, shade);
            RenderPipeline.queueData(RenderPipeline.haloFloats, halo);
            RenderPipeline.queueData(RenderPipeline.shapeTypeFloats, shapeType.ordinal());
        }
    }
}
