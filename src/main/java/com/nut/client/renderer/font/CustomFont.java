package com.nut.client.renderer.font;

import com.nut.client.gui.guibuilder.BaseGui;
import com.nut.client.utils.ShapeType;
import com.nut.client.renderer.RenderPipeline;
import com.nut.client.utils.BColor;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.InputStream;

public class CustomFont {

    public static final String ALL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-/|;:{}[]\"',.=<>?\\";

    public final String fontName;
    public final float fontSize;
    public final String characters;
    public final int spacing;
    public final CharInfo[] charInfos = new CharInfo[128];
    public Font font;

    public CustomFont(String fontName, float fontSize, String characters, int spacing) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.characters = characters;
        this.spacing = spacing;
        loadFont();
    }

    @SneakyThrows
    private void loadFont() {
        ResourceLocation fontLocation = new ResourceLocation("bean", "fonts/" + fontName);
        InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream();
        font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(fontSize);
    }

    public void drawString(int x, int y, String text, BColor color) {
        CharInfo charInfo = charInfos['a'];
        float xScale = BaseGui.scaled.getXScale();
        float yScale = BaseGui.scaled.getYScale();

        x = (int) (x * xScale);
        y = (int) (Display.getHeight() - y * yScale - charInfo.height * yScale);
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                x += charInfo.metrics.charWidth(' ') * xScale;
                continue;
            }
            if (character == 'j') {
                x -= charInfo.metrics.getDescent() * xScale;
            }
            charInfo = charInfos[character];
            int width = (int) (charInfo.width * xScale);
            int height = (int) (charInfo.height * yScale);

            RenderPipeline.queueGuiData(
                    x, y,
                    color.r, color.g, color.b, color.a,
                    width, height,
                    0, 0, 0, ShapeType.FONT.ordinal(),
                    charInfo.x, charInfo.y,
                    charInfo.uvWidth, charInfo.uvHeight
            );
            RenderPipeline.guiShapes++;
            x += width;
        }
    }
}
