package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.annotation.GuiField;
import com.nut.client.annotation.GuiModule;
import net.minecraftforge.common.MinecraftForge;

@Component
@GuiModule(name = "SCHEMATICA")
public class SchematicModule {

    private static SchematicModule instance;

    @GuiField(type = GuiField.Type.BUTTON, label = "Record Print")
    private boolean recording = false;

    @GuiField(type = GuiField.Type.BUTTON, label = "Replay Print")
    private boolean replaying = false;

    @AutoInit
    public SchematicModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public static SchematicModule getInstance() {
        return instance;
    }
}
