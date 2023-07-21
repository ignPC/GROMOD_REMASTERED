package com.gromod.client.renderer.entity;

import com.gromod.client.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class CustomFallingBlockRenderer extends Render<EntityFallingBlock> {

    private boolean renderCustom;
    private boolean xray;
    private boolean fullBright;
    private boolean patchingFPS;
    private int entitiesRendered;

    protected CustomFallingBlockRenderer(RenderManager renderManager) {
        super(renderManager);
        this.renderCustom = true;
        this.xray = true;
        this.fullBright = true;
        this.entitiesRendered = 0;
        RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void renderTickEvent(TickEvent.RenderTickEvent event){
        if(event.phase == TickEvent.Phase.START) {
            entitiesRendered = 0;
        }
    }

    @Override
    public void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entitiesRendered > 2 && patchingFPS) return;
        entitiesRendered++;
        if (renderCustom) {
            Block block = entity.getBlock().getBlock();
            if (!(block instanceof BlockSand) && !(block instanceof BlockGravel)) {
                if (entity.getBlock() != null) {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                    IBlockState iblockstate = entity.getBlock();
                    BlockPos blockpos = new BlockPos(entity);
                    World world = entity.getWorldObj();

                    if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1) {
                        if (block.getRenderType() == 3) {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate((float) x, (float) y, (float) z);
                            GlStateManager.disableLighting();
                            WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
                            worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                            int i = blockpos.getX();
                            int j = blockpos.getY();
                            int k = blockpos.getZ();
                            worldrenderer.setTranslation((double) ((float) (-i) - 0.5F), (double) (-j), (double) ((float) (-k) - 0.5F));
                            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                            IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, (BlockPos) null);
                            blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                            Tessellator.getInstance().draw();
                            GlStateManager.enableLighting();
                            GlStateManager.popMatrix();
                            super.doRender(entity, x, y, z, entityYaw, partialTicks);
                        }
                    }
                }
            } else {
                TextureType textureType = null;
                if (block instanceof BlockGravel)
                    textureType = TextureType.GRAVEL;
                else {
                    BlockSand.EnumType blockType = entity.getBlock().getValue(BlockSand.VARIANT);
                    if (blockType.equals(BlockSand.EnumType.SAND))
                        textureType = TextureType.SAND;
                    else if (blockType.equals(BlockSand.EnumType.RED_SAND))
                        textureType = TextureType.REDSAND;
                }

                float brightness = Minecraft.getMinecraft().theWorld.getSunBrightness(partialTicks) * 0.85f;

                if (fullBright) brightness = 1f;

                BVector3d interpolated = MathUtils.interpolateCoords(entity, partialTicks);
                RenderUtils.drawCube(interpolated.x - 0.5, interpolated.y, interpolated.z - 0.5, 0.98f, 0.98f, 0.98f, new BColor(brightness, brightness, brightness, 1), textureType, xray);
            }
        } else {
            if (entity.getBlock() != null) {
                Block block = entity.getBlock().getBlock();
                this.bindTexture(TextureMap.locationBlocksTexture);
                IBlockState iblockstate = entity.getBlock();
                BlockPos blockpos = new BlockPos(entity);
                World world = entity.getWorldObj();

                if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1) {
                    if (block.getRenderType() == 3) {

                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float) x, (float) y, (float) z);
                        GlStateManager.disableLighting();
                        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
                        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                        int i = blockpos.getX();
                        int j = blockpos.getY();
                        int k = blockpos.getZ();
                        worldrenderer.setTranslation((double) ((float) (-i) - 0.5F), (double) (-j), (double) ((float) (-k) - 0.5F));

                        if (xray) {
                            // Disable depth testing to render behind other blocks
                            GlStateManager.disableDepth();
                            glEnable(GL_ALPHA_TEST); // Enable alpha testing to handle transparent blocks like water
                            glAlphaFunc(GL_GREATER, 0.1f); // Set the alpha threshold for rendering transparent blocks
                        }

                        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                        IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, (BlockPos) null);
                        blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                        Tessellator.getInstance().draw();
                        GlStateManager.enableLighting();
                        GlStateManager.popMatrix();
                        super.doRender(entity, x, y, z, entityYaw, partialTicks);

                        if (xray) {
                            // Re-enable depth testing after rendering the TNT block
                            GlStateManager.enableDepth();
                            glDisable(GL_ALPHA_TEST); // Disable alpha testing
                        }
                    }
                }
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
        if (renderCustom) {
            return null;
        } else {
            return TextureMap.locationBlocksTexture;
        }
    }

    public void setRenderCustom(boolean customRenderer) {
        this.renderCustom = customRenderer;
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

