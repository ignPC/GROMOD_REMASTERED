package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.gui.shape.AbstractShape;
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
        Circle circle = new Circle(0, 0, 100, 100, new Color(1, 1, 0, 1))
                .withRadius(40)
                .withShade(2)
                .withHalo(6);

        Circle circle1 = new Circle(200, 200, 100, 100, new Color(1, 1, 0, 1))
                .withRadius(40)
                .withShade(2)
                .withHalo(6);

        RRectangle roundedRectangle = new RRectangle(0, 0, 400, 1080, new Color(1, 0, 0, 1))
                .withRadius(40)
                .withShade(10);

        shapes.add(circle);
        shapes.add(circle1);
        shapes.add(roundedRectangle);
    }

    public void openGui() {
        RenderPipeline.clearPipeline();
        for (AbstractShape shape : shapes)
            shape.pushToPipeline();
        RenderPipeline.shapes = shapes.size();
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
