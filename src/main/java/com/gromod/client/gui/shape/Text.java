package com.gromod.client.gui.shape;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.renderer.font.CustomFont;
import com.gromod.client.renderer.font.FontAtlasBuilder;
import com.gromod.client.utils.BColor;

public class Text extends Shape<Text>{
    private String text;
    protected static CustomFont interBold;
    private float size;

    public Text(GuiComponent guiComponent, int width, int height, float size, String text, BColor color) {
        super(guiComponent, width, height, color);
        this.text = text;
        this.size = size;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
    }

    public Text(GuiComponent guiComponent, float size, String text, BColor color) {
        super(guiComponent, color);
        this.text = text;
        this.size = size;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
    }

    public Text text(String text){
        this.text = text;
        return this;
    }

    public Text size(float size){
        this.size = size;
        return this;
    }

    public static int getWidth(float size, String text){
        return interBold.getWidth(size, text);
    }
    public static int getHeight(float size) {
        return interBold.getHeight(size);
    }

    public Text center() {
        int textWidth = getWidth(this.size, text);
        int textHeight = getHeight(this.size);

        int parentCenterX = parent.x + parent.width / 2;
        int parentCenterY = parent.y + parent.height / 2;

        int textX = parentCenterX - (textWidth / 2);
        int textY = parentCenterY - (textHeight / 2);

        this.x = textX;
        this.y = textY;

        return this;
    }

    public Text centerX() {
        int textWidth = getWidth(this.size, text);
        int parentCenterX = parent.x + parent.width / 2;
        this.x = parentCenterX - (textWidth / 2);
        return this;
    }

    public Text centerY() {
        int textHeight = getHeight(this.size);
        int parentCenterY = parent.y + parent.height / 2;
        this.y = parentCenterY - (textHeight / 2);
        return this;
    }

    @Override
    public void push() {
        interBold.drawString(x, y, size, text, color);
    }
}
