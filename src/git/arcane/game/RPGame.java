package git.arcane.game;

import git.arcane.core.IGame;
import git.arcane.core.util.Log;
import git.arcane.core.screen.ScreenManager;
import git.arcane.game.screens.Game;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

/**
 * Basic sample implementation of the {@link IGame} interface.
 * This game will be simple RPG.
 */
public class RPGame implements IGame {
    private static final String GAME_NAME = "RPG Project";

    private ScreenManager screens;

    @Override
    public void initialize() {
        Log.GAME.info("Initializing!");
        glEnable(GL_DEPTH_TEST);

        screens = new ScreenManager();
        screens.addScreen("Game", new Game());
    }

    @Override
    public void dispose() {
        Log.GAME.info("Disposing!");
        screens.dispose();
    }

    @Override
    public void update(double dt) {
        screens.update(dt);
    }

    @Override
    public void render(double alpha) {
        screens.render(alpha);
    }

    public String getGameName() {
        return GAME_NAME;
    }
}
