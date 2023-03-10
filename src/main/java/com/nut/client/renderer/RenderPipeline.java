package com.nut.client.renderer;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.AfterScreenCreationEvent;
import com.nut.client.event.GuiRenderEvent;
import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.renderer.font.FontAtlasBuilder;
import com.nut.client.renderer.util.CommonShapes;
import com.nut.client.renderer.util.ProjectionUtils;
import com.nut.client.utils.UvCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

@Component
public class RenderPipeline {

    public static int guiShapes;
    public static int worldShapes;
    private static GLObject guiPipeline;
    private static GLObject worldPipeline;
    private static final List<Float> guiData = new ArrayList<>();
    private static final List<Float> worldData = new ArrayList<>();

    private final Minecraft minecraft;

    private Shader guiShader;
    private Shader worldShader;

    @AutoInit
    public RenderPipeline(Minecraft minecraft) {
        this.minecraft = minecraft;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiRender(GuiRenderEvent event) {
        if (BaseGui.currentScreen == null) return;
        if (guiData.isEmpty()) return;

        glUseProgram(guiShader.getShaderProgram());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, FontAtlasBuilder.textureId);
        glUniformMatrix4(guiShader.getUniform("projection"), false, ProjectionUtils.guiProjection);
        glUniform1i(guiShader.getUniform("font_atlas"), 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        guiPipeline.render(0, guiShapes * 4);
        guiPipeline.unbindVao();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (minecraft.thePlayer == null || minecraft.theWorld == null) return;
        if (worldData.isEmpty()) return;

        glUseProgram(worldShader.getShaderProgram());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 8);
        glUniformMatrix4(worldShader.getUniform("projection"), false, ProjectionUtils.worldProjection);
        glUniform1i(worldShader.getUniform("minecraft_atlas"), 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        refreshWorldPipeline();

        worldPipeline.renderInstanced(0, 24, worldShapes);
        worldPipeline.unbindVao();

        clearWorldPipeline();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onAfterScreenCreation(AfterScreenCreationEvent event) {
        guiShader = new Shader(
                new ResourceLocation("bean", "shaders/guiVS.glsl"),
                new ResourceLocation("bean", "shaders/guiFS.glsl"),
                "projection",
                "font_atlas"
        );

        worldShader = new Shader(
                new ResourceLocation("bean", "shaders/worldVS.glsl"),
                new ResourceLocation("bean", "shaders/worldFS.glsl"),
                "projection",
                "minecraft_atlas"
        );

        guiPipeline = new GLObject(GL_QUADS)
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER)
                .bindVbo(0)
                .populateVao(0, 2, GL_FLOAT, 16 * Float.BYTES, 0)
                .populateVao(1, 4, GL_FLOAT, 16 * Float.BYTES, 2 * Float.BYTES)
                .populateVao(2, 2, GL_FLOAT, 16 * Float.BYTES, 6 * Float.BYTES)
                .populateVao(3, 2, GL_FLOAT, 16 * Float.BYTES, 8 * Float.BYTES)
                .populateVao(4, 1, GL_FLOAT, 16 * Float.BYTES, 10 * Float.BYTES)
                .populateVao(5, 1, GL_FLOAT, 16 * Float.BYTES, 11 * Float.BYTES)
                .populateVao(6, 1, GL_FLOAT, 16 * Float.BYTES, 12 * Float.BYTES)
                .populateVao(7, 1, GL_FLOAT, 16 * Float.BYTES, 13 * Float.BYTES)
                .populateVao(8, 2, GL_FLOAT, 16 * Float.BYTES, 14 * Float.BYTES)
                .unbindVbo(0)
                .unbindVao();

        worldPipeline = new GLObject(GL_QUADS)
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .populateVbo(0, CommonShapes.cubeCoords, GL_STATIC_DRAW)                            // Cube positions 24 * vec3 (static)
                .populateVao(0, 3, GL_FLOAT, 0, 0)
                .bindVbo(1)
                .populateVao(1, 3, GL_FLOAT, 9 * Float.BYTES, 0)                // Offset 1 * vec3
                .populateVao(2, 4, GL_FLOAT, 9 * Float.BYTES, 3 * Float.BYTES)  // Color 1 * vec4
                .populateVao(3, 1, GL_FLOAT, 9 * Float.BYTES, 7 * Float.BYTES)  // Scale 1 * float
                .populateVao(4, 1, GL_FLOAT, 9 * Float.BYTES, 8 * Float.BYTES)  // Texture type 1 * float
                .divisor(1, 1)
                .divisor(2, 1)
                .divisor(3, 1)
                .divisor(4, 1)
                .populateVbo(2, UvCoords.getAllUvCoords(), GL_STATIC_DRAW)
                .populateVao(5, 2, GL_FLOAT, 0, 0)
                .populateVao(6, 2, GL_FLOAT, 0, 48 * Float.BYTES)
                .populateVao(7, 2, GL_FLOAT, 0, 48 * 2 * Float.BYTES)
                .populateVao(8, 2, GL_FLOAT, 0, 48 * 3 * Float.BYTES)
                .unbindVbo(2)
                .unbindVao();
    }

    // Run this if you expect any changes in vertex data for the gui
    public static void refreshGuiPipeline() {
        guiPipeline
                .populateVbo(0, list2Array(guiData), GL_STATIC_DRAW)
                .unbindVbo(0);
    }

    public static void refreshWorldPipeline() {
        worldPipeline
                .populateVbo(1, list2Array(worldData), GL_STREAM_DRAW)
                .unbindVbo(1);
    }

    public static void clearGuiPipeline() {
        guiShapes = 0;
        guiData.clear();
    }

    public static void clearWorldPipeline() {
        worldShapes = 0;
        worldData.clear();
    }

    public static void queueGuiData(float... data) {
        for (float f : data)
            guiData.add(f);
    }

    public static void queueWorldData(float... data) {
        for (float f : data)
            worldData.add(f);
    }

    private static float[] list2Array(List<Float> data) {
        float[] floats = new float[data.size()];
        for (int i = 0; i < floats.length; i++)
            floats[i] = data.get(i);
        return floats;
    }
}
