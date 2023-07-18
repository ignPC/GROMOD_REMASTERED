package com.gromod.client.mixin;

import com.gromod.client.event.GuiRenderEvent;
import com.gromod.client.gui.BaseGui;
import com.gromod.client.renderer.RenderPipeline;
import com.gromod.client.utils.FpsTimer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "updateCameraAndRender", at = @At("RETURN"))
    private void updateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (!FpsTimer.skipRender && BaseGui.currentScreen != null)
            BaseGui.currentScreen.drawGui();
        MinecraftForge.EVENT_BUS.post(new GuiRenderEvent());
    }

    @Inject(method = "renderWorldPass", at = @At("HEAD"))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        if (!FpsTimer.pollTimer())
            RenderPipeline.clearGuiPipeline();
    }
}
