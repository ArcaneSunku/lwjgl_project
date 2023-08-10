package git.arcane.game;

import git.arcane.core.IGame;
import git.arcane.core.Renderer;
import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.util.Log;
import git.arcane.core.graphics.Shaders;
import org.joml.Vector2f;
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

    private Renderer render;

    private OrthoCamera camera;
    private Texture texture;
    private Mesh mesh;

    @Override
    public void initialize() {
        Log.GAME.info("Initializing!");
        glEnable(GL_DEPTH_TEST);

        final Shaders shaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");
        render = new Renderer(shaders);

        camera = new OrthoCamera(16, 10);
        camera.setMoveSpeed(0.5f);

        mesh = Mesh.CreateMesh();
        texture = new Texture("/textures/dirt.png");
    }

    @Override
    public void dispose() {
        Log.GAME.info("Disposing!");

        render.dispose();
        texture.dispose();
        mesh.dispose();
    }

    @Override
    public void update(double dt) {
        camera.update();

        Vector2f position = camera.getPosition();
        position.x -= (float) dt * camera.getMoveSpeed();

        camera.setPosition(position);
    }

    @Override
    public void render(double alpha) {
        render.renderMesh(new Vector3f(-2.0f, 2.0f, 0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(0f, 2.0f, 0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(2.0f, 2.0f, 0f), 1.0f, camera, mesh, texture);

        render.renderMesh(new Vector3f(-2.0f, 0.0f, 0.0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(2.0f, 0.0f, 0.0f), 1.0f, camera, mesh, texture);

        render.renderMesh(new Vector3f(-2.0f, -2.0f, 0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(0f, -2.0f, 0f), 1.0f, camera, mesh, texture);
        render.renderMesh(new Vector3f(2.0f, -2.0f, 0f), 1.0f, camera, mesh, texture);
    }

    public String getGameName() {
        return GAME_NAME;
    }
}
