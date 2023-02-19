package com.nut.client.gui.guibuilder;


import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.gui.shape.AbstractShape;
import com.nut.client.gui.shape.shapes.RRectangle;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    private final List<AbstractShape> shapes = new ArrayList<>();

    @AutoInit
    public BaseGui() {
        init();
    }

    public void init() {
        RRectangle rRectangle = new RRectangle((int) (1920 / 2f - 100), (int) (1080 / 2f - 100), 200, 200, new Color(1f, 1f, 0f, 1f), 10);

        shapes.add(rRectangle);
    }

    public void openGui() {
        RenderPipeline.clearPipeline();
        for (AbstractShape shape : shapes)
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
