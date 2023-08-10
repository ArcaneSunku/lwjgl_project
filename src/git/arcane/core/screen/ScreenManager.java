package git.arcane.core.screen;

import git.arcane.core.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final Map<String, Screen> m_ScreenMap;
    private Screen m_Active;

    public ScreenManager() {
        m_ScreenMap = new HashMap<>();
    }

    public void update(double dt) {
        if(m_Active != null)
            m_Active.update(dt);
    }

    public void render(double alpha) {
        if(m_Active != null)
            m_Active.render(alpha);
    }

    public void dispose() {
        if(m_ScreenMap.isEmpty()) return;

        setActive("niL");
        for(Screen screen : m_ScreenMap.values())
            screen.dispose();
    }

    public void setActive(String sceneName) {
        if(sceneName.equalsIgnoreCase("niL") || sceneName.isEmpty())
        {
            if(m_Active != null) m_Active.hide();
            m_Active = null;
            return;
        }

        if(!m_ScreenMap.containsKey(sceneName)) {
            Log.GAME.warn("Scene [{}] couldn't be found!", sceneName);
            return;
        }

        Screen screen = m_ScreenMap.get(sceneName);
        if(m_Active != null) m_Active.hide();

        m_Active = screen;
        if(m_Active != null) m_Active.show();
    }

    public void addScreen(String name, Screen screen) {
        if(m_ScreenMap.containsKey(name)) {
            Log.GAME.warn("There is already a Scene [{}]!", name);
            return;
        }

        m_ScreenMap.put(name, screen);
    }

    public Screen getActive() {
        return m_Active;
    }

    public Screen getScreen(String name) {
        return m_ScreenMap.get(name);
    }

}
