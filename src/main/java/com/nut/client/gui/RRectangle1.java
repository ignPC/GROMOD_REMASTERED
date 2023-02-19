package com.nut.client.gui;

import com.nut.client.renderer.RenderPipeline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RRectangle1 extends AbstractShape1 {

    private float radius;
    private float shade;
    private float halo;

    public RRectangle1(float x, float y, float width, float height, Color color) {
        super(x, y, width, height, color);
    }

    public RRectangle1 radius(float radius) {
        this.radius = radius;
        return this;
    }

    public RRectangle1 shade(float shade) {
        this.shade = shade;
        return this;
    }

    public RRectangle1 halo(float halo) {
        this.halo = halo;
        return this;
    }

    public void pushToPipeline() {
        RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
        RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
        RenderPipeline.queueData(RenderPipeline.shapePositions, x, y);
        RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
        RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
        RenderPipeline.queueData(RenderPipeline.shadeFloats, shade);
        RenderPipeline.queueData(RenderPipeline.haloFloats, halo);
    }
}
