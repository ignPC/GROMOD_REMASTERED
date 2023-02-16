package com.nut.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.lang.reflect.InvocationTargetException;

@Mod(modid = "Nut Client", version = "1.0.0")
public class MainNut {
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        new Loader();
    }
}
