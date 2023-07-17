package com.nut.client.gui;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.GuiField;
import com.nut.client.annotation.GuiModule;
import com.nut.client.annotation.Component;
import com.nut.client.gui.guicomponent.GuiComponent;
import com.nut.client.gui.shape.RRectangle;
import com.nut.client.gui.shape.Text;
import com.nut.client.module.FlyBoostModule;
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
import org.reflections.Reflections;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    public static Scaled scaled = new Scaled();
    private final List<GuiComponent> components = new ArrayList<>();
    private final Minecraft minecraft;
    private final Reflections reflections = new Reflections("com.nut.client");

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
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(GuiModule.class);
        int classHorizontalOffset = 10;

        for (Class<?> clazz : classes) {

            GuiComponent classComponent = new GuiComponent(screen, 400, 800)
                    .offset(classHorizontalOffset, 10);

            classHorizontalOffset += classComponent.width + 10;

            Field[] fields = clazz.getDeclaredFields();

            int fieldVerticalOffset = 5;

            for (Field field : fields) {
                GuiField guiField = field.getAnnotation(GuiField.class);
                if (guiField != null) {

                    GuiComponent fieldComponent = new GuiComponent(classComponent, 400, 60)
                            .offset(0, fieldVerticalOffset);

                    fieldVerticalOffset += fieldComponent.height + 5;

                    field.setAccessible(true); // Make the field accessible (in case it's private)
                    GuiField.Type fieldType = guiField.type();

                    switch (fieldType) {
                        case BUTTON:
                            initButton(clazz, field, fieldComponent);
                            break;
                        case SLIDER:
                            // Handle slider field
                            break;
                        case TEXT_INPUT:
                            // Handle text input field
                            break;
                    }
                }
            }
        }
    }

    private void initButton(Class<?> clazz, Field field, GuiComponent fieldComponent) {
        fieldComponent.listen2Click(shapes -> {
            try {
                Object instance = clazz.getMethod("getInstance").invoke(null);    // Get the current instance using the getInstance() method
                boolean fieldValue = field.getBoolean(instance);                            // Get the field value from the current instance

                if (fieldValue) {
                    shapes.get(0).color = new BColor(1, 0, 0, 1);
                    field.set(instance, false);                     // Set the field to false in the current instance
                } else {
                    shapes.get(0).color = new BColor(0, 1, 0, 1);
                    field.set(instance, true);                      // Set the field to true in the current instance
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        fieldComponent.shapes(guiComponent -> {
            boolean fieldValue = false;
            String name = "";

            try {
                Object instance = clazz.getMethod("getInstance").invoke(null);
                fieldValue = field.getBoolean(instance);

                GuiField guiField = field.getAnnotation(GuiField.class);
                if (guiField != null) {
                    name = guiField.label();
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            BColor color;
            if (fieldValue) {
                color = new BColor(0, 1, 0, 1); // Set to green if true
            } else {
                color = new BColor(1, 0, 0, 1); // Set to red if false
            }

            new RRectangle(guiComponent, color)
                    .radius(20)
                    .shade(3);

            new Text(guiComponent, name, new BColor(0, 0, 0, 1))
                    .offset(10, 5);
        });


        components.add(fieldComponent);
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
