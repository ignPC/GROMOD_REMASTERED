package com.gromod.client.gui.guicomponent;

import com.google.common.io.PatternFilenameFilter;
import com.gromod.client.gui.NewGui;
import com.gromod.client.gui.shape.Shape;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.Scaled;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class GuiComponent {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean hovered;
    public boolean isBeingClicked;
    private boolean isBeingRightClicked;
    public boolean toggledOn = false;
    private boolean rightClickToggledOn = false;
    public boolean update = true;

    @Setter public boolean isVisible = true;
    @Setter public BColor[] colors;


    private final Scaled scaled = NewGui.scaled;
    public List<Shape> shapes = new ArrayList<>();

    public GuiComponent parent;
    @Getter public List<GuiComponent> children = new ArrayList<>();

    private Consumer<List<Shape>> clickListener;
    private Consumer<List<Shape>> clickReleaseListener;
    private Consumer<List<Shape>> hoverListener;
    private Consumer<List<Shape>> dragListener;
    private Consumer<List<Shape>> rightClickListener;

    public GuiComponent(GuiComponent parent, int width, int height) {
        this.parent = parent;
        parent.getChildren().add(this);
        this.isVisible = parent.isVisible();
        this.width = width;
        this.height = height;
        this.x = parent.x;
        this.y = parent.y;
    }

    public GuiComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GuiComponent offset(int offsetX, int offsetY) {
        x = parent.x + offsetX;
        y = parent.y + offsetY;
        return this;
    }

    public GuiComponent shapes(Consumer<GuiComponent> action) {
        action.accept(this);
        return this;
    }

    public GuiComponent listen2Click(Consumer<List<Shape>> action) {
        clickListener = action;
        return this;
    }

    public GuiComponent listen2RightClick(Consumer<List<Shape>> action) {
        rightClickListener = action;
        return this;
    }

    public GuiComponent listen2ClickRelease(Consumer<List<Shape>> action) {
        clickReleaseListener = action;
        return this;
    }

    public GuiComponent listen2Hover(Consumer<List<Shape>> action) {
        hoverListener = action;
        return this;
    }

    public GuiComponent listen2Drag(Consumer<List<Shape>> action) {
        dragListener = action;
        return this;
    }

    public GuiComponent parent(GuiComponent newParent){
        this.parent.getChildren().remove(this);
        this.parent = newParent;
        newParent.getChildren().add(this);
        return this;
    }

    public void update(int mouseX, int mouseY) {
        float xScale = scaled.getXScale();
        float yScale = scaled.getYScale();
        hovered = mouseX >= x * xScale && mouseY >= y * yScale && mouseX < (x + width) * xScale && mouseY < (y + height) * yScale;
        if (hovered)
            mouseHover(mouseX, mouseY);
    }

    public void mouseHover(int mouseX, int mouseY) {
        if(!isVisible) return;
        if (hoverListener == null) return;
        hoverListener.accept(shapes);
    }

    public void mouseClick(int button, int mouseX, int mouseY) {
        if(!isVisible) return;
        toggledOn = !toggledOn;
        isBeingClicked = true;
        if (clickListener == null) return;
        clickListener.accept(shapes);
    }

    public void mouseClickRight(int button, int mouseX, int mouseY) {
        if(!isVisible) return;
        rightClickToggledOn = !rightClickToggledOn;
        if (rightClickListener == null) return;
        rightClickListener.accept(shapes);
    }

    public void mouseRelease(int mouseX, int mouseY) {
        if(!isVisible) return;
        isBeingClicked = false;
        if (clickReleaseListener == null) return;
        clickReleaseListener.accept(shapes);
    }

    public void mouseDrag(int mouseX, int mouseY) {
        if(!isVisible) return;
        if (dragListener == null) return;
        dragListener.accept(shapes);
    }

    public void drawComponent() {
        for (Shape shape : shapes)
            shape.push();
    }
}
