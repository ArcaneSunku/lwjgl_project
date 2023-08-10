package git.arcane.game.screens;

import git.arcane.core.graphics.rendering.Renderer;
import git.arcane.core.graphics.Mesh;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.screen.Screen;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Game implements Screen {

    private Renderer render;

    private OrthoCamera camera;
    private Texture texture;
    private Mesh mesh;

    @Override
    public void show() {
        final Shaders shaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");
        if(render == null)
            render = new Renderer(shaders);

        camera = new OrthoCamera(16, 9);
        camera.setMoveSpeed(0.5f);

        mesh = Mesh.CreateMesh();
        texture = new Texture("/textures/dirt.png");
    }

    @Override
    public void hide() {
        texture.dispose();
        mesh.dispose();
    }

    @Override
    public void dispose() {
        render.dispose();
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

}
