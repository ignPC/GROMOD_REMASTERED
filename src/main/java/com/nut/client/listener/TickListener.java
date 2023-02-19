package com.nut.client.listener;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.BaseGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Component
public class TickListener {

    private final BaseGui baseGui;

    @AutoInit
    public TickListener(BaseGui baseGui) {
        this.baseGui = baseGui;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Keyboard.getEventKeyState()) {
            int keyCode = Keyboard.getEventKey();
            if (BaseGui.currentScreen == null) {
                if (keyCode == 56) {
                    baseGui.openGui();
                }
            } else {
                BaseGui.currentScreen.keyboardInput(keyCode);
            }
        }
        if (BaseGui.currentScreen != null)
            BaseGui.currentScreen.mouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
    }
}
