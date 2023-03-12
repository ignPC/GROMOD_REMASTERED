package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.shape.Circle;
import com.nut.client.gui.shape.Positioner;
import com.nut.client.gui.shape.RRectangle;
import com.nut.client.gui.shape.Shape;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.renderer.font.CustomFont;
import com.nut.client.renderer.font.FontAtlasBuilder;
import com.nut.client.renderer.util.ProjectionUtils;
import com.nut.client.utils.BColor;
import com.nut.client.utils.Scaled;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    public static Scaled scaled = new Scaled();
    private final List<Shape> shapes = new ArrayList<>();
    private final Minecraft minecraft;

    private float screenWidth = 1920;
    private float screenHeight = 1080;

    private static CustomFont interBold;

    @AutoInit
    public BaseGui(Minecraft minecraft) {
        this.minecraft = minecraft;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
        init();
    }

    public void init() {
        RRectangle rectangle = new RRectangle(100, 200, new BColor(0, 1, 0, 1))
                .radius(20)
                .shade(2);

        RRectangle rRectangle = new RRectangle(100, 100, new BColor(1, 0, 0, 1))
                .radius(30)
                .shade(4);

        RRectangle rRectangle1 = new RRectangle(100, 100, new BColor(0, 0, 1, 1))
                .radius(20)
                .shade(2);

        RRectangle rRectangle2 = new RRectangle(100, 100, new BColor(0, 1, 1, 1))
                .margin(40, 30, 0, 0)
                .radius(20)
                .shade(2);

        Circle circle = new Circle(200, 200, new BColor(1, 1, 1, 1))
                .margin(0, 60, 0, 0)
                .radius(100)
                .shade(2)
                .halo(4);

        Positioner.position(0, 0, 2, 3, 10, rectangle, rRectangle, rRectangle1, rRectangle2, circle);
        shapes.add(rectangle);
        shapes.add(rRectangle);
        shapes.add(rRectangle1);
        shapes.add(rRectangle2);
        shapes.add(circle);
    }

    public void drawGui() {
        for (Shape shape : shapes)
            shape.push();

        interBold.drawString(0, 0, "Your mom gay af lmao kekw", new BColor(1, 0, 0, 0.8f));
    }

    public void openGui() {
        minecraft.setIngameNotInFocus();
        if (!handleResolution())
            refreshPipeline();
        currentScreen = this;
    }

    public void closeGui() {
        currentScreen = null;
        minecraft.setIngameFocus();
    }

    public void loop() {
        handleResolution();
        handleInput();
    }

    public void handleInput() {
        if (Keyboard.getEventKeyState())
            keyboardInput(Keyboard.getEventKey());

        mouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
    }

    public void keyboardInput(int keyCode) {
        if (keyCode == 1)
            closeGui();
        else if (keyCode == 87) {
            minecraft.toggleFullscreen();
            if (!handleResolution())
                refreshPipeline();
        }
    }

    public void mouseInput(int button, int mouseX, int mouseY) {

    }

    public boolean handleResolution() {
        if (Display.getWidth() != screenWidth || Display.getHeight() != screenHeight) {
            screenWidth = Display.getWidth();
            screenHeight = Display.getHeight();
            ProjectionUtils.setOrthoProjection(0, Display.getWidth(), 0, Display.getHeight(), 0, 1);
            scaled.set(Display.getWidth() / 1920f, Display.getHeight() / 1080f);
            refreshPipeline();
            return true;
        }
        return false;
    }

    public void refreshPipeline() {
        RenderPipeline.clearGuiPipeline();
        drawGui();
        RenderPipeline.refreshGuiPipeline();
    }
}
