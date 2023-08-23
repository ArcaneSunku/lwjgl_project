package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Handles a render-able Mesh with a Texture. This allows us to draw a texture super quick. <br>
 * Sprite Sheet support is currently supported.
 */
public class Sprite {

    private static class SpriteData {
        public Mesh mesh;
        public Vector3f tint;
        public Texture texture;

        private SpriteData(Texture texture) {
            this.texture = texture;
            tint = new Vector3f(1.0f);
        }
    }

    private final SpriteData m_Data;
    private final Vector3f m_Position;
    private final Vector2f m_Size;
    private float m_Layer;

    public Sprite(Texture texture, Mesh mesh, Vector3f tint) {
        m_Data = new SpriteData(texture);
        m_Data.tint = tint;
        m_Data.mesh = mesh;

        m_Position = new Vector3f(0.0f);
        m_Size = new Vector2f(1.0f);
        m_Layer = 0.0f;
    }

    public Sprite(Texture texture, Vector3f tint, int x, int y, int width, int height) {
        m_Data = new SpriteData(texture);
        m_Data.tint = tint;

        m_Position = new Vector3f(x, y, 0.0f);
        m_Size = new Vector2f(width, height);
        m_Layer = 0.0f;

        init();
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

    private void init() {
        Vector2f topLeft = new Vector2f(0.0f, 0.0f), topRight = new Vector2f(1.0f, 0.0f);
        Vector2f botLeft = new Vector2f(0.0f, 1.0f), botRight = new Vector2f(1.0f, 1.0f);

        Vector3f color = m_Data.tint;
        float[] vertices = new float[] {
                -1.0f, -1.0f, 0.0f, color.x, color.y, color.z, botLeft.x,  botLeft.y,  // Bottom Left
                -1.0f,  1.0f, 0.0f, color.x, color.y, color.z, topLeft.x,  topLeft.y,  // Top Left
                1.0f,  1.0f, 0.0f, color.x, color.y, color.z, topRight.x, topRight.y, // Top Right
                1.0f, -1.0f, 0.0f, color.x, color.y, color.z, botRight.x, botRight.y, // Bottom Right
        };

        int[] indices = new int[] {
                0, 1, 2,
                3, 0, 2
        };

        if(m_Data.mesh != null)
            m_Data.mesh.dispose();

        m_Data.mesh = new Mesh(vertices, indices);
    }

    private void reloadTint() {
        float[] vertices = m_Data.mesh.getVertices();
        int[] indices = m_Data.mesh.getIndices();

        vertices[3] = m_Data.tint.x;
        vertices[4] = m_Data.tint.y;
        vertices[5] = m_Data.tint.z;

        vertices[11] = m_Data.tint.x;
        vertices[12] = m_Data.tint.y;
        vertices[13] = m_Data.tint.z;

        vertices[19] = m_Data.tint.x;
        vertices[20] = m_Data.tint.y;
        vertices[21] = m_Data.tint.z;

        vertices[27] = m_Data.tint.x;
        vertices[28] = m_Data.tint.y;
        vertices[29] = m_Data.tint.z;

        m_Data.mesh.dispose();
        m_Data.mesh = new Mesh(vertices, indices);
    }

    public void draw(Renderer render) {
        render.renderMesh(m_Position, m_Size, m_Data.mesh, m_Data.texture);
    }

    public void dispose() {
        m_Data.mesh.dispose();
    }

    public void setPosition(float x, float y) {
        m_Position.set(x, y, m_Position.z);
    }

    public void setSize(float xSize, float ySize) {
        m_Size.set(xSize, ySize);
    }

    public void setLayer(float layer) {
        m_Layer = layer;
    }

    public void setTint(Vector3f color) {
        m_Data.tint.set(color);
        reloadTint();
    }

    public void setTexture(Texture texture) {
        m_Data.texture = texture;
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Vector2f getSize() {
        return m_Size;
    }

    public float getLayer() {
        return m_Layer;
    }

    public Vector3f getTint() {
        return m_Data.tint;
    }

    public Texture getTexture() {
        return m_Data.texture;
    }
}
