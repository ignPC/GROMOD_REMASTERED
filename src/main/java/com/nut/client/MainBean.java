package com.nut.client;

import com.nut.client.gui.guibuilder.MainGui;
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
     */
}
