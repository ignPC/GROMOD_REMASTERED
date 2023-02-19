package com.nut.client.renderer;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.AfterScreenCreationEvent;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.gui.BaseGui;
import com.nut.client.renderer.util.ProjectionUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

@Component
public class RenderPipeline {

    private final GLObject pipeline = new GLObject(GL_QUADS, 0, 4);
    private final Shader circle = new Shader(
            new ResourceLocation("bean", "shaders/circleVS.glsl"),
            new ResourceLocation("bean", "shaders/circleFS.glsl"),
            "projection"
    );

    public static final List<Float> quadPositions = new ArrayList<>();
    public static final List<Float> colors = new ArrayList<>();
    public static final List<Float> shapePositions = new ArrayList<>();
    public static final List<Float> shapeSizes = new ArrayList<>();
    public static final List<Float> radiusFloats = new ArrayList<>();
    public static final List<Float> shadeFloats = new ArrayList<>();
    public static final List<Float> haloFloats = new ArrayList<>();

    @AutoInit
    public RenderPipeline() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (BaseGui.currentScreen == null) return;
        glUseProgram(circle.getShaderProgram());
        glUniformMatrix4(circle.getUniform("projection"), false, ProjectionUtils.orthoProjection);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);

        pipeline.render();
        pipeline.unbindVao();

        GlStateManager.enableTexture2D();
        glUseProgram(0);
    }

    @SubscribeEvent
    public void onGuiChange(GuiOpenEvent event) {
        pipeline
                .populateVbo(0, list2Array(quadPositions), GL_STATIC_DRAW)
                .populateVbo(1, list2Array(colors), GL_STATIC_DRAW)
                .populateVbo(2, list2Array(shapePositions), GL_STATIC_DRAW)
                .populateVbo(3, list2Array(shapeSizes), GL_STATIC_DRAW)
                .populateVbo(4, list2Array(radiusFloats), GL_STATIC_DRAW)
                .populateVbo(5, list2Array(shadeFloats), GL_STATIC_DRAW)
                .populateVbo(6, list2Array(haloFloats), GL_STATIC_DRAW);
    }

    @SubscribeEvent
    public void onAfterScreenCreation(AfterScreenCreationEvent event) {
        pipeline
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER) // Quad positions 4 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Color 1 * vec4
                .addVbo(GL_ARRAY_BUFFER) // Shape position 1 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Shape size 1 * vec2
                .addVbo(GL_ARRAY_BUFFER) // Radius 1 * float (Rounded rectangle)
                .addVbo(GL_ARRAY_BUFFER) // Shade 1 * float
                .addVbo(GL_ARRAY_BUFFER) // Halo 1 * float (Circle)

                .bindVbo(0)
                .populateVao(0, 2, GL_FLOAT, 0, 0)

                .bindVbo(1)
                .populateVao(1, 4, GL_FLOAT, 0, 0)
                .divisor(1, 1)

                .bindVbo(2)
                .populateVao(2, 2, GL_FLOAT, 0, 0)
                .divisor(2, 1)

                .bindVbo(3)
                .populateVao(3, 2, GL_FLOAT, 0, 0)
                .divisor(3, 1)

                .bindVbo(4)
                .populateVao(4, 1, GL_FLOAT, 0, 0)
                .divisor(4, 1)

                .bindVbo(5)
                .populateVao(5, 1, GL_FLOAT, 0, 0)
                .divisor(5, 1)

                .bindVbo(6)
                .populateVao(6, 1, GL_FLOAT, 0, 0)
                .divisor(6, 1)

                .unbindVbo(6)
                .unbindVao();
    }

    public static void queueData(List<Float> floats, float... data) {
        for (float f : data)
            floats.add(f);
    }

    private float[] list2Array(List<Float> data) {
        float[] floats = new float[data.size()];
        for (int i = 0; i < floats.length; i++)
            floats[i] = data.get(i);
        System.out.println(Arrays.toString(floats));
        return floats;
    }

    public static void clearPipeline() {
        quadPositions.clear();
        colors.clear();
        shapePositions.clear();
        shapeSizes.clear();
        radiusFloats.clear();
        shadeFloats.clear();
        haloFloats.clear();
    }
}
