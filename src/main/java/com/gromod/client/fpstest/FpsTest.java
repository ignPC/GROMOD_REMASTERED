package com.gromod.client.fpstest;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class FpsTest {

    public FpsTest(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack stack = event.entityPlayer.getHeldItem();
            if (stack.stackSize != 0) {
                // Check if the item is TNT (replace "YourTNTItem" with the actual class of your TNT item)
                if (stack.getItem() instanceof ItemAxe) {
                    if (!event.world.isRemote) {
                        // Create and spawn the TNT entity
                        for (int i = 0; i < 100; i++) {
                            EntityTNTPrimed tnt = new EntityTNTPrimed(event.world, event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, event.entityPlayer);
                            event.world.spawnEntityInWorld(tnt);
                        }
                    }
                }
            }
        }
    }
}
