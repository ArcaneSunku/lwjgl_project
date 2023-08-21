package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Creates {@link Sprite}s with the data needed for a Mesh to display a specific portion of a specified {@link Texture}.
 */
public class SpriteSheet {

    private static class SpriteSheetData {
        public Texture sheetTexture;
        public int sheetWidth, sheetHeight;

        private SpriteSheetData(Texture texture) {
            sheetTexture = texture;
            sheetWidth   = texture.getWidth();
            sheetHeight  = texture.getHeight();
        }
    }

    private final SpriteSheetData m_Data;

    /**
     * Creates a SpriteSheet using the given {@link Texture}.
     * @param texture the texture that will be used to create {@link Sprite}s.
     */
    public SpriteSheet(Texture texture) {
        m_Data = new SpriteSheetData(texture);
    }

    /**
     * Creates a {@link Sprite} that will render a specific area from the {@link Texture} assigned to the SpriteSheet.<br>
     * The tint is defaulted to White.
     * @param xOffset the xOffset (in pixels) where the Sprite section is.
     * @param yOffset the yOffset (in pixels) where the Sprite section is.
     * @param spriteWidth the width (in pixels) of the Sprite section.
     * @param spriteHeight the height (in pixels) of the Sprite section.
     * @return the {@link Sprite} created using the passed in data.
     */
    public Sprite getSprite(int xOffset, int yOffset, int spriteWidth, int spriteHeight) {
        return getSprite(new Vector3f(1.0f), xOffset, yOffset, spriteWidth, spriteHeight);
    }

    /**
     * Creates a {@link Sprite} w/ the position, size, and color data of the passed in Sprite.<br>
     * Then we create the Sprite in the normal way. See the main <i>getSprite</i> method.
     * @param sprite the Sprite we'll pull the position, size, and color data from.
     * @param xOffset the xOffset (in pixels) where the Sprite section is.
     * @param yOffset the yOffset (in pixels) where the Sprite section is.
     * @param spriteWidth the width (in pixels) of the Sprite section.
     * @param spriteHeight the height (in pixels) of the Sprite section.
     * @return the {@link Sprite} created using the passed in data.
     */
    public Sprite getSprite(Sprite sprite, int xOffset, int yOffset, int spriteWidth, int spriteHeight) {
        final Vector3f color = new Vector3f(sprite.getTint());
        final Vector3f position = new Vector3f(sprite.getPosition());
        final Vector2f size = new Vector2f(sprite.getSize());

        final Mesh mesh = CreateSpriteMesh(color, xOffset, yOffset, spriteWidth, spriteHeight);
        final Sprite result = new Sprite(m_Data.sheetTexture, mesh, color);
        result.setPosition(position.x, position.y);
        result.setSize(size.x, size.y);

        return result;
    }

    /**
     * Creates a {@link Sprite} that will render a specific area from the {@link Texture} assigned to the SpriteSheet.<br>
     *
     * @param color the color we want the Sprite to be tinted (additive).
     * @param xOffset the xOffset (in pixels) where the Sprite section is.
     * @param yOffset the yOffset (in pixels) where the Sprite section is.
     * @param spriteWidth the width (in pixels) of the Sprite section.
     * @param spriteHeight the height (in pixels) of the Sprite section.
     * @return the {@link Sprite} created using the passed in data.
     */
    public Sprite getSprite(Vector3f color, int xOffset, int yOffset, int spriteWidth, int spriteHeight) {
        final Mesh mesh = CreateSpriteMesh(color, xOffset, yOffset, spriteWidth, spriteHeight);
        return new Sprite(m_Data.sheetTexture, mesh, new Vector3f(1.0f));
    }

    private Mesh CreateSpriteMesh(Vector3f color, float x, float y, float width, float height) {
        Vector2f topLeft = new Vector2f(), topRight = new Vector2f();
        Vector2f botLeft = new Vector2f(), botRight = new Vector2f();

        if((x <= 0 && y <= 0) && (width >= m_Data.sheetWidth && height >= m_Data.sheetHeight)) {
            topLeft.set( 0.0f, 0.0f);
            topRight.set(1.0f, 0.0f);
            botLeft.set( 0.0f, 1.0f);
            botRight.set(1.0f, 1.0f);
        } else {
            topLeft.set(x / m_Data.sheetWidth, (y + height) / m_Data.sheetHeight);
            topRight.set((x + width) / m_Data.sheetWidth, (y + height) / m_Data.sheetHeight);
            botLeft.set(x / m_Data.sheetWidth, y / m_Data.sheetHeight);
            botRight.set((x + width) / m_Data.sheetWidth, y / m_Data.sheetHeight);
        }

        float[] vertices = { // x, y, z, r, g, b, u, v
                -1.0f, -1.0f, 0.0f, color.x, color.y, color.z, botLeft.x,  botLeft.y,  // Bottom Left
                -1.0f,  1.0f, 0.0f, color.x, color.y, color.z, topLeft.x,  topLeft.y,  // Top Left
                 1.0f,  1.0f, 0.0f, color.x, color.y, color.z, topRight.x, topRight.y, // Top Right
                 1.0f, -1.0f, 0.0f, color.x, color.y, color.z, botRight.x, botRight.y, // Bottom Right
        };

        int[] indices = {
                0, 1, 2,
                3, 0, 2
        };

        return new Mesh(vertices, indices);
    }

}
