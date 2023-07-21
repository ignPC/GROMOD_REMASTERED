package com.gromod.client.module.other;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@Component
@GuiModule(name = "Flyboost", category = GuiModule.Category.Other, index = 1)
public class FlyBoostModule {
    @Getter
    public static FlyBoostModule instance;

    public Minecraft mc;

    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "FlyBoost")
    private boolean flyBoost = false;

    private final double flyBoostMultiplier = 2;

    @AutoInit
    public FlyBoostModule(){
        instance = this;
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;


        if (mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.isSprinting() && flyBoost) {
            mc.thePlayer.capabilities.setFlySpeed((float)(0.05D * this.flyBoostMultiplier));
            if (mc.thePlayer.movementInput.jump && !mc.thePlayer.movementInput.sneak && mc.thePlayer.isSprinting() && mc.thePlayer.capabilities.isFlying) {
                mc.thePlayer.motionY += 0.009999999776482582D * this.flyBoostMultiplier;
            } else if (mc.thePlayer.movementInput.sneak && !mc.thePlayer.movementInput.jump && mc.thePlayer.capabilities.isFlying) {
                mc.thePlayer.motionY -= 0.009999999776482582D * this.flyBoostMultiplier;
            }
        } else if (mc.thePlayer.capabilities.isCreativeMode) {
            mc.thePlayer.capabilities.setFlySpeed(0.05F);
        }
    }
}
