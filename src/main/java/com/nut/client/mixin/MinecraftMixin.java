package com.nut.client.mixin;

import com.nut.client.event.AfterScreenCreationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "startGame", at = @At("RETURN"))
    private void startGame(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new AfterScreenCreationEvent());
    }
}
