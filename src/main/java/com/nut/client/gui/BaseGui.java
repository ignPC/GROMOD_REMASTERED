package com.nut.client.gui;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.gui.guicomponent.GuiComponent;
import com.nut.client.gui.shape.RRectangle;
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
    private final List<GuiComponent> components = new ArrayList<>();
    private final Minecraft minecraft;

    private float screenWidth = 1920;
    private float screenHeight = 1080;
    private int eventButton;
    private long lastMouseEvent;

    protected static CustomFont interBold;
    protected static GuiComponent screen = new GuiComponent(0, 0, 1920, 1080);

    @AutoInit
    public BaseGui(Minecraft minecraft) {
        this.minecraft = minecraft;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
        init();
    }

    public void init() {
        GuiComponent component = new GuiComponent(screen, 400, 400)
                .offset(100, 100)
                .shapes(guiComponent -> {
                    new RRectangle(guiComponent, new BColor(1, 0, 0, 1))
                            .radius(20)
                            .shade(3);

                    new RRectangle(guiComponent, 200, 200, new BColor(0, 0, 1, 1))
                            .offset(100, 100)
                            .radius(20)
                            .shade(3);
                })
                .listen2Hover(shapes -> shapes.get(0).color = new BColor(0, 1, 0, 1));

        components.add(component);
    }

    public void drawGui() {
        for (GuiComponent component : components)
            component.drawComponent();
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
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                mouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
            }
        }

        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState())
                    keyboardInput(Keyboard.getEventKey());
            }
        }
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
        mouseY = Display.getHeight() - mouseY;

        for (GuiComponent component : components)
            if (component.update) component.update(mouseX, mouseY);

        if (Mouse.getEventButtonState()) {
            this.eventButton = button;
            this.lastMouseEvent = System.currentTimeMillis();

            for (GuiComponent component : components)
                if (component.update && component.hovered)
                    component.mouseClick(button, mouseX, mouseY);
        } else if (button != -1) {
            this.eventButton = -1;
            for (GuiComponent component : components)
                if (component.update)
                    component.mouseRelease(mouseX, mouseY);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            for (GuiComponent component : components)
                if (component.update)
                    component.mouseDrag(mouseX, mouseY);
        }
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
