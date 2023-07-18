package com.nut.client;

import com.nut.client.renderer.entity.CustomEntityLoader;
import com.nut.client.renderer.font.CustomFont;
import com.nut.client.renderer.font.FontAtlasBuilder;
import lombok.SneakyThrows;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "Nut Client", version = "1.0.0")
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
            - patching
                - tnt sand xray
                - patching fps optimizations
                - crumbs with good code
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
