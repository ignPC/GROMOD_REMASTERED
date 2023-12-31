package com.gromod.client.keybinds;

import com.gromod.client.gui.NewGui;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;


import java.util.ArrayList;
import java.util.List;

@Component
public class InputHandler {

    private static final KeyBinding KEY_BINDING_GUI = new KeyBinding("Click Gui", 56, "Gromod");
    private final NewGui newGui;

    @AutoInit
    public InputHandler(NewGui newGui) {
        MinecraftForge.EVENT_BUS.register(this);
        this.newGui = newGui;

        List<KeyBinding> bindingList = new ArrayList<>();

        bindingList.add(KEY_BINDING_GUI);

        for (KeyBinding key : bindingList) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent event) {
        if (NewGui.currentScreen == null) {
            if (KEY_BINDING_GUI.isPressed()) {
                newGui.openGui();
            }
        }
    }
}