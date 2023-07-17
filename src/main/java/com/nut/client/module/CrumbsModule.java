package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.annotation.GuiField;
import com.nut.client.annotation.GuiModule;
import com.nut.client.utils.MessageUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;


@Component
@GuiModule(name = "Crumbs")
public class CrumbsModule {
    public static CrumbsModule instance;

    public Minecraft mc = Minecraft.getMinecraft();
    public AxisAlignedBB Box;

    public boolean RenderX;
    public boolean RenderZ;
    public long crumbTimeoutAfterEnd;
    public long crumbTimeoutShot;

    @GuiField(type = GuiField.Type.BUTTON, label = "Crumbs")
    private boolean crumbs = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Cannon Detection")
    private boolean cannonDetection = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Auto Direction")
    private boolean autoDirection = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Callouts")
    private boolean callouts = true;

    private int timeout = 1000;
    private int sandDetectionAmount = 1;


    @AutoInit
    public CrumbsModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null) return;

        if (!crumbs) {
            if (Box != null)
                Box = null;
            return;
        }

        if (Box != null && System.currentTimeMillis() > crumbTimeoutAfterEnd) Box = null;

        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (!(System.currentTimeMillis() > crumbTimeoutShot)) return;
            if (!(entity instanceof EntityFallingBlock && entity.motionZ + entity.motionX == 0)) return;

            AxisAlignedBB SandCheckRadius = entity.getEntityBoundingBox();
            List<EntityFallingBlock> SandList = mc.theWorld.getEntitiesWithinAABB(EntityFallingBlock.class, SandCheckRadius);

            if (SandList.size() < sandDetectionAmount) return;

            // detects if cannon near, if so return
            if (cannonDetection) {
                for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(entity.posX - 8, entity.posY - 8, entity.posZ - 8), new BlockPos(entity.posX + 8, entity.posY + 8, entity.posZ + 8))) {
                    if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockDispenser)
                        return;
                }
            }

            // check directions
            if (autoDirection) {
                RenderX = true;
                RenderZ = true;
                BlockPos xPositive = new BlockPos(entity.posX + 1, entity.posY, entity.posZ);
                BlockPos xNegative = new BlockPos(entity.posX - 1, entity.posY, entity.posZ);
                BlockPos zPositive = new BlockPos(entity.posX, entity.posY, entity.posZ + 1);
                BlockPos zNegative = new BlockPos(entity.posX, entity.posY, entity.posZ - 1);

                if (
                        mc.theWorld.getBlockState(xPositive).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(xNegative).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(xPositive).getBlock() instanceof BlockSand
                        || mc.theWorld.getBlockState(xNegative).getBlock() instanceof BlockSand
                ) RenderX = false;

                else if (
                        mc.theWorld.getBlockState(zPositive).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(zNegative).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(zPositive).getBlock() instanceof BlockSand
                        || mc.theWorld.getBlockState(zNegative).getBlock() instanceof BlockSand
                ) RenderZ = false;
            }

            Box = SandCheckRadius;

            // callouts
            if (callouts) {
                MessageUtils.addClientMessage("Patch Y: " + Math.ceil(Box.minY));
            }

            // you will still see crumbs for 20 seconds after last shot
            crumbTimeoutAfterEnd = System.currentTimeMillis() + 20000L;
            crumbTimeoutShot = System.currentTimeMillis() + (long) timeout;
        }
    }

    public static CrumbsModule getInstance(){
        return instance;
    }
}
