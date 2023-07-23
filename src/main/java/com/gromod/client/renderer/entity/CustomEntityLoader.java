package com.gromod.client.renderer.entity;

import lombok.Getter;
import net.minecraft.client.Minecraft;

public class CustomEntityLoader {

    @Getter
    private final CustomTntRenderer customTNT;
    @Getter
    private final CustomFallingBlockRenderer customFB;

    public CustomEntityLoader() {
        customTNT = new CustomTntRenderer(Minecraft.getMinecraft().getRenderManager());
        customFB = new CustomFallingBlockRenderer(Minecraft.getMinecraft().getRenderManager());
    }

    public void setRenderCustom(boolean renderCustom) {
        customTNT.setRenderCustom(renderCustom);
        customFB.setRenderCustom(renderCustom);
    }

    public void setXray(boolean xray){
        customTNT.setXray(xray);
        customFB.setXray(xray);
    }

    public void setFullBright(boolean fullBright){
        customTNT.setFullBright(fullBright);
        customFB.setFullBright(fullBright);
    }

    public void setPatchingFPS(boolean patchingFPS){
        customTNT.setPatchingFPS(patchingFPS);
        customFB.setPatchingFPS(patchingFPS);
    }
}
