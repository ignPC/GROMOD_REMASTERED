package com.nut.client.module;

import com.nut.client.MainBean;
import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.annotation.GuiField;
import com.nut.client.annotation.GuiModule;
import com.nut.client.renderer.entity.CustomEntityLoader;
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

    public static FpsModule getInstance() {
        return instance;
    }
}
