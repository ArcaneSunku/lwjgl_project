package git.arcane.game.screens;

import git.arcane.core.graphics.rendering.Renderer;
import git.arcane.core.graphics.Shaders;
import git.arcane.core.graphics.Texture;
import git.arcane.core.graphics.cameras.OrthoCamera;
import git.arcane.core.graphics.rendering.Sprite;
import git.arcane.core.graphics.rendering.SpriteSheet;
import git.arcane.core.graphics.rendering.font.RenderFont;
import git.arcane.core.screen.Screen;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of a {@link Screen} that is meant to represent the Main Game.
 */
public class Game implements Screen {

    private Map<String, Texture> textures;
    private Renderer render;

    private OrthoCamera camera;
    private SpriteSheet tileSheet;
    private Sprite sprite, sprite2;
    private RenderFont font;

    @Override
    public void show() {
        final Shaders shaders = new Shaders("/shaders/scene.vert", "/shaders/scene.frag");
        final Texture tiles = Texture.loadTexture("/textures/tiles.png");
        final Texture dirt = Texture.loadTexture("/textures/dirt.png");

        font = new RenderFont("/fonts/vcr.ttf", 16);

        camera = new OrthoCamera(16, 9);
        camera.setMoveSpeed(0.5f);

        if(textures == null)
            textures = new HashMap<>();

        if(render == null)
            render = new Renderer(shaders, camera);

        textures.putIfAbsent("tiles", tiles);
        textures.putIfAbsent("dirt", dirt);

        tileSheet = new SpriteSheet(textures.get("tiles"));
        sprite = tileSheet.getSprite(0, 0, 16, 16);
        sprite2 = tileSheet.getSprite(16, 0, 16, 16);

        sprite.setPosition(sprite.getPosition().x - 1.0f, sprite.getPosition().y);
        sprite2.setPosition(sprite2.getPosition().x + 1.0f, sprite2.getPosition().y);

        camera.setZoom(0.2f);
    }

    @Override
    public void hide() {
        sprite.dispose();
        sprite2.dispose();

        for(Texture texture : textures.values())
            texture.delete();
    }

    @Override
    public void dispose() {
        render.dispose();
    }

    @Override
    public void update(double dt) {
        render.update();
    }

    @Override
    public void render(double alpha) {
        font.drawText(render, "Hello, World! 123", 0, 0, 0.25f);

        sprite.draw(render);
        sprite2.draw(render);
    }

}
