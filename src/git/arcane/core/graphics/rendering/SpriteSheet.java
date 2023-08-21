package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

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

    public SpriteSheet(Texture texture) {
        m_Data = new SpriteSheetData(texture);
    }

    public Sprite getSprite(int xOffset, int yOffset, int spriteWidth, int spriteHeight) {
        return getSprite(new Vector3f(1.0f), xOffset, yOffset, spriteWidth, spriteHeight);
    }

    public Sprite getSprite(Vector3f color, int xOffset, int yOffset, int spriteWidth, int spriteHeight) {
        return new Sprite(m_Data.sheetTexture, CreateSpriteMesh(color, xOffset, yOffset, spriteWidth, spriteHeight), new Vector3f(1.0f));
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
