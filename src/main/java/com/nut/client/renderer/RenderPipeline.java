package com.nut.client.renderer;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.AfterScreenCreationEvent;
import com.nut.client.event.GuiRenderEvent;
import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.renderer.font.CustomFont;
import com.nut.client.renderer.font.FontRenderer;
import com.nut.client.renderer.util.ProjectionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;

@Component
public class RenderPipeline {

    public static FontRenderer fontRenderer;
    public static int shapes;
    private static GLObject pipeline;
    private static final List<Float> dataList = new ArrayList<>();

    private Shader shader;

    @AutoInit
    public RenderPipeline() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiRender(GuiRenderEvent event) {
        if (BaseGui.currentScreen == null) return;

        glUseProgram(shader.getShaderProgram());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, fontRenderer.textureId);
        glUniformMatrix4(shader.getUniform("projection"), false, ProjectionUtils.orthoProjection);
        glUniform1i(shader.getUniform("font_atlas"), 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        pipeline.render(0, shapes * 4);
        pipeline.unbindVao();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onAfterScreenCreation(AfterScreenCreationEvent event) {
        fontRenderer = new FontRenderer(
                new CustomFont(
                        "Sweets Smile.ttf",
                        60,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6),
                new CustomFont(
                        "Inter-Bold.ttf",
                        30,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6
                ),
                new CustomFont(
                        "Inter-ExtraBold.ttf",
                        24,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6
                ),
                new CustomFont(
                        "Inter-Thin.ttf",
                        12,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6
                ),
                new CustomFont(
                        "Inter-Black.ttf",
                        60,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6
                ),
                new CustomFont(
                        "Inter-Light.ttf",
                        30,
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\",
                        6
                )
        );

        shader = new Shader(
                new ResourceLocation("bean", "shaders/guiVS.glsl"),
                new ResourceLocation("bean", "shaders/guiFS.glsl"),
                "projection",
                "font_atlas"
        );

        pipeline = new GLObject(GL_QUADS);
        pipeline
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
    }

    // Run this if you expect any changes in vertex data
    public static void refreshPipeline() {
        pipeline
                .populateVbo(0, list2Array(dataList), GL_STATIC_DRAW)
                .unbindVbo(0);
    }

    public static void queueData(float... data) {
        for (float f : data)
            dataList.add(f);
    }

    private static float[] list2Array(List<Float> data) {
        float[] floats = new float[data.size()];
        for (int i = 0; i < floats.length; i++)
            floats[i] = data.get(i);
        return floats;
    }

    public static void clearPipeline() {
        shapes = 0;
        dataList.clear();
    }
}
