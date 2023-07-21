package com.gromod.client.gui;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.gui.shape.Rectangle;
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
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TestGui {

    public static TestGui currentScreen;
    public static Scaled scaled = new Scaled();
    private final List<GuiComponent> components = new ArrayList<>();
    private final Minecraft minecraft;
    private final Reflections reflections = new Reflections("com.gromod.client");

    private float screenWidth = 1920;
    private float screenHeight = 1080;

    private int eventButton;
    private long lastMouseEvent;

    private final BColor backgroundColor = new BColor(0.537f, 0.427f, 0.361f, 0.72f);
    private final BColor shade1 = new BColor(0.451f, 0.349f, 0.286f, 1f);
    private final BColor shade2 = new BColor(0.247f, 0.141f, 0.078f, 1f);
    private final BColor shade3 = new BColor(0.165f, 0.094f, 0.051f, 1f);
    private final BColor orange1 = new BColor(0.741f, 0.424f, 0.231f, 1f);
    private final BColor white = new BColor(1f, 1f, 1f, 1f);

    private int mainBoxWidth;
    private int mainBoxHeight;
    private int mainBoxOffsetX;
    private int mainBoxOffsetY;
    private final int mainBoxPaddingX = (int) ((screen.width / 192) * 6.1); //61
    private final int mainBoxPaddingY = (int) ((screen.width / 108) * 4.0); //40

    private GuiComponent mainBox;
    private GuiComponent topBox;

    private GuiComponent categoryBox; //fps, patching, movement, schematica
    private GuiComponent moduleBox;  //example: fps: custom renderer
    private GuiComponent fieldBox;   //example: fps: custom renderer: toggle custom render sand, toggle custom render tnt
    private GuiComponent scrollbar;

    protected static CustomFont interBold;
    protected static GuiComponent screen = new GuiComponent(0, 0, 1920, 1080);

    @AutoInit
    public TestGui(Minecraft minecraft) {
        this.minecraft = minecraft;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
        init();
    }

    @SneakyThrows
    public void init() {

        //---------------------------------------------------------BACKGROUND---------------------------------------------------------//

        screen.shapes(guiComponent -> { // Background shapes
            new Rectangle(guiComponent, backgroundColor);  // Makes background darker

            int offsetX1 = (int) (guiComponent.width / 8);
            int offsetY1 = (int) (guiComponent.height / 6);

            int width1 = (int) (guiComponent.width - offsetX1 * 2);
            int height1 = (int) (guiComponent.height - offsetY1 * 2);


            new Rectangle(guiComponent, width1, height1, shade1) // First outline
                    .offset(offsetX1, offsetY1)
                    .radius(95)
                    .shade(3);

            int offsetX2 = (int) ((guiComponent.width / 192) * 1.5);
            int offsetY2 = (int) ((guiComponent.height / 108) * 1.5);

            int width2 = width1 - offsetX2 * 2;
            int height2 = height1 - offsetY2 * 2;

            new Rectangle(guiComponent, width2, height2, shade2) // Second outline
                    .offset(offsetX1 + offsetX2, offsetY1 + offsetY2)
                    .radius(79)
                    .shade(3);

            int offsetX3 = (int) ((guiComponent.width / 192) * 1.4);
            int offsetY3 = (int) ((guiComponent.height / 108) * 1.4);

            int width3 = width2 - offsetX3 * 2;
            int height3 = height2 - offsetY3 * 2;

            new Rectangle(guiComponent, width3, height3, shade3) // Third outline aka main box
                    .offset(offsetX1 + offsetX2 + offsetX3, offsetY1 + offsetY2 + offsetY3)
                    .radius(61)
                    .shade(3);

            setMainBox(width3, height3, offsetX1 + offsetX2 + offsetX3, offsetY1 + offsetY2 + offsetY3);
        });



        //---------------------------------------------------------COMPONENTS---------------------------------------------------------//



        // Main Box Encapsulates Everything Below
        mainBox = new GuiComponent(screen, mainBoxWidth, mainBoxHeight)
                .offset(mainBoxOffsetX, mainBoxOffsetY);

        // Title + Search
        topBox = new GuiComponent(mainBox, mainBoxWidth, mainBoxHeight / 9);

        // General Module Categories
        int paddingY = (int) (mainBoxPaddingY / 1.8);
        int categoryOffsetX = mainBoxWidth / 16;

        categoryBox = new GuiComponent(mainBox, mainBoxWidth, mainBoxHeight / 9)
                .offset(0, topBox.height + paddingY);

        // Modules Under Categories
        int moduleBoxOffsetY = topBox.height + paddingY + categoryBox.height + paddingY;
        int moduleBoxHeight = mainBoxHeight - moduleBoxOffsetY;

        moduleBox = new GuiComponent(mainBox, mainBoxWidth, moduleBoxHeight)
                .offset(0, moduleBoxOffsetY)
                .shapes(guiComponent -> {
                    new Rectangle(guiComponent, mainBoxWidth, 2, shade1); // Line for decoration
        });

        // Scrollbar
        int scrollBarWidth = (int) (mainBoxPaddingX * 0.3);

        scrollbar = new GuiComponent(mainBox, scrollBarWidth, mainBox.height)
                .offset((int) (mainBox.width + mainBoxPaddingX - scrollBarWidth / 2), topBox.height)
                .shapes(guiComponent -> {
                    new Rectangle(guiComponent, (int) scrollBarWidth, (int) (mainBox.height - topBox.height * 2), shade2)
                            .radius(6)
                            .shade(3);
        });



        //---------------------------------------------------------SEARCH+TITLE---------------------------------------------------------//



        topBox.shapes(guiComponent -> {
            int moduleOffsetX = mainBoxWidth / 16;
            int moduleWidth = (moduleBox.width - moduleOffsetX * 2)  / 3;
            int categoryWidth = (categoryBox.width - categoryOffsetX * 3) / 4;

            int titleBoxWidth = guiComponent.width - moduleOffsetX - moduleWidth;


           new Rectangle(guiComponent, titleBoxWidth, guiComponent.height, shade2)
                   .radius(10)
                   .shade(3);
           new Text(guiComponent, 1, "Search Modules (WIP)", shade1)
                   .offset(guiComponent.width / 30, 0)
                   .centerY();

           new Text(guiComponent, 1.3f, "Gromod", white)
                   .offset(mainBox.width - Text.getWidth(1.3f, "Gromod"), 0)
                   .centerY();
        });


        components.add(screen);
        components.add(topBox);
        components.add(categoryBox);
        components.add(moduleBox);
        components.add(scrollbar);



        //---------------------------------------------------------CATEGORIES---------------------------------------------------------//



        int iteratingOffsetX = 0;

        ArrayList<GuiComponent> categoryComponents = new ArrayList<>();

        for (GuiModule.Category category : GuiModule.Category.values()) {
            GuiComponent categoryComponent = new GuiComponent(categoryBox, (categoryBox.width - categoryOffsetX * 3) / 4, categoryBox.height)
                    .offset(iteratingOffsetX, 0);

            BColor[] onColors = {shade1, shade1};
            BColor[] offColors = {orange1, white};

            // Set initial colors based on the toggledOn state
            BColor[] colors = categoryComponent.toggledOn ? offColors : onColors;
            categoryComponent.childrenVisible = categoryComponent.toggledOn;

            categoryComponent.listen2Click(shapes -> {
                BColor[] updatedColors = categoryComponent.toggledOn ? offColors : onColors;
                shapes.get(0).color = updatedColors[0];
                shapes.get(2).color = updatedColors[1];

                categoryComponent.childrenVisible = categoryComponent.toggledOn;

                if (!categoryComponent.toggledOn) return;

                for(GuiComponent guiComponent : categoryComponents){
                    if(guiComponent.toggledOn && guiComponent != categoryComponent)
                        guiComponent.mouseClick(1, 1,1);
                }
            });

            categoryComponent.shapes(guiComponent -> {
                int outlineThickness = 2;
                new Rectangle(guiComponent, colors[0])
                        .radius(10)
                        .shade(2);
                new Rectangle(guiComponent, guiComponent.width - 4, guiComponent.height - 4, shade2)
                        .offset(outlineThickness, outlineThickness)
                        .radius(9)
                        .shade(2);
                new Text(guiComponent, 1f, category.toString(), colors[1])
                        .offset(categoryComponent.width / 15, 0)
                        .centerY();
            });

            iteratingOffsetX += categoryOffsetX + categoryComponent.width;
            components.add(categoryComponent);
            categoryComponents.add(categoryComponent);
        }

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(GuiModule.class);
        ArrayList<Class<?>> sortedClasses = classes.stream()
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(GuiModule.class).index())).collect(Collectors.toCollection(ArrayList::new));



        //---------------------------------------------------------MODULES---------------------------------------------------------//



        ArrayList<GuiComponent> moduleComponents = new ArrayList<>();

        for (GuiModule.Category category : GuiModule.Category.values()){
            int moduleOffsetX = mainBoxWidth / 16;
            int moduleOffsetY = (int) (paddingY * 1.3);

            int moduleIteratingOffsetX = 0;
            int moduleIteratingOffsetY = moduleOffsetY;

            for (Class<?> clazz : sortedClasses) {
                if(clazz.getAnnotation(GuiModule.class) == null) continue;
                if(clazz.getAnnotation(GuiModule.class).category() != category) continue;

                GuiComponent parentComponent = null;

                switch (category) {
                    case Fps:
                        parentComponent = categoryComponents.get(0);
                        break;
                    case Patching:
                        parentComponent = categoryComponents.get(1);
                        break;
                    case Schematica:
                        parentComponent = categoryComponents.get(2);
                        break;
                    case Other:
                        parentComponent = categoryComponents.get(3);
                        break;
                }

                GuiComponent moduleComponent = new GuiComponent(moduleBox, (moduleBox.width - moduleOffsetX * 2)  / 3, (moduleBox.height - moduleOffsetY * 2) / 2)
                        .offset(moduleIteratingOffsetX, moduleIteratingOffsetY)
                        .parent(parentComponent);

                // Iterate Offsets
                moduleIteratingOffsetX += moduleComponent.width + moduleOffsetX;
                if(moduleComponent.x + moduleComponent.width + moduleOffsetX + moduleComponent.width >= mainBox.x + mainBox.width){
                    moduleIteratingOffsetX = 0;
                    moduleIteratingOffsetY += moduleComponent.height + moduleOffsetY;
                }

                // Get Button Value (On or Off)
                Field mainButtonField = null;
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getAnnotation(GuiField.class) == null) continue;
                    if (field.getAnnotation(GuiField.class).type() != GuiField.Type.MAIN_BUTTON) continue;

                    mainButtonField = field;
                }

                if (mainButtonField == null) continue;

                mainButtonField.setAccessible(true);
                Object instance = clazz.getMethod("getInstance").invoke(null);
                boolean buttonValue = mainButtonField.getBoolean(instance);

                // Set initial colors based on the state
                BColor[] onColors = {shade1, shade1};
                BColor[] offColors = {orange1, white};
                BColor[] colors = buttonValue ? offColors : onColors;

                Field finalMainButtonField = mainButtonField;
                moduleComponent.listen2Click(shapes -> {
                    boolean updatedButtonValue = false;
                    try {
                        updatedButtonValue = finalMainButtonField.getBoolean(instance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    try {
                        finalMainButtonField.set(instance, !updatedButtonValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    BColor[] updatedColors = updatedButtonValue ? onColors : offColors;
                    shapes.get(0).color = updatedColors[0];
                    shapes.get(2).color = updatedColors[1];
                });


                moduleComponent.shapes(guiComponent1 -> {
                    int outlineThickness = 2;
                    new Rectangle(moduleComponent, colors[0])
                            .radius(10)
                            .shade(2);
                    new Rectangle(moduleComponent, moduleComponent.width - 4, moduleComponent.height - 4, shade2)
                            .offset(outlineThickness, outlineThickness)
                            .radius(9)
                            .shade(2);
                    new Text(moduleComponent, 1f, finalMainButtonField.getAnnotation(GuiField.class).label(), colors[1])
                            .offset(moduleComponent.width / 15, 0)
                            .center();
                });

                components.add(moduleComponent);
                moduleComponents.add(moduleComponent);
            }
        }
    }


    public void drawGui() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
        for (GuiComponent component : components) {
            if (component.parent != null) component.setVisible(component.parent.childrenVisible);
            if (component.isVisible) component.drawComponent();
        }
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

    public void setMainBox(int width, int height, int offsetX, int offsetY) {
        mainBoxWidth = width - mainBoxPaddingX * 3;
        mainBoxHeight = height - mainBoxPaddingY * 2;
        mainBoxOffsetX = offsetX + mainBoxPaddingX;
        mainBoxOffsetY = offsetY + mainBoxPaddingY;
    }
}
