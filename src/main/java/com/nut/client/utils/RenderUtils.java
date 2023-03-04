package com.nut.client.utils;

import com.nut.client.renderer.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderUtils {

    public static void drawCube(double x, double y, double z, float scale, Color color, TextureType textureType, float partialTicks) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        RenderPipeline.queueWorldData(
                (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                color.r, color.g, color.b, color.a,
                scale,
                textureType.ordinal()
        );
        RenderPipeline.worldShapes++;
    }
}
