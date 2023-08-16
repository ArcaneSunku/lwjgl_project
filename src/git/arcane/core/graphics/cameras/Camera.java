package git.arcane.core.graphics.cameras;

import org.joml.Matrix4f;

/**
 * An abstract class that is meant to encapsulate the basic functionalities of any Camera.<br>
 * This should, in theory, allow us to create all sorts of cameras by simply extending this class.
 */
public abstract class Camera {
    protected static class CameraData {
        public Matrix4f Combined;
        public Matrix4f View = new Matrix4f();
        public Matrix4f Projection = new Matrix4f();

        public float MoveSpeed = 2.5f;
    }

    protected final CameraData m_Data;

    protected Camera() {
        m_Data = new CameraData();
    }

    public abstract void update();
    public abstract void resize(int width, int height);

    public void setMoveSpeed(float moveSpeed) {
        m_Data.MoveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return m_Data.MoveSpeed;
    }
    public Matrix4f getViewMatrix() {
        return m_Data.View;
    }

    public Matrix4f getProjectionMatrix() {
        return m_Data.Projection;
    }

    public Matrix4f getCombinedMatrix() {
        return m_Data.Combined;
    }

}
