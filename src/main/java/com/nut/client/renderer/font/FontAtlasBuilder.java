package com.nut.client.renderer.font;

import lombok.SneakyThrows;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class FontAtlasBuilder {

    public static final FontAtlasBuilder instance = new FontAtlasBuilder();

    public static HashMap<String, CustomFont> fonts = new HashMap<>();
    public static int textureId;
    private static final List<CustomFont> customFonts = new ArrayList<>();

    public FontAtlasBuilder addFont(String fontName, float fontSize, String characters, int spacing) {
        customFonts.add(new CustomFont(fontName, fontSize, characters, spacing));
        return this;
    }

    public void buildAtlas() {
        if (customFonts.size() == 0)
            throw new RuntimeException("Cannot build atlas without any custom fonts");
        createBitmap();
    }

    @SneakyThrows
    public void createBitmap() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        int width = 0;
        int height = 0;
        for (CustomFont customFont : customFonts) {
            Font font = customFont.font;
            graphics.setFont(font);
            FontMetrics metrics = graphics.getFontMetrics();
            height += metrics.getHeight();

            for (char character : customFont.characters.toCharArray()) {
                if (!font.canDisplay(character)) continue;

                int charWidth = metrics.charWidth(character);
                width += charWidth + customFont.spacing;
            }
        }
        height /= customFonts.size();
        graphics.dispose();

        int widthHeight = getMinimumBitmapSize((int) ((width * height) * 1.2));
        image = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setColor(Color.WHITE);

        int x = 0;
        int y = 0;
        int charHeight = 0;
        for (CustomFont customFont : customFonts) {
            x = 0;
            y += charHeight;
            Font font = customFont.font;
            graphics.setFont(font);
            FontMetrics metrics = graphics.getFontMetrics();
            charHeight = metrics.getHeight();
            for (char character : customFont.characters.toCharArray()) {
                if (!font.canDisplay(character)) continue;

                int charWidth = metrics.charWidth(character);
                if (character == 'j')
                    charWidth += metrics.getDescent();
                if (x + charWidth > widthHeight) {
                    x = 0;
                    y += metrics.getHeight();
                }
                customFont.charInfos[character] = new CharInfo(
                        metrics,
                        (float) x / widthHeight,
                        (float) y / widthHeight,
                        (float) charWidth / widthHeight,
                        (float) charHeight / widthHeight,
                        charWidth,
                        charHeight);
                if (character == 'j')
                    graphics.drawString(String.valueOf(character), x + metrics.getDescent(), y + metrics.getAscent());
                else
                    graphics.drawString(String.valueOf(character), x, y + metrics.getAscent());
                x += charWidth + customFont.spacing;
            }
            fonts.put(customFont.fontName, customFont);
        }
        graphics.dispose();
        createTexture(image);
    }

    @SneakyThrows
    private void createTexture(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight());

        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        buffer.flip();

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, image.getWidth(), image.getHeight(), 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
        buffer.clear();
    }

    private static int getMinimumBitmapSize(int surface) {
        int length = (int) Math.sqrt(surface);
        length |= length >> 1;
        length |= length >> 2;
        length |= length >> 4;
        length |= length >> 8;
        length |= length >> 16;
        return ++length;
    }
}
