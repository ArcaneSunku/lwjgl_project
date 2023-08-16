package git.arcane.core.screen;

import git.arcane.core.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a {@link Map<>} of Screens with Strings for their names/keys.<br>
 * This class also manages an active {@link Screen} that will decide explicitly what is updated and rendered.
 */
public class ScreenManager {

    private final Map<String, Screen> m_ScreenMap;
    private Screen m_Active;

    public ScreenManager() {
        m_ScreenMap = new HashMap<>();
        m_Active = null;
    }

    /**
     * Updates the active {@link Screen}.
     * @param dt The time in nanoseconds since last update.
     */
    public void update(double dt) {
        if(m_Active != null)
            m_Active.update(dt);
    }

    /**
     * Renders the active {@link Screen}.
     * @param alpha The interpolation factor to render the Screen smoothly.
     */
    public void render(double alpha) {
        if(m_Active != null)
            m_Active.render(alpha);
    }

    /**
     * Cleans up all the resources created in all Screens managed by this class. <br>
     * We make sure the active {@link Screen} is no longer defined before disposing of <br>
     * all the managed Screens' resources.
     */
    public void dispose() {
        if(m_ScreenMap.isEmpty()) return;

        setActive("niL");
        for(Screen screen : m_ScreenMap.values())
            screen.dispose();
    }

    /**
     * Sets the active {@link Screen} we will use to update and render. <br>
     * If this is set to "niL" or the sceneName is empty, the active Screen will be hid and unset.
     * @param sceneName The name we'll check against our {@link Map<>} of Screens to decide which one will become active.
     */
    public void setActive(String sceneName) {
        // Checks if sceneName is "niL" or empty. These will be our "set to nothing" conditions.
        if(sceneName.equalsIgnoreCase("niL") || sceneName.isEmpty())
        {
            if(m_Active != null) m_Active.hide();
            m_Active = null;
            return;
        }

        // Duplication handling
        if(!m_ScreenMap.containsKey(sceneName)) {
            Log.GAME.warn("Screen [{}] couldn't be found!", sceneName);
            return;
        }

        Screen screen = m_ScreenMap.get(sceneName);
        if(m_Active != null) m_Active.hide();

        m_Active = screen;
        if(m_Active != null) m_Active.show();
    }

    /**
     * Adds a {@link Screen} to our {@link Map<>}, with the name we assign as its key.<br>
     * If there are no other Screens and the active Screen is undefined<br>
     * the Screen we add, will be treated as our active.
     * @param name The name that will be used to reference our {@link Screen}
     * @param screen The screen that we will add to our collection of Screens.
     */
    public void addScreen(String name, Screen screen) {
        // Makes sure the name we are assigning to this screen doesn't exist.
        // If it does, we assume that Screen exists as well.
        if(m_ScreenMap.containsKey(name)) {
            Log.GAME.warn("There is already a Screen [{}]!", name);
            return;
        }

        // Checks if this is the first Screen added. Also makes sure the active Screen is not defined
        // If these conditions are met, we set the first screen we add as the active Screen.
        if(m_ScreenMap.isEmpty() && m_Active == null) {
            m_ScreenMap.put(name, screen);
            setActive(name);
            return;
        }

        m_ScreenMap.put(name, screen);
    }

    /**
     * Receives the active {@link Screen}.
     * @return The active Screen, can be null
     */
    public Screen getActive() {
        return m_Active;
    }

    /**
     * Receives a {@link Screen} from our {@link Map<>} with the key that matches our parameter.
     * @param name The name we'll check our Map with.
     * @return The Screen, if any found in the Map given our name as the key.
     */
    public Screen getScreen(String name) {
        return m_ScreenMap.get(name);
    }

}
