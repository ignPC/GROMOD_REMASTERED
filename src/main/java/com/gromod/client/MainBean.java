package com.gromod.client;

import com.gromod.client.fpstest.FpsTest;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import com.gromod.client.renderer.font.CustomFont;
import com.gromod.client.renderer.font.FontAtlasBuilder;
import com.gromod.client.settings.SaveSettings;
import lombok.SneakyThrows;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "Gromod", version = "Pre-Alpha")
public class MainBean {

    private CustomEntityLoader customEntityLoader;
    private static MainBean instance;

    @SneakyThrows
    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        FontAtlasBuilder.instance
                .addFont("Sweets Smile.ttf", 60, CustomFont.ALL, 0)
                .addFont("Inter-Bold.ttf", 30, CustomFont.ALL, 0)
                .addFont("Purple Smile.ttf", 60, CustomFont.ALL, 0)
                .buildAtlas();

        instance = this;
        customEntityLoader = new CustomEntityLoader();
        new Loader();
        new FpsTest();
        new SaveSettings();
    }

    public CustomEntityLoader getCustomEntityRenderer() {
        return customEntityLoader;
    }

    public static MainBean getInstance() {
        return instance;
    }

    /*
    TODO:
        - Modules:
            - cannoning
                - autotick
                - debugblock that works
                - inside of gametick tnt visualiser
            - fps
                - entity limit
            - schematica mods
                - faster printer somehow
                - replay printer
                - auto fit schematic
            - other
                - basebuilder
     */
}
