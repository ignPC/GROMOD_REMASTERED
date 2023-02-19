package com.nut.client;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.AfterScreenCreationEvent;
import com.nut.client.renderer.GLObject;
import com.nut.client.renderer.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

@Component
public class TestGui extends GuiScreen {

    private final KeyBinding keyBinding;
    private final Shader circle = new Shader(
            null,
            new ResourceLocation("bean", "shaders/circleFS.glsl"),
            "center"
    );
    private GLObject object;

    @AutoInit
    public TestGui() {
        MinecraftForge.EVENT_BUS.register(this);
        keyBinding = new KeyBinding("Gui", 56, "Nut");
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    public void setup() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        glUseProgram(circle.getShaderProgram());
        glUniform2f(circle.getUniform("center"), Display.getWidth() / 2f, Display.getHeight() / 2f);

        object.render();
        object.unbindVao();

        glUseProgram(0);
    }

    @SubscribeEvent
    public void onScreenCreation(AfterScreenCreationEvent event) {
        float[] positions = {
                0, 0,
                0, 1080,
                1920, 1080,
                1920, 0
        };

        object = new GLObject(GL_QUADS, 0, 4)
                .bindVao()
                .addVbo(GL_ARRAY_BUFFER)
                .bindVbo(0)
                .populateVao(0, 2, GL_FLOAT, 0, 0)
                .populateVbo(0, positions, GL_STATIC_DRAW)
                .unbindVbo(0)
                .unbindVao();
    }

    @SubscribeEvent
    public void onClientTick(InputEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            if (keyBinding.isPressed())
                Minecraft.getMinecraft().displayGuiScreen(this);
        }
    }
}
