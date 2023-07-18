package com.gromod.client.gui;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.gui.shape.RRectangle;
import com.gromod.client.annotation.AutoInit;
import com.gromod.client.annotation.GuiField;
import com.gromod.client.annotation.GuiModule;
import com.gromod.client.annotation.Component;
import com.gromod.client.gui.shape.Text;
import com.gromod.client.renderer.RenderPipeline;
import com.gromod.client.renderer.font.CustomFont;
import com.gromod.client.renderer.font.FontAtlasBuilder;
import com.gromod.client.renderer.util.ProjectionUtils;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.Scaled;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    public static Scaled scaled = new Scaled();
    private final List<GuiComponent> components = new ArrayList<>();
    private final Minecraft minecraft;
    private final Reflections reflections = new Reflections("com.gromod.client");

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
        ArrayList<Class<?>> sortedClasses = classes.stream()
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(GuiModule.class).index())).collect(Collectors.toCollection(ArrayList::new));

        int classHorizontalOffset = 10;

        screen.shapes(guiComponent -> {
            new RRectangle(guiComponent, new BColor(0.12f, 0.12f, 0.12f, 0.32f));
        });

        GuiComponent text = new GuiComponent(screen, screen.width, screen.height - 800)
                .offset(0, 950);
        text.shapes(guiComponent -> {
            new Text(guiComponent, 2.5f, "GROMOD PRE-ALPHA GUI", new BColor(0, 0, 0, 1))
                    .offset(10, 5);
        });

        components.add(screen);
        components.add(text);

        for (Class<?> clazz : sortedClasses) {

            GuiComponent classComponent = new GuiComponent(screen, (screen.width - 70) / 5, 800);
            classComponent.offset(classHorizontalOffset, 10);
            classComponent.shapes(guiComponent -> {
                GuiModule guiModule = clazz.getAnnotation(GuiModule.class);
                String name = guiModule.name();
                new RRectangle(guiComponent, classComponent.width, 45, new BColor(0.52f, 0.52f, 0.52f, 1))
                        .radius(12)
                        .shade(3);
                new Text(guiComponent, 1, name, new BColor(0, 0, 0, 1))
                        .offset(10, 5);
            });

            components.add(classComponent);

            classHorizontalOffset += classComponent.width + 10;

            Field[] fields = clazz.getDeclaredFields();

            int fieldVerticalOffset = 50;

            for (Field field : fields) {
                GuiField guiField = field.getAnnotation(GuiField.class);
                if (guiField == null) continue;

                GuiComponent fieldComponent = new GuiComponent(classComponent, classComponent.width, 60)
                        .offset(0, fieldVerticalOffset);

                fieldVerticalOffset += fieldComponent.height + 5;

                field.setAccessible(true);                  // Make the field accessible (in case it's private)
                GuiField.Type fieldType = guiField.type();

                switch (fieldType) {
                    case BUTTON:
                        initButton(clazz, field, fieldComponent, classComponent);
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

    private void initButton(Class<?> clazz, Field field, GuiComponent fieldComponent, GuiComponent classComponent) {
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

            new Text(guiComponent, 1, name, new BColor(0, 0, 0, 1))
                    .offset(10, 5);
        });

        components.add(fieldComponent);
    }

    public void drawGui() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
        for (GuiComponent component : components)
            component.drawComponent();
    }

    public void openGui() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
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
