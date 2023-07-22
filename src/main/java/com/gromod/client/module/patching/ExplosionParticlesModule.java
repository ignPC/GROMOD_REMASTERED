package com.gromod.client.module.patching;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Component
@GuiModule(name = "Explosion Particles",category = GuiModule.Category.Patching)
public class ExplosionParticlesModule {
    @Getter
    private static ExplosionParticlesModule instance;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Explosion Particles")
    private boolean explosionparticles = false;

    @AutoInit
    public ExplosionParticlesModule(){
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }
}
