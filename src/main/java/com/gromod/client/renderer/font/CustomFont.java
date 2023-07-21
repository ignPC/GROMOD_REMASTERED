package com.gromod.client.renderer.font;

import com.gromod.client.gui.TestGui;
import com.gromod.client.renderer.RenderPipeline;
import com.gromod.client.utils.BColor;
import com.gromod.client.utils.ShapeType;
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
        float xScale = TestGui.scaled.getXScale();
        float yScale = TestGui.scaled.getYScale();

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

    public void drawString(int x, int y, float letterSize, String text, BColor color) {
        CharInfo charInfo = charInfos['a'];
        float xScale = TestGui.scaled.getXScale();
        float yScale = TestGui.scaled.getYScale();

        x = (int) (x * xScale);
        y = (int) (Display.getHeight() - y * yScale - charInfo.height * yScale * letterSize);
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                x += charInfo.metrics.charWidth(' ') * xScale * letterSize;
                continue;
            }
            if (character == 'j') {
                x -= charInfo.metrics.getDescent() * xScale * letterSize;
            }
            charInfo = charInfos[character];
            int width = (int) (charInfo.width * xScale * letterSize);
            int height = (int) (charInfo.height * yScale * letterSize);

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

    public int getWidth(float letterSize, String text) {
        int totalWidth = 0;
        CharInfo charInfo = charInfos['a'];
        float xScale = TestGui.scaled.getXScale();

        for (char character : text.toCharArray()) {
            if (character == ' ') {
                totalWidth += charInfo.metrics.charWidth(' ') * xScale * letterSize;
                continue;
            }
            charInfo = charInfos[character];
            totalWidth += (int) (charInfo.width * xScale * letterSize);
        }

        return totalWidth;
    }

    public int getHeight(float letterSize) {
        CharInfo charInfo = charInfos['a'];
        float yScale = TestGui.scaled.getYScale();
        return (int) (charInfo.height * yScale * letterSize);
    }
}
