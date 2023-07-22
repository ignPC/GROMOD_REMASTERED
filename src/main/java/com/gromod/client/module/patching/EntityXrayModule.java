package com.gromod.client.module.patching;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Component
@GuiModule(name = "Entity Xray",category = GuiModule.Category.Patching)
public class EntityXrayModule {
    @Getter
    private static EntityXrayModule instance;
    private final CustomEntityLoader customRenderer;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Entity Xray")
    private boolean entityXray = true;

    private boolean entityXRaying = true;


    @AutoInit
    public EntityXrayModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        customRenderer = MainBean.getInstance().getCustomEntityRenderer();
        customRenderer.setXray(entityXray);
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }

        if (entityXray && !entityXRaying){
            entityXRaying = true;
            customRenderer.setXray(true);
        } else if (!entityXray && entityXRaying) {
            entityXRaying = false;
            customRenderer.setXray(false);
        }
    }
}
