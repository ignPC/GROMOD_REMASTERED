package com.gromod.client.module.fps;

import com.gromod.client.MainBean;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import com.gromod.client.annotation.Component;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
@GuiModule(name = "Custom Renderer",category = GuiModule.Category.Fps)
public class CustomRendererModule {
    @Getter
    private static CustomRendererModule instance;

    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Custom Renderer")
    private boolean customRender = true;

    private boolean customRendering = false;

    private final CustomEntityLoader customRenderer;

    @AutoInit
    public CustomRendererModule() {
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
}
