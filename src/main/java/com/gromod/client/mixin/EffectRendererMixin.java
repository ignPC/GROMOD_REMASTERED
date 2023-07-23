package com.gromod.client.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin {

    @Shadow
    protected abstract void tickParticle(EntityFX entityfx);

    /**
     * @author PC
     * @reason more fps pleaseeeeeeeee
     */

    @Overwrite
    private void updateEffectAlphaLayer(List<EntityFX> entitiesFX)
    {
        List<EntityFX> list = Lists.<EntityFX>newArrayList();

        for (int i = 0; i < entitiesFX.size(); ++i)
        {
            EntityFX entityfx = (EntityFX)entitiesFX.get(i);

            if (!(entityfx instanceof EntitySmokeFX))
                this.tickParticle(entityfx);
            else
                entityfx.setDead();

            if (entityfx.isDead)
            {
                list.add(entityfx);
            }
        }

        entitiesFX.removeAll(list);
    }
}
