package com.gromod.client.module.other;

import com.gromod.client.MainBean;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
@GuiModule(name = "Full Bright",category = GuiModule.Category.Patching, index = 3)
public class FullBrightModule {
    @Getter
    private static FullBrightModule instance;
    private final CustomEntityLoader customRenderer;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Full Bright")
    private boolean fullBright = true;

    @AutoInit
    public FullBrightModule(){
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        customRenderer = MainBean.getInstance().getCustomEntityRenderer();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }

        if (fullBright) {
            enableFullbright();
        } else {
            disableFullbright();
        }
    }

    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.RenderFogEvent event) {
        if (fullBright) {
            // Disable fog when fullbright is enabled
            GlStateManager.disableFog();
        }
    }

    private void enableFullbright() {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        gameSettings.gammaSetting = 100.0f;
    }

    private void disableFullbright() {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        gameSettings.gammaSetting = 1.0f;
    }
}
