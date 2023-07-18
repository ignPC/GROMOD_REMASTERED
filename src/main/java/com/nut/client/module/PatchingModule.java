package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.annotation.GuiField;
import com.nut.client.annotation.GuiModule;
import net.minecraftforge.common.MinecraftForge;

@Component
@GuiModule(name = "PATCHING")
public class PatchingModule {
    private static PatchingModule instance;

    @GuiField(type = GuiField.Type.BUTTON, label = "Entity Xray")
    private boolean entityXray = false;

    @GuiField(type = GuiField.Type.BUTTON, label = "FullBright")
    private boolean fullBright = false;

    @GuiField(type = GuiField.Type.BUTTON, label = "Patching FPS")
    private boolean patchingFps = false;

    @AutoInit
    public PatchingModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public static PatchingModule getInstance() {
        return instance;
    }
}
