package com.nut.client.renderer;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.AfterScreenCreationEvent;
import com.nut.client.event.GuiRenderEvent;
import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.renderer.util.ProjectionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

@Component
public class RenderPipeline {

    public static int shapes;
    private static GLObject pipeline;

    private Shader shader;

    public static final List<Float> quadPositions = new ArrayList<>();
    public static final List<Float> colors = new ArrayList<>();
    public static final List<Float> shapePositions = new ArrayList<>();
    public static final List<Float> shapeSizes = new ArrayList<>();
    public static final List<Float> radiusFloats = new ArrayList<>();
    public static final List<Float> shadeFloats = new ArrayList<>();
    public static final List<Float> haloFloats = new ArrayList<>();
    public static final List<Float> shapeTypeFloats = new ArrayList<>();

    @AutoInit
    public RenderPipeline() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiRender(GuiRenderEvent event) {
        if (BaseGui.currentScreen == null) return;

        glUseProgram(shader.getShaderProgram());
        glUniformMatrix4(shader.getUniform("projection"), false, ProjectionUtils.orthoProjection);

        glEnable(GL_BLEND);
        glBlendFunc(770, 771);

        pipeline.render(0, shapes * 4);
        pipeline.unbindVao();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onAfterScreenCreation(AfterScreenCreationEvent event) {
        shader = new Shader(
                new ResourceLocation("bean", "shaders/guiVS.glsl"),
                new ResourceLocation("bean", "shaders/guiFS.glsl"),
                "projection"
        );

        pipeline = new GLObject(GL_QUADS);
        pipeline
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER) // Quad positions 4 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Color 1 * vec4
                .addVbo(GL_ARRAY_BUFFER) // Shape position 1 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Shape size 1 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Radius 1 * float (Rounded rectangle)
                .addVbo(GL_ARRAY_BUFFER) // Shade 1 * float
                .addVbo(GL_ARRAY_BUFFER) // Halo 1 * float (Circle)
                .addVbo(GL_ARRAY_BUFFER) // Shape type 1 * float
                .bindVbo(0)
                .populateVao(0, 2, GL_FLOAT, 0, 0)
                .bindVbo(1)
                .populateVao(1, 4, GL_FLOAT, 0, 0)
                .bindVbo(2)
                .populateVao(2, 2, GL_FLOAT, 0, 0)
                .bindVbo(3)
                .populateVao(3, 2, GL_FLOAT, 0, 0)
                .bindVbo(4)
                .populateVao(4, 1, GL_FLOAT, 0, 0)
                .bindVbo(5)
                .populateVao(5, 1, GL_FLOAT, 0, 0)
                .bindVbo(6)
                .populateVao(6, 1, GL_FLOAT, 0, 0)
                .bindVbo(7)
                .populateVao(7, 1, GL_FLOAT, 0, 0)
                .unbindVbo(7)
                .unbindVao();
    }

    // Run this if you expect any changes in shape data
    public static void refreshPipeline() {
        pipeline
                .populateVbo(0, list2Array(quadPositions), GL_STATIC_DRAW)
                .populateVbo(1, list2Array(colors), GL_STATIC_DRAW)
                .populateVbo(2, list2Array(shapePositions), GL_STATIC_DRAW)
                .populateVbo(3, list2Array(shapeSizes), GL_STATIC_DRAW)
                .populateVbo(4, list2Array(radiusFloats), GL_STATIC_DRAW)
                .populateVbo(5, list2Array(shadeFloats), GL_STATIC_DRAW)
                .populateVbo(6, list2Array(haloFloats), GL_STATIC_DRAW)
                .populateVbo(7, list2Array(shapeTypeFloats), GL_STATIC_DRAW)
                .unbindVbo(7);
    }

    public static void queueData(List<Float> floats, float... data) {
        for (float f : data)
            floats.add(f);
    }

    private static float[] list2Array(List<Float> data) {
        float[] floats = new float[data.size()];
        for (int i = 0; i < floats.length; i++)
            floats[i] = data.get(i);
        return floats;
    }

    public static void clearPipeline() {
        shapes = 0;
        quadPositions.clear();
        colors.clear();
        shapePositions.clear();
        shapeSizes.clear();
        radiusFloats.clear();
        shadeFloats.clear();
        haloFloats.clear();
        shapeTypeFloats.clear();
    }
}
