package com.nut.client;

import com.nut.client.renderer.entity.CustomEntityLoader;
import com.nut.client.renderer.font.CustomFont;
import com.nut.client.renderer.font.FontAtlasBuilder;
import lombok.SneakyThrows;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "Nut Client", version = "1.0.0")
public class MainBean {
    
    @SneakyThrows
    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        FontAtlasBuilder.instance
                .addFont("Sweets Smile.ttf", 60, CustomFont.ALL, 0)
                .addFont("Inter-Bold.ttf", 30, CustomFont.ALL, 0)
                .addFont("Purple Smile.ttf", 60, CustomFont.ALL, 0)
                .buildAtlas();
        new Loader();
        new CustomEntityLoader();
    }

    /*
    TODO:
        - gui framework
            - shaders
            - more shapes
            - make it nice
        - modules annotation
            - module class automatically creates gui element
        - custom 3d renders

    TODO:
        - Modules:
            - patching
                - tnt sand xray
                - patching fps optimizations
                - crumbs with good code
            - cannoning
                - autotick
                - debugblock that works
            - fps
                - entity limit
                - entity custom render
            - schematica mods
                - faster printer somehow
                - replay printer
                - auto fit schematic
            - other
                - basebuilder
     */
}
