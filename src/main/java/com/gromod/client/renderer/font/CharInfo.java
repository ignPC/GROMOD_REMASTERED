package com.gromod.client.renderer.font;

import java.awt.*;

public class CharInfo {

    public final FontMetrics metrics;
    public final float x, y, uvWidth, uvHeight;
    public final int width, height;

    public CharInfo(FontMetrics metrics, float x, float y, float uvWidth, float uvHeight, int width, int height) {
        this.metrics = metrics;
        this.x = x;
        this.y = y;
        this.uvWidth = uvWidth;
        this.uvHeight = uvHeight;
        this.width = width;
        this.height = height;
    }
}
