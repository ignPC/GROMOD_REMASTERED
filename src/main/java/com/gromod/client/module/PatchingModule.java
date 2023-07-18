package com.gromod.client.module;

import com.gromod.client.MainBean;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.renderer.entity.CustomEntityLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Component
@GuiModule(name = "PATCHING", index = 3)
public class PatchingModule {
    private static PatchingModule instance;
    private final CustomEntityLoader customRenderer;

    @GuiField(type = GuiField.Type.BUTTON, label = "Entity Xray")
    private boolean entityXray = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "FullBright")
    private boolean fullBright = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Clear Water")
    private boolean clearWater = true;

    @GuiField(type = GuiField.Type.BUTTON, label = "Patching FPS")
    private boolean patchingFps = false;

    private boolean entityXRaying = true;
    private boolean limitingEntities = false;

    @AutoInit
    public PatchingModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        customRenderer = MainBean.getInstance().getCustomEntityRenderer();
        customRenderer.setXray(entityXray);
        customRenderer.setPatchingFPS(patchingFps);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEntityViewRender(EntityViewRenderEvent.FogDensity event) {
        if (clearWater) {
            // Modify fog density to remove darkness effect
            event.density = 0.0f;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEntityViewRender(EntityViewRenderEvent.FogColors event) {
        if (clearWater) {
            // Modify color density to remove darkness effect
            event.red = 0.0F;
            event.green = 0.75F;
            event.blue = 1.0F;
        }
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

        if (entityXray && !entityXRaying){
            entityXRaying = true;
            customRenderer.setXray(true);
        } else if (!entityXray && entityXRaying) {
            entityXRaying = false;
            customRenderer.setXray(false);
        }

        if (patchingFps && !limitingEntities) {
            limitingEntities = true;
            customRenderer.setPatchingFPS(true);
        } else if (!patchingFps && limitingEntities){
            limitingEntities = false;
            customRenderer.setPatchingFPS(false);
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
        EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

        // Set gamma value to a high value (e.g., 100.0)
        gameSettings.gammaSetting = 100.0f;
    }

    private void disableFullbright() {
        EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

        // Set gamma value back to default (e.g., 1.0)
        gameSettings.gammaSetting = 1.0f;
    }

    public boolean isEntityXray() {
        return entityXray;
    }

    public boolean isFullBright() {
        return fullBright;
    }

    public boolean isClearWater() {
        return clearWater;
    }

    public boolean isPatchingFps() {
        return patchingFps;
    }

    public static PatchingModule getInstance() {
        return instance;
    }
}
