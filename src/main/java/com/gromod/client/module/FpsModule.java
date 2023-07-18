package com.gromod.client.module;

import com.gromod.client.MainBean;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import com.gromod.client.annotation.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
@GuiModule(name = "FPS")
public class FpsModule {

    private static FpsModule instance;

    @GuiField(type = GuiField.Type.BUTTON, label = "Custom Entity Render")
    private boolean customRender = true;

    private boolean customRendering = false;

    private final CustomEntityLoader customRenderer;

    @AutoInit
    public FpsModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        customRenderer = MainBean.getInstance().getCustomEntityRenderer();
        customRenderer.setRenderCustom(customRender);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (customRender && !customRendering){
            customRendering = true;
            customRenderer.setRenderCustom(true);
        } else if (!customRender && customRendering) {
            customRendering = false;
            customRenderer.setRenderCustom(false);
        }
    }

    public boolean isCustomRender() {
        return customRender;
    }

    public static FpsModule getInstance() {
        return instance;
    }
}
