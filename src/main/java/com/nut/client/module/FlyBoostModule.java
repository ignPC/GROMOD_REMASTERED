package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.GuiModule;
import com.nut.client.annotation.Component;
import com.nut.client.annotation.GuiField;
import com.nut.client.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.swing.text.JTextComponent;

@Component
@GuiModule(name = "Flyboost")
public class FlyBoostModule {
    public static FlyBoostModule instance;

    public Minecraft mc;

    @GuiField(type = GuiField.Type.BUTTON, label = "Toggle Sprint")
    private boolean toggleSprint = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "FlyBoost")
    private boolean flyBoost = true;

    private boolean sprinting = false;

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

        if (toggleSprint) {
            if (mc.gameSettings.keyBindSprint.isPressed()) sprinting = !sprinting;
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), sprinting);
        }

        if (mc.thePlayer.capabilities.isCreativeMode && sprinting && flyBoost) {
            mc.thePlayer.capabilities.setFlySpeed((float)(0.05D * this.flyBoostMultiplier));
            if (mc.thePlayer.movementInput.jump && !mc.thePlayer.movementInput.sneak && sprinting && mc.thePlayer.capabilities.isFlying) {
                mc.thePlayer.motionY += 0.009999999776482582D * this.flyBoostMultiplier;
            } else if (mc.thePlayer.movementInput.sneak && !mc.thePlayer.movementInput.jump && mc.thePlayer.capabilities.isFlying) {
                mc.thePlayer.motionY -= 0.009999999776482582D * this.flyBoostMultiplier;
            }
        } else if (mc.thePlayer.capabilities.isCreativeMode) {
            mc.thePlayer.capabilities.setFlySpeed(0.05F);
        }
    }

    public static FlyBoostModule getInstance() {
        return instance;
    }
}
