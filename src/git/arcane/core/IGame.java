package git.arcane.core;

/**
 * Interface that defines the basic method our Game class might need.
 */
public interface IGame {

    /**
     * Initializes the Game's resources.
     */
    void initialize();

    /**
     * Disposes the Game's resources.
     */
    void dispose();

    /**
     * Updates the logical processes of our Game.
     * @param dt The time in nanoseconds since the last update.
     */
    void update(double dt);

    /**
     * Renders our Game to the screen so we can see.
     * @param alpha The interpolation factor for smooth rendering.
     */
    void render(double alpha);

}
