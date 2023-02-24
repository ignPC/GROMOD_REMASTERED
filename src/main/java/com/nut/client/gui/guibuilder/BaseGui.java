package com.nut.client.gui.guibuilder;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.event.RenderPipelineRefreshEvent;
import com.nut.client.gui.shape.AbstractShape;
import com.nut.client.gui.shape.FloatDir;
import com.nut.client.gui.shape.shapes.RRectangle;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseGui {

    public static BaseGui currentScreen;
    private final List<AbstractShape> shapes = new ArrayList<>();
    private final Minecraft minecraft;

    private int width;
    private int height;

    @AutoInit
    public BaseGui(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.width = Display.getWidth();
        this.height = Display.getHeight();
        init();
        System.out.println(width + " " + height);
    }

    public void init() {
        RRectangle roundedRectangle = (RRectangle) new RRectangle(0, 0, 960, 540, new Color(1, 0, 0, 1))
                .withRadius(40)
                .withShade(2)
                .withPadding(20);

        roundedRectangle.add(new RRectangle(0, 0, 200, 1000, new Color(0, 1, 0, 1))
                .withRadius(40)
                .withShade(2)
                .withFloat(FloatDir.RIGHT));

        shapes.add(roundedRectangle);
    }

    public void openGui() {
        width =  Display.getWidth();
        height = Display.getHeight();

        System.out.println("WIDTH: " + width + " HEIGHT: " + height);

        RenderPipeline.clearPipeline();

        for (AbstractShape shape : shapes){
            shape.draw();

            //DEBUG SHAPES
            String ANSI_RED = "\u001B[31m";
            String ANSI_RESET = "\u001B[0m";

            System.out.println(ANSI_RED + "[SHAPE-DEBUG]" + ANSI_RESET + " " + shape.getInformation());
            for (AbstractShape childShape : shape.getChildShapeList()) {
                System.out.println(ANSI_RED + "[SHAPE-DEBUG]" + ANSI_RESET + "    -CHILD: " + childShape.getInformation());
            }
        }

        MinecraftForge.EVENT_BUS.post(new RenderPipelineRefreshEvent());
        minecraft.setIngameNotInFocus();
        currentScreen = this;
    }

    public void closeGui() {
        currentScreen = null;
        minecraft.setIngameFocus();
    }

    public void handleInput() {
        if (Keyboard.getEventKeyState())
            keyboardInput(Keyboard.getEventKey());

        mouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
    }

    public void keyboardInput(int keyCode) {
        if (keyCode == 1)
            closeGui();
    }

    public void mouseInput(int button, int mouseX, int mouseY) {

    }
}
