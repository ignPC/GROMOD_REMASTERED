package com.nut.client.gui.shape;

import com.nut.client.gui.guicomponent.GuiComponent;
import com.nut.client.renderer.font.CustomFont;
import com.nut.client.renderer.font.FontAtlasBuilder;
import com.nut.client.utils.BColor;

public class Text extends Shape<Text>{
    private String text;
    protected static CustomFont interBold;

    public Text(GuiComponent guiComponent, int width, int height, String text, BColor color) {
        super(guiComponent, width, height, color);
        this.text = text;
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
    }

    public Text(GuiComponent guiComponent, String text, BColor color) {
        super(guiComponent, color);
        interBold = FontAtlasBuilder.fonts.get("Inter-Bold.ttf");
        this.text = text;
    }

    public Text text(String text){
        this.text = text;
        return this;
    }

    @Override
    public void push() {
        interBold.drawString(x, y, text, new BColor(0, 0, 0, 1));
    }
}
