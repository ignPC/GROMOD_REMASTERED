package com.nut.client.gui.shape.shapes;

import com.nut.client.utils.Color;
import com.nut.client.gui.shape.AbstractShape;
import com.nut.client.renderer.RenderPipeline;
import org.spongepowered.asm.mixin.Implements;

public class RRectangle extends AbstractShape {
    private final int radius;

    public RRectangle(int x, int y, int width, int height, Color color, int radius){
        super(x, y, width, height, color);

        this.radius = radius;
    }

    @Override
    public void draw(){
        super.draw();
        pushToPipeline();
    }

    public void pushToPipeline() {
        RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
        RenderPipeline.queueData(RenderPipeline.colors, color.getR(), color.getG(), color.getB(), color.getA());
        RenderPipeline.queueData(RenderPipeline.shapePositions, x, y);
        RenderPipeline.queueData(RenderPipeline.shapeSizes, width, height);
        RenderPipeline.queueData(RenderPipeline.radiusFloats, radius);
        RenderPipeline.queueData(RenderPipeline.shadeFloats, 2.0f);
        RenderPipeline.queueData(RenderPipeline.haloFloats, 2.0f);
    }

    public int getRadius() {
        return radius;
    }
}
