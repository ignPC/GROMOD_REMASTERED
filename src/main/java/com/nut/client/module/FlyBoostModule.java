package com.nut.client.module;

import akka.io.Tcp;
import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
public class FlyBoostModule {
    public static FlyBoostModule instance;

    public Minecraft mc;
    public EntityPlayerSP player;
    private boolean toggleSprint = true;
    private boolean sprinting;
    private boolean toggleSneak = false;
    private boolean sneaking;
    private boolean flyBoost = true;

    private double flyBoostMultiplier = 2;
    private boolean init = true;

    private boolean sprintkeydown;

    @AutoInit
    public FlyBoostModule(){
        mc = Minecraft.getMinecraft();
        player = this.mc.thePlayer;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInput(InputEvent event) {
        if (this.toggleSprint && this.mc.gameSettings.keyBindSprint.isPressed() && !sprintkeydown) {
            this.sprinting = !this.sprinting;
            if(sprinting)
                MessageUtils.addClientMessage("SPRINT TRUE");
            else
                MessageUtils.addClientMessage("SPRINT FALSE");
        }
        if (this.toggleSneak && this.mc.gameSettings.keyBindSneak.isPressed()) {
            this.sneaking = !this.sneaking;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayerSP player = this.mc.thePlayer;

        if (init) {
            player.setSprinting(false);
            player.setSneaking(false);
            sprinting = false;
            sneaking = false;
            init = false;
        }

        if (this.toggleSprint) {
            if (!player.isSprinting() && this.sprinting) {
                player.setSprinting(true);
            } else if (player.isSprinting() && !this.sprinting){
                player.setSprinting(false);
            }
        }
        if (this.toggleSneak) {
            if (!player.isSneaking() && this.sneaking) {
                player.setSneaking(true);
            } else if (player.isSneaking() && !this.sneaking){
                player.setSneaking(false);
            }
        }
        if (player.capabilities.isCreativeMode && this.flyBoost && this.sprinting) {
            player.capabilities.setFlySpeed((float)(0.05D * this.flyBoostMultiplier));
            if (player.movementInput.jump && !player.movementInput.sneak && sprinting) {
                player.motionY += 0.009999999776482582D * this.flyBoostMultiplier;
            } else if (player.movementInput.sneak && !player.movementInput.jump && sneaking) {
                player.motionY -= 0.009999999776482582D * this.flyBoostMultiplier;
            }
        } else if (player.capabilities.isCreativeMode) {
            player.capabilities.setFlySpeed(0.05F);
        }
    }


    public static FlyBoostModule getInstance() {
        return instance;
    }
}
