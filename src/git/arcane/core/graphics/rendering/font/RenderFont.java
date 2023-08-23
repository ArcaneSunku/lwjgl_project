package git.arcane.core.graphics.rendering.font;

import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.rendering.Renderer;
import git.arcane.core.graphics.rendering.Sprite;
import git.arcane.core.graphics.rendering.SpriteSheet;
import git.arcane.core.util.Log;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.*;

public class RenderFont {

    private final Map<Character, Glyph> m_Glyphs;
    private final SpriteSheet m_FontSheet;

    private Texture m_Texture;
    private int m_FontHeight;

    public RenderFont() {
        this(new Font(MONOSPACED, PLAIN, 16), true);
    }

    public RenderFont(boolean antiAlias) {
        this(new Font(MONOSPACED, PLAIN, 16), antiAlias);
    }

    public RenderFont(int size) {
        this(new Font(MONOSPACED, PLAIN, size), true);
    }

    public RenderFont(int size, boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    public RenderFont(String path, int size) {
        this(path, size, true);
    }

    public RenderFont(String path, int size, boolean antiAlias) {
        m_Glyphs = new HashMap<>();

        try(InputStream is = RenderFont.class.getResourceAsStream(path)) {
            if(is == null)
                throw new IOException("Failed to load file!");

            m_Texture = createFontTexture(Font.createFont(TRUETYPE_FONT, is).deriveFont(PLAIN, size), antiAlias);
        } catch (Exception e) {
            Log.RENDER.error(e.getMessage());
            System.exit(-1);
        }

        m_FontSheet = new SpriteSheet(m_Texture);
    }

    public RenderFont(Font font) {
        this(font, true);
    }

    public RenderFont(Font font, boolean antiAlias) {
        m_Glyphs = new HashMap<>();
        m_Texture = createFontTexture(font, antiAlias);
        m_FontSheet = new SpriteSheet(m_Texture);
    }

    public void drawText(Renderer renderer, CharSequence text, float x, float y, float padding, Vector3f color) {
        int textHeight = getHeight(text);

        float drawX = x, drawY = y;
        if(textHeight > m_FontHeight)
            drawY += textHeight - m_FontHeight;

        m_Texture.bind();
        for(int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY -= m_FontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r')
                continue;

            Glyph g = m_Glyphs.get(ch);
            Sprite cha = m_FontSheet.getSprite(color, g.x, g.y, g.width, g.height);
            cha.setPosition(drawX, drawY);
            cha.setLayer(0f);

            cha.draw(renderer);
            cha.dispose();
            drawX += g.width * padding;
        }
    }

    public void drawText(Renderer renderer, CharSequence text, float x, float y, float padding) {
        drawText(renderer, text, x, y, padding, new Vector3f(1.0f));
    }

    public void drawText(Renderer renderer, CharSequence text, float x, float y) {
        drawText(renderer, text, x, y, 1.0f, new Vector3f(1.0f));
    }

    public void dispose() {
        m_Texture.delete();
    }

    public int getWidth(CharSequence text) {
        int width = 0;
        int lineWidth = 0;

        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if(c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }

            if(c == '\r')
                continue;

            Glyph g = m_Glyphs.get(c);
            lineWidth += g.width;
        }

        width = Math.max(width, lineWidth);
        return width;
    }

    public int getHeight(CharSequence text) {
        int height = 0;
        int lineHeight = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r')
                continue;

            Glyph g = m_Glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }

        height += lineHeight;
        return height;
    }

    private Texture createFontTexture(Font font, boolean antiAlias) {
        int imageWidth = 0, imageHeight = 0;

        for(int i = 32; i < 256; i++) {
            if(i == 127) continue;

            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);
            if(ch == null)
                continue;

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        m_FontHeight = imageHeight;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;
        for(int i = 32; i < 256; i++) {
            if(i == 127) continue;

            char c = (char) i;
            BufferedImage charImage = createCharImage(font, c, antiAlias);
            if(charImage == null)
                continue;

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            Glyph ch = new Glyph(x, image.getHeight() - charHeight, charWidth, charHeight, 0f);
            g.drawImage(charImage, x, 0, null);

            x += ch.width;
            m_Glyphs.put(c, ch);
        }

        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        image = operation.filter(image, null);

        int width = image.getWidth(), height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        Texture fontTexture = Texture.createTexture(width, height, 4, buffer);
        MemoryUtil.memFree(buffer);
        return fontTexture;
    }

    private BufferedImage createCharImage(Font font, char c, boolean antiAlias) {
        BufferedImage image  = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if(antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if(charWidth == 0)
            return null;

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if(antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        g.setPaint(Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();

        return image;
    }

}
