package git.arcane.game;

import git.arcane.core.IGame;
import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.util.Log;
import git.arcane.core.graphics.Shaders;
import org.joml.Vector3f;
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
    private OrthoCamera camera;
    private Texture texture;
    private Mesh mesh;

    @Override
    public void initialize() {
        Log.GAME.info("Initializing!");
        glEnable(GL_DEPTH_TEST);

        camera = new OrthoCamera(16, 10);

        mesh = Mesh.CreateMesh();
        texture = new Texture("/textures/dirt.png");
        sceneShaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");

        sceneShaders.createUniform("u_PVMatrix");
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
        camera.update();
    }

    @Override
    public void render(double alpha) {
        sceneShaders.bind();

        sceneShaders.setUniform1i("u_Sampler", 0);
        sceneShaders.setUniformBool("u_Textured", true);
        texture.bind(0);

        sceneShaders.setUniformMat4("u_PVMatrix", camera.getCombinedMatrix());

        glBindVertexArray(mesh.getVAO());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
    }

    public String getGameName() {
        return GAME_NAME;
    }
}
