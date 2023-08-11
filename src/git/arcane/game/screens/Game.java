package git.arcane.game.screens;

import git.arcane.core.graphics.rendering.Renderer;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.graphics.rendering.Sprite;
import git.arcane.core.screen.Screen;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Game implements Screen {

    private Map<String, Texture> textures;
    private Renderer render;

    private OrthoCamera camera;
    private Sprite sprite;

    @Override
    public void show() {
        final Shaders shaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");
        if(textures == null)
            textures = new HashMap<>();

        if(render == null)
            render = new Renderer(shaders);

        camera = new OrthoCamera(16, 9);
        camera.setMoveSpeed(0.5f);

        final Texture tiles = new Texture("/textures/tiles.png");
        final Texture dirt = new Texture("/textures/dirt.png");

        textures.putIfAbsent("tiles", tiles);
        textures.putIfAbsent("dirt", dirt);

        sprite = new Sprite(textures.get("tiles"), 0, 0, 16, 16);
    }

    @Override
    public void hide() {
        for(Texture texture : textures.values())
            texture.dispose();

        sprite.dispose();
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
        sprite.render(camera, render);
    }

}
