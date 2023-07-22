package com.gromod.client.module.other;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
@GuiModule(name = "Toggle Sprint", category = GuiModule.Category.Other)
public class ToggleSprintModule {
    @Getter
    public static ToggleSprintModule instance;

    public Minecraft mc;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Toggle Sprint")
    private boolean toggleSprint = true;

    private boolean sprinting = false;

    @AutoInit
    public ToggleSprintModule(){
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
    }
}

