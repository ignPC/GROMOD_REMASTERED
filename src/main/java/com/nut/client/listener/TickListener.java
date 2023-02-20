package com.nut.client.listener;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.guibuilder.BaseGui;
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
        if(BaseGui.currentScreen == null) return;

        BaseGui.currentScreen.mouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());

        if (!Keyboard.getEventKeyState()) return;

        int keyCode = Keyboard.getEventKey();
        BaseGui.currentScreen.keyboardInput(keyCode);
    }
}
