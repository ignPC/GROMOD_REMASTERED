package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.gui.shape.AbstractShape;
import com.nut.client.gui.shape.FloatDir;
import com.nut.client.gui.shape.shapes.Circle;
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
        RRectangle roundedRectangle = (RRectangle) new RRectangle(500, 500, 400, 1080, new Color(1, 0, 0, 1))
                .withRadius(40)
                .withShade(2);

        roundedRectangle.add(new RRectangle(0, 0, 200, 100, new Color(0, 1, 0, 1))
                .withRadius(40)
                .withShade(2));

        shapes.add(roundedRectangle);
    }

    public void openGui() {
        RenderPipeline.clearPipeline();
        for (AbstractShape shape : shapes)
            shape.draw();

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
