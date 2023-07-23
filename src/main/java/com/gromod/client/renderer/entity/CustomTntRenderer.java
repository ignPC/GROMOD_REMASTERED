package com.gromod.client.renderer.entity;

import com.gromod.client.utils.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class CustomTntRenderer extends RenderTNTPrimed {

    private boolean renderCustom;
    private boolean xray;
    private boolean fullBright;
    private boolean patchingFPS;
    private int entitiesRendered;

    @Setter
    @Getter
    private ArrayList<Entity> renderEntityList = new ArrayList<>();

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
        // disable shadows
        renderManager.options.entityShadows = false;

        if(entitiesRendered > 2 && patchingFPS) return;

        if(!renderEntityList.contains(entity) && patchingFPS)
            renderEntityList.add(entity);

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

            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);

            if ((float)entity.fuse - partialTicks + 1.0F < 10.0F)
            {
                float f = 1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 10.0F;
                f = MathHelper.clamp_float(f, 0.0F, 1.0F);
                f = f * f;
                f = f * f;
                float f1 = 1.0F + f * 0.3F;
                GlStateManager.scale(f1, f1, f1);
            }

            float f2 = (1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
            this.bindEntityTexture(entity);
            GlStateManager.translate(-0.5F, -0.5F, 0.5F);
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
            GlStateManager.translate(0.0F, 0.0F, 1.0F);

            if (entity.fuse / 5 % 2 == 0)
            {
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 772);
                GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
                GlStateManager.doPolygonOffset(-3.0F, -3.0F);
                GlStateManager.enablePolygonOffset();
                blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
                GlStateManager.doPolygonOffset(0.0F, 0.0F);
                GlStateManager.disablePolygonOffset();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
            }

            GlStateManager.popMatrix();

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

