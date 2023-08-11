package git.arcane.core.graphics.cameras;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthoCamera extends Camera {

    private final Vector2f m_Position;
    private final Vector2f m_Dimension;

    private float m_Zoom, m_AspectRatio;

    public OrthoCamera(int width, int height) {
        super();

        m_Position = new Vector2f(0.0f, 0.0f);
        m_Dimension = new Vector2f(width, height);

        m_Zoom = 1.0f;
        m_AspectRatio = m_Dimension.x / m_Dimension.y;

        m_Data.Projection = new Matrix4f();
        m_Data.Projection.ortho(-m_Dimension.x / m_AspectRatio, m_Dimension.x / m_AspectRatio, -m_Dimension.y / m_AspectRatio, m_Dimension.y / m_AspectRatio, -1.0f, 1.0f);
        update();
    }

    @Override
    public void update() {
        m_Data.View = new Matrix4f();
        m_Data.View.translate(-m_Position.x, -m_Position.y, 0.0f)
                .scale(m_Zoom);

        m_Data.Combined = new Matrix4f();
        m_Data.Combined.set(m_Data.Projection).mul(m_Data.View);
    }

    @Override
    public void resize(int width, int height) {
        m_Dimension.set(width, height);
        m_AspectRatio = m_Dimension.x / m_Dimension.y;

        m_Data.Projection = new Matrix4f();
        m_Data.Projection.ortho(-m_Dimension.x / m_AspectRatio, m_Dimension.x / m_AspectRatio, -m_Dimension.y / m_AspectRatio, m_Dimension.y / m_AspectRatio, -1.0f, 1.0f);
        update();
    }

    public void setZoom(float zoom) {
        m_Zoom = zoom;
    }

    public void setPosition(Vector2f position) {
        m_Position.set(position);
    }

    public void setDimension(Vector2f dimension) {
        m_Dimension.set(dimension);
    }

    public float getZoom() {
        return m_Zoom;
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
