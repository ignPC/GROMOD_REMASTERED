package com.nut.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.lang.reflect.InvocationTargetException;

@Mod(modid = "Nut Client", version = "1.0.0")
public class MainBean {
    
    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        new Loader();
    }

    /*
    TODO:
        - gui framework
            - shaders
            - more shapes
        - modules annotation
            - module class automatically creates gui element
        - custom 3d renders
        - basebuilder
        - autotick
        - fps
            -entity limit
            -entity custom render
        - debugblock that works
        - schematica mods
            -faster printer somehow
            -replay printer
            -auto fit schematic

     */
}
