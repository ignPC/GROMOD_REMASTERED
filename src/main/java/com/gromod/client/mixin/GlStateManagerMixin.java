package com.gromod.client.mixin;

import com.gromod.client.renderer.RenderPipeline;
import com.gromod.client.utils.MessageUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {

    @Inject(method = {"setActiveTexture"}, at = {@At("HEAD")})
    private static void setActiveTexture(int texture, CallbackInfo ci) {
        RenderPipeline.currentActiveTexture = texture;
    }

    @Inject(method = {"bindTexture"}, at = {@At("HEAD")})
    private static void bindTexture(int texture, CallbackInfo ci) {
        RenderPipeline.currentBindTexture = texture;
    }
}
