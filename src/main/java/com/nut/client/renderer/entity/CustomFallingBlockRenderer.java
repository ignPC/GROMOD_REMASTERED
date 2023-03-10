package com.nut.client.renderer.entity;

import com.nut.client.utils.*;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class CustomFallingBlockRenderer extends Render<EntityFallingBlock> {

    protected CustomFallingBlockRenderer(RenderManager renderManager) {
        super(renderManager);
        RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, this);
    }

    @Override
    public void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Block block = entity.getBlock().getBlock();
        if (!(block instanceof BlockSand) && !(block instanceof BlockGravel)) {
            if (entity.getBlock() != null)
            {
                this.bindTexture(TextureMap.locationBlocksTexture);
                IBlockState iblockstate = entity.getBlock();
                BlockPos blockpos = new BlockPos(entity);
                World world = entity.getWorldObj();

                if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1)
                {
                    if (block.getRenderType() == 3)
                    {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float)x, (float)y, (float)z);
                        GlStateManager.disableLighting();
                        Tessellator tessellator = Tessellator.getInstance();
                        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                        int i = blockpos.getX();
                        int j = blockpos.getY();
                        int k = blockpos.getZ();
                        worldrenderer.setTranslation((double)((float)(-i) - 0.5F), (double)(-j), (double)((float)(-k) - 0.5F));
                        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                        IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, (BlockPos)null);
                        blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                        tessellator.draw();
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
            Vector3d interpolated = MathUtils.interpolateCoords(entity, partialTicks);
            RenderUtils.drawCube(interpolated.x - 0.5, interpolated.y, interpolated.z - 0.5, 0.98f, 0.98f, 0.98f, new Color(brightness, brightness, brightness, 1), textureType);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
        return null;
    }
}
