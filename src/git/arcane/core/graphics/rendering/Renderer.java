package git.arcane.core.graphics.rendering;

import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * A basic renderer that allows us to render things to the screen <br>
 * in a "quick and dirty" manner. Will see lots of change.
 */
public class Renderer {

    private final Shaders m_Shader;

    public Renderer(Shaders shaders) {
        m_Shader = shaders;
        initialize();
    }

    private void initialize() {
        m_Shader.createUniform("u_MVPmatrix");
        m_Shader.createUniform("u_Sampler");
        m_Shader.createUniform("u_Textured");
    }

    public void dispose() {
        m_Shader.dispose();
    }

    public void renderMesh(Camera camera, Mesh mesh) {
        renderMesh(new Vector3f(0.0f), new Vector2f(1.0f), camera, mesh);
    }

    public void renderMesh(Camera camera, Mesh mesh, Texture texture) {
        renderMesh(new Vector3f(0.0f), new Vector2f(1.0f), camera, mesh, texture);
    }

    public void renderMesh(Vector3f position, Vector2f scale, Camera camera, Mesh mesh) {
        renderMesh(position, scale, camera, mesh, null);
    }

    public void renderMesh(Vector3f position, Vector2f scale, Camera camera, Mesh mesh, Texture texture) {
        boolean textured = texture != null;
        Matrix4f model = new Matrix4f();
        model.translate(position).scale(scale.x, scale.y, 1.0f);

        m_Shader.bind();
        m_Shader.setUniformMat4("u_MVPmatrix", new Matrix4f(camera.getCombinedMatrix()).mul(model));

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
