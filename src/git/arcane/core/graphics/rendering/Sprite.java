package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Sprite {

    private Mesh m_Mesh;
    private Vector3f m_Tint;
    private Texture m_Texture;

    public Sprite(Texture texture, Vector3f tint, int x, int y, int width, int height) {
        m_Mesh = CreateSpriteMesh(texture, tint, x, y, width, height);
        m_Tint = tint;
        m_Texture = texture;
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

    public void render(Vector3f position, float scale, OrthoCamera camera, Renderer render) {
        render.renderMesh(position, scale, camera, m_Mesh, m_Texture);
    }

    public void render(Vector3f position, OrthoCamera camera, Renderer render) {
        render(position, 1.0f, camera, render);
    }

    public void setTint(Vector3f color) {
        m_Tint = new Vector3f(color);
    }

    public void setTexture(Texture texture) {
        m_Texture = texture;
    }

    public void setSubImage(int x, int y, int width, int height) {
        m_Mesh = CreateSpriteMesh(m_Texture, m_Tint, x, y, width, height);
    }

    public Vector3f getTint() {
        return m_Tint;
    }

    public Texture getTexture() {
        return m_Texture;
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
