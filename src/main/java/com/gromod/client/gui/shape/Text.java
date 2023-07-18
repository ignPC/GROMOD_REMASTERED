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

    @Override
    public void push() {
        interBold.drawString(x, y, size, text, new BColor(0, 0, 0, 1));
    }
}
