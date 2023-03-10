package com.nut.client.renderer.entity;

import com.nut.client.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class CustomTntRenderer extends Render<EntityTNTPrimed> {

    protected CustomTntRenderer(RenderManager renderManager) {
        super(renderManager);
        RenderingRegistry.registerEntityRenderingHandler(EntityTNTPrimed.class, this);
    }

    @Override
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float brightness = Minecraft.getMinecraft().theWorld.getSunBrightness(partialTicks) * 0.85f;
        Vector3d interpolated = MathUtils.interpolateCoords(entity, partialTicks);
        RenderUtils.drawCube(interpolated.x - 0.5, interpolated.y, interpolated.z - 0.5, 0.98f, 0.98f, 0.98f, new Color(brightness, brightness, brightness, 1), TextureType.TNT);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity) {
        return null;
    }
}
