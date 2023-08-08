package git.arcane.core.graphics.cameras;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class OrthoCamera extends Camera {

    private final Vector2f m_Position;
    private final Vector2f m_Dimension;

    private float m_AspectRatio;

    public OrthoCamera(int width, int height) {
        super();

        m_Position = new Vector2f(0.0f, 0.0f);
        m_Dimension = new Vector2f(width, height);
        m_AspectRatio = m_Dimension.x / m_Dimension.y;

        m_Data.Projection = new Matrix4f();
        m_Data.Projection.ortho(-m_Dimension.x / m_AspectRatio, m_Dimension.x / m_AspectRatio, -m_Dimension.y / m_AspectRatio, m_Dimension.y / m_AspectRatio, -1.0f, 1.0f);
        update();
    }

    @Override
    public void update() {
        m_Data.View.identity();
        m_Data.View.translation(-m_Position.x, -m_Position.y, 0.0f)
                .scaling(1.0f);

        m_Data.Combined = new Matrix4f();
        m_Data.Combined.set(m_Data.Projection).mul(m_Data.View);
    }

    @Override
    public void resize(int width, int height) {
        m_Dimension.set(width, height);
        m_AspectRatio = m_Dimension.x / m_Dimension.y;

        m_Data.Projection.identity();
        m_Data.Projection.setOrtho(-m_Dimension.x / m_AspectRatio, m_Dimension.x / m_AspectRatio, -m_Dimension.y / m_AspectRatio, m_Dimension.y / m_AspectRatio, -1.0f, 1.0f);
        update();
    }

    public float getAspectRatio() {
        return m_AspectRatio;
    }

    public Vector2f getPosition() {
        return m_Position;
    }

    public Vector2f getDimension() {
        return m_Dimension;
    }

}
