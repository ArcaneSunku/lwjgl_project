package git.arcane.core.screen;

/**
 * Interface for defining the basic methods a Screen will need.
 */
public interface Screen {

    /**
     * Called when a Screen is set as the active one. <br>Shows the Screen it is called on.
     */
    void show();

    /**
     * Called when a new Screen is set to active. <br>Hides the Screen it is called on.
     */
    void hide();

    /**
     * Called when we want to dispose of resources our Screen might have allocated.
     */
    void dispose();

    /**
     * This is called to update the Screen. <br>This will handle all the logic of the Screen it is called on.
     * @param dt The time in nanoseconds since the last update.
     */
    void update(double dt);

    /**
     * This is called to render the Screen. <br>This will handle all rendering logic for the Screen it is called on.
     * @param alpha The interpolation factor for smooth rendering.
     */
    void render(double alpha);

}
