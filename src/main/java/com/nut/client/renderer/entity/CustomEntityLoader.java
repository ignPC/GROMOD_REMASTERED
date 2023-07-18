package com.nut.client.renderer.entity;

import net.minecraft.client.Minecraft;

public class CustomEntityLoader {

    private final CustomTntRenderer customTNT;
    private final CustomFallingBlockRenderer customFB;

    public CustomEntityLoader() {
        customTNT = new CustomTntRenderer(Minecraft.getMinecraft().getRenderManager());
        customFB = new CustomFallingBlockRenderer(Minecraft.getMinecraft().getRenderManager());
    }

    public void setRenderCustom(boolean renderCustom) {
        customTNT.setRenderCustom(renderCustom);
        customFB.setRenderCustom(renderCustom);
    }
}
