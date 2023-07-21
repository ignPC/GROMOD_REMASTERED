package com.gromod.client.renderer;

import com.gromod.client.event.AfterScreenCreationEvent;
import com.gromod.client.event.GuiRenderEvent;
import com.gromod.client.gui.TestGui;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.renderer.font.FontAtlasBuilder;
import com.gromod.client.renderer.util.CommonShapes;
import com.gromod.client.renderer.util.ProjectionUtils;
import com.gromod.client.utils.FpsTimer;
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
    public static int xrayShapes;
    private static GLObject guiPipeline;
    private static GLObject worldPipeline;
    private static GLObject xrayPipeline;
    private static final List<Float> guiData = new ArrayList<>();
    private static final List<Float> worldData = new ArrayList<>();
    private static final List<Float> xrayData = new ArrayList<>();

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
        if (TestGui.currentScreen == null) return;
        if (guiShapes == 0) return;

        glUseProgram(guiShader.getShaderProgram());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, FontAtlasBuilder.textureId);
        glUniformMatrix4(guiShader.getUniform("projection"), false, ProjectionUtils.guiProjection);
        glUniform1i(guiShader.getUniform("font_atlas"), 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (!FpsTimer.skipRender)
            refreshGuiPipeline();

        guiPipeline.renderInstanced(0, 4, guiShapes);
        guiPipeline.unbindVao();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (minecraft.thePlayer == null || minecraft.theWorld == null) return;
        if (worldShapes == 0 && xrayShapes == 0) return;

        glUseProgram(worldShader.getShaderProgram());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 8);
        glUniformMatrix4(worldShader.getUniform("projection"), false, ProjectionUtils.worldProjection);
        glUniform1i(worldShader.getUniform("minecraft_atlas"), 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (worldShapes != 0) {
            refreshWorldPipeline();

            worldPipeline.renderInstanced(0, 24, worldShapes);
            worldPipeline.unbindVao();

            clearWorldPipeline();
        }

        if (xrayShapes != 0) {
            refreshXrayPipeline();

            glDisable(GL_DEPTH_TEST);
            xrayPipeline.renderInstanced(0, 24, xrayShapes);
            glEnable(GL_DEPTH_TEST);

            xrayPipeline.unbindVao();

            clearXrayPipeline();
        }

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
                .addVbo(GL_ARRAY_BUFFER)
                .populateVbo(0, CommonShapes.rectangle(0, 0, 1, 1, 1), GL_STATIC_DRAW)
                .populateVao(0, 2, GL_FLOAT, 0, 0)
                .bindVbo(1)
                .populateVao(1, 2, GL_FLOAT, 16 * Float.BYTES, 0)
                .populateVao(2, 4, GL_FLOAT, 16 * Float.BYTES, 2 * Float.BYTES)
                .populateVao(3, 2, GL_FLOAT, 16 * Float.BYTES, 6 * Float.BYTES)
                .populateVao(4, 4, GL_FLOAT, 16 * Float.BYTES, 8 * Float.BYTES)
                .populateVao(5, 4, GL_FLOAT, 16 * Float.BYTES, 12 * Float.BYTES)
                .divisor(1, 1)
                .divisor(2, 1)
                .divisor(3, 1)
                .divisor(4, 1)
                .divisor(5, 1)
                .unbindVbo(1)
                .unbindVao();

        worldPipeline = new GLObject(GL_QUADS)
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .populateVbo(0, CommonShapes.cube(0, 0, 0, 1, 1, 1), GL_STATIC_DRAW)                            // Cube positions 24 * vec3 (static)
                .populateVao(0, 3, GL_FLOAT, 0, 0)
                .populateVbo(1, CommonShapes.rectangle(0, 0, 1, 1, 6), GL_STATIC_DRAW)
                .populateVao(1, 2, GL_FLOAT, 0, 0)
                .bindVbo(2)
                .populateVao(2, 3, GL_FLOAT, 23 * Float.BYTES, 0)
                .populateVao(3, 4, GL_FLOAT, 23 * Float.BYTES, 3 * Float.BYTES)
                .populateVao(4, 4, GL_FLOAT, 23 * Float.BYTES, 7 * Float.BYTES)
                .populateVao(5, 4, GL_FLOAT, 23 * Float.BYTES, 11 * Float.BYTES)
                .populateVao(6, 4, GL_FLOAT, 23 * Float.BYTES, 15 * Float.BYTES)
                .populateVao(7, 4, GL_FLOAT, 23 * Float.BYTES, 19 * Float.BYTES)
                .divisor(2, 1)
                .divisor(3, 1)
                .divisor(4, 1)
                .divisor(5, 1)
                .divisor(6, 1)
                .divisor(7, 1)
                .unbindVbo(2)
                .unbindVao();

        xrayPipeline = new GLObject(GL_QUADS)
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .addVbo(GL_ARRAY_BUFFER)
                .populateVbo(0, CommonShapes.cube(0, 0, 0, 1, 1, 1), GL_STATIC_DRAW)                            // Cube positions 24 * vec3 (static)
                .populateVao(0, 3, GL_FLOAT, 0, 0)
                .populateVbo(1, CommonShapes.rectangle(0, 0, 1, 1, 6), GL_STATIC_DRAW)
                .populateVao(1, 2, GL_FLOAT, 0, 0)
                .bindVbo(2)
                .populateVao(2, 3, GL_FLOAT, 23 * Float.BYTES, 0)
                .populateVao(3, 4, GL_FLOAT, 23 * Float.BYTES, 3 * Float.BYTES)
                .populateVao(4, 4, GL_FLOAT, 23 * Float.BYTES, 7 * Float.BYTES)
                .populateVao(5, 4, GL_FLOAT, 23 * Float.BYTES, 11 * Float.BYTES)
                .populateVao(6, 4, GL_FLOAT, 23 * Float.BYTES, 15 * Float.BYTES)
                .populateVao(7, 4, GL_FLOAT, 23 * Float.BYTES, 19 * Float.BYTES)
                .divisor(2, 1)
                .divisor(3, 1)
                .divisor(4, 1)
                .divisor(5, 1)
                .divisor(6, 1)
                .divisor(7, 1)
                .unbindVbo(2)
                .unbindVao();
    }

    public static void refreshGuiPipeline() {
        guiPipeline
                .populateVbo(1, list2Array(guiData), GL_STREAM_DRAW)
                .unbindVbo(1);
    }

    public static void refreshWorldPipeline() {
        worldPipeline
                .populateVbo(2, list2Array(worldData), GL_STREAM_DRAW)
                .unbindVbo(2);
    }

    public static void refreshXrayPipeline() {
        xrayPipeline
                .populateVbo(2, list2Array(xrayData), GL_STREAM_DRAW)
                .unbindVbo(2);
    }

    public static void clearGuiPipeline() {
        guiShapes = 0;
        guiData.clear();
    }

    public static void clearWorldPipeline() {
        worldShapes = 0;
        worldData.clear();
    }

    public static void clearXrayPipeline() {
        xrayShapes = 0;
        xrayData.clear();
    }

    public static void queueGuiData(float... data) {
        for (float f : data)
            guiData.add(f);
    }

    public static void queueWorldData(float... data) {
        for (float f : data)
            worldData.add(f);
    }

    public static void queueXrayData(float... data) {
        for (float f : data)
            xrayData.add(f);
    }

    private static float[] list2Array(List<Float> data) {
        float[] floats = new float[data.size()];
        for (int i = 0; i < floats.length; i++)
            floats[i] = data.get(i);
        return floats;
    }
}
