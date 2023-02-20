package com.nut.client.gui.shape.shapes;

import com.nut.client.gui.shape.ShapeType;
import com.nut.client.utils.Color;
import com.nut.client.gui.shape.AbstractShape;
import com.nut.client.renderer.RenderPipeline;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RRectangle extends AbstractShape {

    private float radius;
    private float shade;
    private float halo;
    private final ShapeType shapeType = ShapeType.ROUNDEDRECTANGLE;

    public RRectangle(int x, int y, int width, int height, Color color){
        super(x, y, width, height, color);
    }

    public RRectangle withRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public RRectangle withShade(float shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public void draw(){
        pushToPipeline();
        super.draw();
    }

    public void pushToPipeline() {
        RenderPipeline.queueData(RenderPipeline.quadPositions, 800, y + height, 800, y, 800 + width, y, 800 + width, y + height);
        for (int i = 0; i < 4; i++) {
            RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
            RenderPipeline.queueData(RenderPipeline.shapePositions, 800, y);
            RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
            RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
            RenderPipeline.queueData(RenderPipeline.shadeFloats, shade);
            RenderPipeline.queueData(RenderPipeline.haloFloats, halo);
            RenderPipeline.queueData(RenderPipeline.shapeTypeFloats, shapeType.ordinal());
        }
        RenderPipeline.shapes++;
    }
}
