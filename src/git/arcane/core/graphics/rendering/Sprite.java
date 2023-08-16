package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Handles a render-able Mesh with a Texture. This allows us to draw a texture super quick. <br>
 * Sprite Sheet support is currently supported.
 */
public class Sprite {

    private static class SpriteData {
        public Mesh Mesh;
        public Vector3f Tint;
        public Texture Texture;
    }

    private final SpriteData m_Data;
    private final Vector3f m_Position;
    private final Vector2f m_Size;

    public Sprite(Texture texture, Vector3f tint, int x, int y, int width, int height) {
        m_Data = new SpriteData();
        m_Position = new Vector3f(0.0f);
        m_Size = new Vector2f(1.0f);

        m_Data.Mesh = CreateSpriteMesh(texture, tint, x, y, width, height);
        m_Data.Tint = tint;
        m_Data.Texture = texture;
    }

    public Sprite(Texture texture, int x, int y, int width, int height) {
        this(texture, new Vector3f(1.0f), x, y, width, height);
    }

    public Sprite(Texture texture, Vector3f tint) {
        this(texture, tint, 0, 0, texture.getWidth(), texture.getHeight());
    }

    public Sprite(Texture texture) {
        this(texture, new Vector3f(1.0f));
    }

    public void render(OrthoCamera camera, Renderer render) {
        render.renderMesh(m_Position, m_Size, camera, m_Data.Mesh, m_Data.Texture);
    }

    public void dispose() {
        m_Data.Mesh.dispose();
    }

    public void setPosition(float x, float y) {
        m_Position.set(x, y, m_Position.z);
    }

    public void setSize(float xSize, float ySize) {
        m_Size.set(xSize, ySize);
    }

    public void setTint(Vector3f color) {
        m_Data.Tint.set(color);
    }

    public void setTexture(Texture texture) {
        m_Data.Texture = texture;
    }

    public void setSubImage(int x, int y, int width, int height) {
        m_Data.Mesh = CreateSpriteMesh(m_Data.Texture, m_Data.Tint, x, y, width, height);
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Vector2f getSize() {
        return m_Size;
    }

    public Vector3f getTint() {
        return m_Data.Tint;
    }

    public Texture getTexture() {
        return m_Data.Texture;
    }

    private static Mesh CreateSpriteMesh(Texture texture, Vector3f color, float x, float y, float width, float height) {
        Vector2f topLeft = new Vector2f(), topRight = new Vector2f();
        Vector2f botLeft = new Vector2f(), botRight = new Vector2f();

        if((x <= 0 && y <= 0) && (width >= texture.getWidth() && height >= texture.getHeight())) {
            topLeft.set( 0.0f, 0.0f);
            topRight.set(1.0f, 0.0f);
            botLeft.set( 0.0f, 1.0f);
            botRight.set(1.0f, 1.0f);
        } else {
            topLeft.set(x / texture.getWidth(), (y + height) / texture.getHeight());
            topRight.set((x + width) / texture.getWidth(), (y + height) / texture.getHeight());
            botLeft.set(x / texture.getWidth(), y / texture.getHeight());
            botRight.set((x + width) / texture.getWidth(), y / texture.getHeight());
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
