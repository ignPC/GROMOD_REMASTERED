package com.nut.client.renderer.font;

import com.nut.client.gui.shape.ShapeType;
import com.nut.client.gui.testshape.Shape;
import com.nut.client.renderer.RenderPipeline;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class FontRenderer {

    private final String fontName;
    private final float fontSize;
    private final String characters;
    private final int spacing;
    @Getter private int textureId;
    @Getter private final CharInfo[] charInfos = new CharInfo[128];

    public FontRenderer(String fontName, int fontSize, String characters, int spacing) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.characters = characters;
        this.spacing = spacing;
        init();
    }

    public void init() {
        createBitmap();
    }

    @SneakyThrows
    public void createBitmap() {
        ResourceLocation fontLocation = new ResourceLocation("bean", "fonts/" + fontName);
        InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream();
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(fontSize);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(font);
        FontMetrics fontMetrics = graphics.getFontMetrics();

        int width = 0;
        int height = fontMetrics.getHeight();
        for (char character : characters.toCharArray()) {
            if (!font.canDisplay(character)) continue;

            int charWidth = fontMetrics.charWidth(character);
            width += charWidth + spacing;
        }
        graphics.dispose();

        int widthHeight = getMinimumBitmapSize(width * height);
        image = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);

        int x = 0;
        int y = fontMetrics.getAscent() - fontMetrics.getDescent() - fontMetrics.getLeading();
        for (char character : characters.toCharArray()) {
            if (!font.canDisplay(character)) continue;

            int charWidth = fontMetrics.charWidth(character);
            if (x + charWidth > widthHeight) {
                x = 0;
                y += fontMetrics.getHeight();
            }
            charInfos[character] = new CharInfo(
                    fontMetrics,
                    (float) x / widthHeight,
                    (float) (y - fontMetrics.getHeight() + fontMetrics.getDescent()) / widthHeight,
                    (float) charWidth / widthHeight,
                    (float) height / widthHeight,
                    charWidth,
                    height);
            graphics.drawString(String.valueOf(character), x, y);
            x += charWidth + spacing;
        }
        graphics.dispose();
        createTexture(image);
    }

    @SneakyThrows
    private void createTexture(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight());

        for(int h = 0; h < image.getHeight(); h++) {
            for(int w = 0; w < image.getWidth(); w++) {
                int pixel = pixels[h * image.getWidth() + w];

                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, image.getWidth(), image.getHeight(), 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
    }

    private int getMinimumBitmapSize(int surface) {
        int length = (int) Math.sqrt(surface);
        length |= length >> 1;
        length |= length >> 2;
        length |= length >> 4;
        length |= length >> 8;
        length |= length >> 16;
        return ++length;
    }

    public void drawString(int x, int y, String text) {
        CharInfo charInfo = charInfos['a'];
        float xScale = Shape.xScale;
        float yScale = Shape.yScale;

        x = (int) (x * xScale);
        y = (int) (Display.getHeight() - y * yScale - (charInfo.height - charInfo.metrics.getDescent()) * yScale);
        for (char character : text.toCharArray()) {
            charInfo = charInfos[character];
            int width = (int) (charInfo.width * xScale);
            int height = (int) (charInfo.height * yScale);

            float uvWidth = charInfo.uvWidth;
            float uvHeight = charInfo.uvHeight;

            RenderPipeline.queueData(RenderPipeline.quadPositions, x, y + height, x, y, x + width, y, x + width, y + height);
            for (int i = 0; i < 4; i++) {
                RenderPipeline.queueData(RenderPipeline.colors, 0, 0, 0, 0);
                RenderPipeline.queueData(RenderPipeline.shapePositions, 0, 0);
                RenderPipeline.queueData(RenderPipeline.shapeSizes, 0, 0);
                RenderPipeline.queueData(RenderPipeline.radiusFloats, 0);
                RenderPipeline.queueData(RenderPipeline.shadeFloats, 0);
                RenderPipeline.queueData(RenderPipeline.haloFloats, 0);
                RenderPipeline.queueData(RenderPipeline.shapeTypeFloats, ShapeType.FONT.ordinal());
            }
            RenderPipeline.queueData(RenderPipeline.textureCoordFloats, charInfo.x, charInfo.y, charInfo.x, charInfo.y + uvHeight,
                    charInfo.x + uvWidth, charInfo.y + uvHeight, charInfo.x + uvWidth, charInfo.y);
            RenderPipeline.shapes++;
            x += width;
        }
    }
}
