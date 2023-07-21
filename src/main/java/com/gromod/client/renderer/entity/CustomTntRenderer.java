package com.gromod.client.renderer.entity;

import com.gromod.client.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;

import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class CustomTntRenderer extends RenderTNTPrimed {

    private boolean renderCustom;
    private boolean xray;
    private boolean fullBright;
    private boolean patchingFPS;
    private int entitiesRendered;

    public CustomTntRenderer(RenderManager renderManager) {
        super(renderManager);
        this.renderCustom = true;
        this.xray = true;
        this.fullBright = true;
        this.entitiesRendered = 0;
        RenderingRegistry.registerEntityRenderingHandler(EntityTNTPrimed.class, this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void renderTickEvent(TickEvent.RenderTickEvent event){
        if(event.phase == TickEvent.Phase.START) {
            entitiesRendered = 0;
        }
    }

    @Override
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entitiesRendered > 2 && patchingFPS) return;
        entitiesRendered++;
        if (renderCustom) {
            float brightness = Minecraft.getMinecraft().theWorld.getSunBrightness(partialTicks) * 0.85f;

            if (fullBright) brightness = 1f;

            /*TODO:
                - IMPLEMENT FULLBRIGHT FOR NON CUSTOM RENDER
             */

            BVector3d interpolated = MathUtils.interpolateCoords(entity, partialTicks);
            RenderUtils.drawCube(interpolated.x - 0.5, interpolated.y, interpolated.z - 0.5, 0.98f, 0.98f, 0.98f, new BColor(brightness, brightness, brightness, 1), TextureType.TNT, xray);
        } else {
            // Use Minecraft's normal renderer
            if (xray) glDisable(GL_DEPTH_TEST);
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            if (xray) glEnable(GL_DEPTH_TEST);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity) {
        if (renderCustom) {
            return null;
        } else {
            return TextureMap.locationBlocksTexture;
        }
    }

    public void setRenderCustom(boolean renderCustom) {
        this.renderCustom = renderCustom;
    }

    public void setXray(boolean xray){
        this.xray = xray;
    }

    public void setFullBright(boolean fullBright) {
        this.fullBright = fullBright;
    }

    public void setPatchingFPS(boolean patchingFPS){
        this.patchingFPS = patchingFPS;
    }
}

