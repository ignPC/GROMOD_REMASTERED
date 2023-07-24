package com.gromod.client.gui.shape;

import com.gromod.client.gui.guicomponent.GuiComponent;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.RenderUtils;

public class Image extends Shape<Image> {
    public Image(GuiComponent guiComponent, int width, int height, BColor color) {
        super(guiComponent, width, height, color);
    }

    public Image(GuiComponent guiComponent, BColor color) {
        super(guiComponent, color);
    }

    public Image centerY() {
        int height = this.height;
        int parentCenterY = parent.y + parent.height / 2;
        this.y = parentCenterY - (height / 2);
        return this;
    }

    @Override
    public void push() {
        RenderUtils.drawImage(x, y, width, height, color);
    }
}
