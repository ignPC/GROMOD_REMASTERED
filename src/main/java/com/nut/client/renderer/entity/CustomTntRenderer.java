package com.nut.client.renderer.entity;

import com.nut.client.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;

import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomTntRenderer extends RenderTNTPrimed {

    private boolean renderCustom;

    public CustomTntRenderer(RenderManager renderManager) {
        super(renderManager);
        this.renderCustom = true;
        RenderingRegistry.registerEntityRenderingHandler(EntityTNTPrimed.class, this);
    }

    @Override
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (renderCustom) {
            // Perform custom rendering logic
            float brightness = Minecraft.getMinecraft().theWorld.getSunBrightness(partialTicks) * 0.85f;
            BVector3d interpolated = MathUtils.interpolateCoords(entity, partialTicks);
            RenderUtils.drawCube(interpolated.x - 0.5, interpolated.y, interpolated.z - 0.5, 0.98f, 0.98f, 0.98f, new BColor(brightness, brightness, brightness, 1), TextureType.TNT);
        } else {
            // Use Minecraft's normal renderer
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity) {
        return null;
    }

    public void setRenderCustom(boolean renderCustom) {
        this.renderCustom = renderCustom;
    }
}

