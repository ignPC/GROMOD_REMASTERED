package com.gromod.client.module.schematica;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

@Component
@GuiModule(name = "Replay Printer", category = GuiModule.Category.Schematica)
public class SchematicModule {
    @Getter
    private static SchematicModule instance;

    //@GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Replay Printer")
    private boolean replay = false;

    @GuiField(type = GuiField.Type.COMING_SOON, label = "Start Recording")
    private boolean recording = false;

    @GuiField(type = GuiField.Type.COMING_SOON, label = "Start Replaying")
    private boolean replaying = false;

    @AutoInit
    public SchematicModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }
}
