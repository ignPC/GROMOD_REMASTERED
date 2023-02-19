package com.nut.client.gui;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.renderer.RenderPipeline;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    private final List<AbstractShape1> shapes = new ArrayList<>();

    @AutoInit
    public BaseGui() {
        init();
    }

    public void init() {
        RRectangle1 rRectangle1 = new RRectangle1(1920 / 2f - 100, 1080 / 2f - 100, 200, 200, new Color(1f, 1f, 0f, 1f))
                .radius(80)
                .shade(2f)
                .halo(10f);
        shapes.add(rRectangle1);
    }

    public void openGui() {
        RenderPipeline.clearPipeline();
        for (AbstractShape1 shape : shapes)
            shape.pushToPipeline();
        MinecraftForge.EVENT_BUS.post(new GuiOpenEvent());
        currentScreen = this;
    }

    public void closeGui() {
        currentScreen = null;
    }

    public void keyboardInput(int keyCode) {
        if (keyCode == 1)
            closeGui();
    }

    public void mouseInput(int button, int mouseX, int mouseY) {

    }
}
