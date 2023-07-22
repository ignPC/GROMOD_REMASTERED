package com.gromod.client.module.patching;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.MessageUtils;
import com.gromod.client.utils.RenderUtils;
import com.gromod.client.utils.TextureType;
import lombok.Getter;
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

import java.util.HashSet;
import java.util.List;


@Component
@GuiModule(name = "Patchcrumbs", category = GuiModule.Category.Patching)
public class CrumbsModule {
    @Getter
    public static CrumbsModule instance;

    public Minecraft mc;
    public AxisAlignedBB box;

    public boolean renderX;
    public boolean renderZ;
    public long crumbTimeoutAfterEnd;
    public long crumbTimeoutShot;

    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Patchcrumbs")
    private boolean crumbs = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Cannon Detection")
    private boolean cannonDetection = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Auto Direction")
    private boolean autoDirection = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Callouts")
    private boolean callouts = false;


    @AutoInit
    public CrumbsModule() {
        MinecraftForge.EVENT_BUS.register(this);
        mc = Minecraft.getMinecraft();
        crumbTimeoutShot = System.currentTimeMillis();
        instance = this;
    }

    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        int timeout = 2900;
        int sandDetectionAmount = 2;
        //int tntDetectionAmount = 1;

        if (event.phase != TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null) return;

        if (!crumbs) {
            if (box != null)
                box = null;
            return;
        }

        if (box != null && System.currentTimeMillis() > crumbTimeoutAfterEnd) box = null;


        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (System.currentTimeMillis() < crumbTimeoutShot) return;
            if (!(entity instanceof EntityFallingBlock && entity.motionZ + entity.motionX == 0)) continue;

            AxisAlignedBB sandCheckRadius = entity.getEntityBoundingBox();
            //AxisAlignedBB tntCheckRadius = new AxisAlignedBB(entity.posX - 1, entity.posY - 1, entity.posZ - 1,entity.posX + 1, entity.posY + 1, entity.posZ + 1);

            List<EntityFallingBlock> sandList = mc.theWorld.getEntitiesWithinAABB(EntityFallingBlock.class, sandCheckRadius);
            //List<EntityTNTPrimed> tntList = mc.theWorld.getEntitiesWithinAABB(EntityTNTPrimed.class, tntCheckRadius);

            if (sandList.size() < sandDetectionAmount) continue;
            //if (tntList.size() < tntDetectionAmount) continue;

            // detects if cannon near, if so return
            if (cannonDetection) {
                for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(entity.posX - 8, entity.posY - 8, entity.posZ - 8), new BlockPos(entity.posX + 8, entity.posY + 8, entity.posZ + 8))) {
                    if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockDispenser)
                        return;
                }
            }

            renderX = true;
            renderZ = true;

            // check directions
            if (autoDirection) {
                BlockPos xPositive = new BlockPos(entity.posX + 1, entity.posY, entity.posZ);
                BlockPos xNegative = new BlockPos(entity.posX - 1, entity.posY, entity.posZ);
                BlockPos zPositive = new BlockPos(entity.posX, entity.posY, entity.posZ + 1);
                BlockPos zNegative = new BlockPos(entity.posX, entity.posY, entity.posZ - 1);

                if (
                        mc.theWorld.getBlockState(xPositive).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(xNegative).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(xPositive).getBlock() instanceof BlockSand
                        || mc.theWorld.getBlockState(xNegative).getBlock() instanceof BlockSand
                ) renderZ = false;

                else if (
                        mc.theWorld.getBlockState(zPositive).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(zNegative).getBlock() instanceof BlockObsidian
                        || mc.theWorld.getBlockState(zPositive).getBlock() instanceof BlockSand
                        || mc.theWorld.getBlockState(zNegative).getBlock() instanceof BlockSand
                ) renderX = false;
            }

            box = sandCheckRadius;

            // callouts
            if (callouts) {
                MessageUtils.addClientMessage("Patch Y: " + Math.ceil(box.minY));
            }

            // you will still see crumbs for 20 seconds after last shot
            crumbTimeoutAfterEnd = System.currentTimeMillis() + 20000L;
            crumbTimeoutShot = System.currentTimeMillis() + (long) timeout;
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(TickEvent.RenderTickEvent event){
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (box == null) return;

        BColor extensionColor = new BColor(1, 1, 1, 1);
        BColor boxColor = new BColor(1, 1, 1, 1);

        float lineThickness = 0.02f;
        float lineLength = 16 * 20;


        // Main box
        RenderUtils.drawCube(box.minX, box.minY, box.minZ, 1f, 1f, 1f, boxColor, TextureType.NONE, true);

        if (renderX) {
            // 4 lines along the x-axis
            RenderUtils.drawCube(box.minX - lineLength / 2, box.minY, box.minZ, lineLength, lineThickness, lineThickness, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX - lineLength / 2, box.minY, box.minZ + 1, lineLength, lineThickness, lineThickness, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX - lineLength / 2, box.minY + 1, box.minZ, lineLength, lineThickness, lineThickness, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX - lineLength / 2, box.minY + 1, box.minZ + 1, lineLength, lineThickness, lineThickness, extensionColor, TextureType.NONE, true);
        }

        if (renderZ) {
            // 4 lines along the x-axis
            RenderUtils.drawCube(box.minX, box.minY, box.minZ - lineLength / 2, lineThickness, lineThickness, lineLength, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX + 1, box.minY, box.minZ - lineLength / 2, lineThickness, lineThickness, lineLength, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX, box.minY + 1, box.minZ - lineLength / 2, lineThickness, lineThickness, lineLength, extensionColor, TextureType.NONE, true);
            RenderUtils.drawCube(box.minX + 1, box.minY + 1, box.minZ - lineLength / 2, lineThickness, lineThickness, lineLength, extensionColor, TextureType.NONE, true);
        }
    }

    public static CrumbsModule getInstance(){
        return instance;
    }
}
