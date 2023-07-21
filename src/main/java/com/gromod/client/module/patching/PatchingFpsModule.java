package com.gromod.client.module.patching;

import com.gromod.client.MainBean;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@Component
@GuiModule(name = "Patching FPS",category = GuiModule.Category.Patching, index = 3)
public class PatchingFpsModule {
    @Getter
    private static PatchingFpsModule instance;
    private final CustomEntityLoader customRenderer;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Patching FPS")
    private boolean patchingFps = false;

    private boolean limitingEntities = false;

    @AutoInit
    public PatchingFpsModule(){
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        customRenderer = MainBean.getInstance().getCustomEntityRenderer();
        customRenderer.setPatchingFPS(patchingFps);
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        if (patchingFps && !limitingEntities) {
            limitingEntities = true;
            customRenderer.setPatchingFPS(true);
        } else if (!patchingFps && limitingEntities) {
            limitingEntities = false;
            customRenderer.setPatchingFPS(false);
        }
    }
}
