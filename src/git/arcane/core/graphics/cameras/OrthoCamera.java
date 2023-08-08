package git.arcane.core.graphics.cameras;

import org.joml.Vector2f;

public class OrthoCamera extends Camera {

    private final Vector2f m_Position;
    private final Vector2f m_Dimension;

    public OrthoCamera(int width, int height) {
        super();

        m_Position = new Vector2f(0f);
        m_Dimension = new Vector2f(width, height);
    }

    @Override
    public void update() {

    }

    @Override
    public void resize(int width, int height) {

    }

    public Vector2f getPosition() {
        return m_Position;
    }

    public Vector2f getDimension() {
        return m_Dimension;
    }

}
