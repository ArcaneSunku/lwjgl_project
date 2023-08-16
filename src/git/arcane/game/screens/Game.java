package git.arcane.game.screens;

import git.arcane.core.graphics.rendering.Renderer;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.graphics.rendering.Sprite;
import git.arcane.core.screen.Screen;
import git.arcane.core.util.Input;
import git.arcane.core.util.Log;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A simple implementation of a {@link Screen} that is meant to represent the Main Game.
 */
public class Game implements Screen {

    private Map<String, Texture> textures;
    private Renderer render;

    private OrthoCamera camera;
    private Sprite sprite, sprite2;

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
        sprite2 = new Sprite(textures.get("tiles"), 16, 0, 16, 16);

        sprite.setPosition(sprite.getPosition().x - 1.0f, sprite.getPosition().y);
        sprite2.setPosition(sprite2.getPosition().x + 1.0f, sprite2.getPosition().y);
    }

    @Override
    public void hide() {
        for(Texture texture : textures.values())
            texture.dispose();

        sprite.dispose();
        sprite2.dispose();
    }

    @Override
    public void dispose() {
        render.dispose();
    }

    @Override
    public void update(double dt) {
        camera.update();

        if(Input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            Log.GAME.info("Left Click!");
        }


        Vector2f camPos = camera.getPosition();

        if(Input.isKeyDown(GLFW_KEY_LEFT))
            camPos.x -= (float) dt * camera.getMoveSpeed();
        else if(Input.isKeyDown(GLFW_KEY_RIGHT))
            camPos.x += (float) dt * camera.getMoveSpeed();

        if(Input.isKeyDown(GLFW_KEY_UP))
            camPos.y += (float) dt * camera.getMoveSpeed();
        else if(Input.isKeyDown(GLFW_KEY_DOWN))
            camPos.y -= (float) dt * camera.getMoveSpeed();

        camera.setPosition(camPos);

        Vector3f position = sprite.getPosition();

        if(Input.isKeyDown(GLFW_KEY_A))
            position.x -= (float) dt * 0.5f;
        else if(Input.isKeyDown(GLFW_KEY_D))
            position.x += (float) dt * 0.5f;

        if(Input.isKeyDown(GLFW_KEY_W))
            position.y += (float) dt * 0.5f;
        else if(Input.isKeyDown(GLFW_KEY_S))
            position.y -= (float) dt * 0.5f;

        sprite.setPosition(position.x, position.y);
    }

    @Override
    public void render(double alpha) {
        sprite.render(camera, render);
        sprite2.render(camera, render);
    }

}
