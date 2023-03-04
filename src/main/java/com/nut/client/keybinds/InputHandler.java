package com.nut.client.keybinds;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.guibuilder.BaseGui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

@Component
public class InputHandler {

    private static final KeyBinding KEY_BINDING_GUI = new KeyBinding("Click Gui", 56, "Bean Client");
    private final BaseGui baseGui;

    @AutoInit
    public InputHandler(BaseGui baseGui) {
        MinecraftForge.EVENT_BUS.register(this);
        this.baseGui = baseGui;

        List<KeyBinding> bindingList = new ArrayList<>();

        bindingList.add(KEY_BINDING_GUI);

        for (KeyBinding key : bindingList) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent event) {
        if (BaseGui.currentScreen == null) {
            if (KEY_BINDING_GUI.isPressed()) {
                baseGui.openGui();
            }
        }
    }
}