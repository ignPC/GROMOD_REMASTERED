package com.gromod.client.module.patching;

import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.Component;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

@Component
@GuiModule(name = "Explosion Particles",category = GuiModule.Category.Patching)
public class ExplosionParticlesModule {
    @Getter
    private static ExplosionParticlesModule instance;

    @Getter
    @GuiField(type = GuiField.Type.MAIN_BUTTON, label = "Explosion Particles")
    private boolean explosionParticles = false;

    @AutoInit
    public ExplosionParticlesModule(){
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }
}
