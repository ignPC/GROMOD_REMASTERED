package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.GuiOpenEvent;
import com.nut.client.guitest.Circle;
import com.nut.client.guitest.RoundedRectangle;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    private final List<com.nut.client.guitest.AbstractShape> shapes = new ArrayList<>();

    @AutoInit
    public BaseGui() {
        init();
    }

    public void init() {
        Circle circle = new Circle(0, 0, 100, 100, new Color(1, 1, 0, 1))
                .radius(40)
                .shade(2)
                .halo(6);

        Circle circle1 = new Circle(200, 200, 100, 100, new Color(1, 1, 0, 1))
                .radius(40)
                .shade(2)
                .halo(6);

        RoundedRectangle roundedRectangle = new RoundedRectangle(0, 0, 400, 1080, new Color(1, 0, 0, 1))
                .radius(40)
                .shade(10);

        shapes.add(circle);
        shapes.add(circle1);
        shapes.add(roundedRectangle);
    }

    public void openGui() {
        RenderPipeline.clearPipeline();
        for (com.nut.client.guitest.AbstractShape shape : shapes)
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
