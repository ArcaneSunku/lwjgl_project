package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL20.glBlendEquationSeparate;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * A basic renderer that allows us to render things to the screen <br>
 * in a "quick and dirty" manner. Will see lots of change.
 */
public class Renderer {

    private final Shaders m_Shader;
    private Camera m_Camera;

    public Renderer(Shaders shaders, Camera camera) {
        m_Shader = shaders;
        m_Camera = camera;

        initialize();
    }

    private void initialize() {
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        m_Shader.createUniform("u_MVPmatrix");
        m_Shader.createUniform("u_Sampler");
        m_Shader.createUniform("u_Textured");

        update();
    }

    public void update() {
        m_Camera.update();
    }

    public void dispose() {
        m_Shader.dispose();
    }

    public void setCamera(Camera camera) {
        m_Camera = camera;
        update();
    }

    public void renderMesh(Mesh mesh) {
        renderMesh(new Vector3f(0.0f), new Vector2f(1.0f), mesh);
    }

    public void renderMesh(Camera camera, Mesh mesh, Texture texture) {
        renderMesh(new Vector3f(0.0f), new Vector2f(1.0f), mesh, texture);
    }

    public void renderMesh(Vector3f position, Vector2f scale, Mesh mesh) {
        renderMesh(position, scale, mesh, null);
    }

    public void renderMesh(Vector3f position, Vector2f scale, Mesh mesh, Texture texture) {
        if(m_Shader == null || m_Camera == null)
            return;

        boolean textured = texture != null;
        Matrix4f model = new Matrix4f();
        model.translate(position).scale(scale.x, scale.y, 1.0f);

        m_Shader.bind();
        m_Shader.setUniformMat4("u_MVPmatrix", new Matrix4f(m_Camera.getCombinedMatrix()).mul(model));

        m_Shader.setUniformBool("u_Textured", textured);
        if(textured) {
            texture.bind(0);
            m_Shader.setUniform1i("u_Sampler", 0);
        }

        glBindVertexArray(mesh.getVAO());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

}
