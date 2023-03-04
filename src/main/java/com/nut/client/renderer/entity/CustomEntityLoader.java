package com.nut.client.renderer.entity;

import net.minecraft.client.Minecraft;

public class CustomEntityLoader {

    public CustomEntityLoader() {
        new CustomTntRenderer(Minecraft.getMinecraft().getRenderManager());
        new CustomFallingBlockRenderer(Minecraft.getMinecraft().getRenderManager());
    }
}
