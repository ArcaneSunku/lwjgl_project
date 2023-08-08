package git.arcane.game;

import git.arcane.core.IGame;
import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import git.arcane.core.util.Log;
import git.arcane.core.graphics.Shaders;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;

public class RPGame implements IGame {
    private static final String GAME_NAME = "RPG Project";

    private Shaders sceneShaders;
    private Texture texture;
    private Mesh mesh;

    @Override
    public void initialize() {
        Log.GAME.info("Initializing!");
        sceneShaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");
        texture = new Texture("/textures/dirt.png");

        float[] vertices = { // x, y, z, r, g, b, u, v
                -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left
                -0.5f,  0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, // Top Left
                 0.5f,  0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, // Top Right
                 0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, // Bottom Right
        };

        int[] indices = {
                0, 1, 2,
                3, 0, 2
        };

        mesh = new Mesh(vertices, indices);

        sceneShaders.createUniform("u_Sampler");
        sceneShaders.createUniform("u_Textured");
    }

    @Override
    public void dispose() {
        Log.GAME.info("Disposing!");

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);

        sceneShaders.dispose();
        texture.dispose();
        mesh.dispose();
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(double alpha) {
        sceneShaders.bind();

        sceneShaders.setUniformBool("u_Textured", true);
        texture.bind(0);
        sceneShaders.setUniform1i("u_Sampler", 0);

        glBindVertexArray(mesh.getVAO());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
    }

    public String getGameName() {
        return GAME_NAME;
    }
}
