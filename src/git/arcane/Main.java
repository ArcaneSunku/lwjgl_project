package git.arcane;

import git.arcane.core.LWJGLProject;
import git.arcane.core.GameSettings;
import git.arcane.game.RPGame;

public class Main {

    public static void main(String[] args) {
        final RPGame game = new RPGame();
        final GameSettings settings = new GameSettings();

        settings.GameName = game.getGameName();
        settings.WinWidth = 800;
        settings.WinHeight = 460;

        final LWJGLProject project = new LWJGLProject(game, settings);
        project.start();
    }

}
