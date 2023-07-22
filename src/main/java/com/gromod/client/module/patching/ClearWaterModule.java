package com.gromod.client.module.patching;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import lombok.Getter;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Component
@GuiModule(name = "Clear Water",category = GuiModule.Category.Patching)
public class ClearWaterModule {
    @Getter
    private static ClearWaterModule instance;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Clear Water")
    private boolean clearWater = true;

    @AutoInit
    public ClearWaterModule() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
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
}