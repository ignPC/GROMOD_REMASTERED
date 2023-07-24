package com.gromod.client.utils;

import com.gromod.client.gui.TestGui;
import com.gromod.client.renderer.RenderPipeline;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderUtils {

    public static void drawImage(float x, float y, float width, float height, BColor color) {
        float xScale = TestGui.scaled.getXScale();
        float yScale = TestGui.scaled.getYScale();

        x *= xScale;
        y = (int) ((Display.getHeight() - y * yScale - height * yScale));
        width *= xScale;
        height *= yScale;

        RenderPipeline.queueGuiData(
                x, y,
                color.r, color.g, color.b, color.a,
                width, height,
                0, 0, 0, ShapeType.IMAGE.ordinal(),
                0, 0, 1, 1
        );
        RenderPipeline.guiShapes++;
    }

    public static void drawCube(double x, double y, double z, float scaleX, float scaleY, float scaleZ, BColor color, TextureType textureType) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        if (textureType.ordinal() != 4)
            RenderPipeline.queueWorldData(
                    (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                    color.r, color.g, color.b, color.a,
                    scaleX, scaleY, scaleZ, textureType.ordinal(),
                    textureType.texture[0].getMinU(), textureType.texture[0].getMinV(), textureType.texture[0].getMaxU() - textureType.texture[0].getMinU(), textureType.texture[0].getMaxV() - textureType.texture[0].getMinV(),
                    textureType.texture[1].getMinU(), textureType.texture[1].getMinV(), textureType.texture[1].getMaxU() - textureType.texture[1].getMinU(), textureType.texture[1].getMaxV() - textureType.texture[1].getMinV(),
                    textureType.texture[2].getMinU(), textureType.texture[2].getMinV(), textureType.texture[2].getMaxU() - textureType.texture[2].getMinU(), textureType.texture[2].getMaxV() - textureType.texture[2].getMinV()
            );
        else
            RenderPipeline.queueWorldData(
                    (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                    color.r, color.g, color.b, color.a,
                    scaleX, scaleY, scaleZ, textureType.ordinal(),
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
            );
        RenderPipeline.worldShapes++;
    }

    public static void drawCube(double x, double y, double z, float scaleX, float scaleY, float scaleZ, BColor color, TextureType textureType, boolean xray) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        if(!xray) {
            if (textureType.ordinal() != 4)
                RenderPipeline.queueWorldData(
                        (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                        color.r, color.g, color.b, color.a,
                        scaleX, scaleY, scaleZ, textureType.ordinal(),
                        textureType.texture[0].getMinU(), textureType.texture[0].getMinV(), textureType.texture[0].getMaxU() - textureType.texture[0].getMinU(), textureType.texture[0].getMaxV() - textureType.texture[0].getMinV(),
                        textureType.texture[1].getMinU(), textureType.texture[1].getMinV(), textureType.texture[1].getMaxU() - textureType.texture[1].getMinU(), textureType.texture[1].getMaxV() - textureType.texture[1].getMinV(),
                        textureType.texture[2].getMinU(), textureType.texture[2].getMinV(), textureType.texture[2].getMaxU() - textureType.texture[2].getMinU(), textureType.texture[2].getMaxV() - textureType.texture[2].getMinV()
                );
            else
                RenderPipeline.queueWorldData(
                        (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                        color.r, color.g, color.b, color.a,
                        scaleX, scaleY, scaleZ, textureType.ordinal(),
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0
                );
            RenderPipeline.worldShapes++;
        }
        else {
            if (textureType.ordinal() != 4)
                RenderPipeline.queueXrayData(
                        (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                        color.r, color.g, color.b, color.a,
                        scaleX, scaleY, scaleZ, textureType.ordinal(),
                        textureType.texture[0].getMinU(), textureType.texture[0].getMinV(), textureType.texture[0].getMaxU() - textureType.texture[0].getMinU(), textureType.texture[0].getMaxV() - textureType.texture[0].getMinV(),
                        textureType.texture[1].getMinU(), textureType.texture[1].getMinV(), textureType.texture[1].getMaxU() - textureType.texture[1].getMinU(), textureType.texture[1].getMaxV() - textureType.texture[1].getMinV(),
                        textureType.texture[2].getMinU(), textureType.texture[2].getMinV(), textureType.texture[2].getMaxU() - textureType.texture[2].getMinU(), textureType.texture[2].getMaxV() - textureType.texture[2].getMinV()
                );
            else
                RenderPipeline.queueXrayData(
                        (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                        color.r, color.g, color.b, color.a,
                        scaleX, scaleY, scaleZ, textureType.ordinal(),
                        0, 0, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 0, 0
                );
            RenderPipeline.xrayShapes++;
        }
    }

    public static void drawRoundedRectangle(float x, float y, float width, float height, float radius, float shade, BColor color) {
        float xScale = TestGui.scaled.getXScale();
        float yScale = TestGui.scaled.getYScale();

        x *= xScale;
        y = (int) ((Display.getHeight() - y * yScale - height * yScale));
        width *= xScale;
        height *= yScale;
        radius *= xScale;
        shade *= xScale;

        RenderPipeline.queueGuiData(
                x, y,
                color.r, color.g, color.b, color.a,
                width, height,
                radius, shade, 0, ShapeType.ROUNDEDRECTANGLE.ordinal(),
                0, 0, 0, 0
        );
        RenderPipeline.guiShapes++;
    }

    public static void drawCircle(float x, float y, float width, float height, float radius, float shade, BColor color) {
        float xScale = TestGui.scaled.getXScale();
        float yScale = TestGui.scaled.getYScale();

        x *= xScale;
        y = (int) ((Display.getHeight() - y * yScale - height * yScale));
        width *= xScale;
        height *= yScale;
        radius *= xScale;
        shade *= xScale;

        RenderPipeline.queueGuiData(
                x, y,
                color.r, color.g, color.b, color.a,
                width, height,
                radius, shade, 0, ShapeType.CIRCLE.ordinal(),
                0, 0, 0, 0
        );
        RenderPipeline.guiShapes++;
    }
}
