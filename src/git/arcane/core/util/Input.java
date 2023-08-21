package git.arcane.core.util;

import git.arcane.core.graphics.Window;
import org.joml.Vector2f;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static Input ms_Instance = null;

    private final long m_WindowHandle;
    private final Map<Integer, Boolean> m_KeyMap, m_MouseButtonMap;
    private final Vector2f m_MousePos, m_MouseDelta;

    public Input(Window window) {
        m_WindowHandle = window.getGLFWwindow();

        m_KeyMap = new ConcurrentHashMap<>();
        m_MouseButtonMap = new ConcurrentHashMap<>();

        m_MousePos = new Vector2f(0.0f, 0.0f);
        m_MouseDelta = new Vector2f(0.0f, 0.0f);

        if(ms_Instance == null)
            ms_Instance = this;
    }

    public synchronized void initialize() {
        for(int i = GLFW_KEY_SPACE; i < GLFW_KEY_LAST; i++)
            m_KeyMap.put(i, false);

        for(int i = GLFW_MOUSE_BUTTON_1; i < GLFW_MOUSE_BUTTON_LAST; i++)
            m_MouseButtonMap.put(i, false);

        glfwSetKeyCallback(m_WindowHandle, Input::key_callback);
        glfwSetCursorPosCallback(m_WindowHandle, Input::cursor_pos_callback);
        glfwSetMouseButtonCallback(m_WindowHandle, Input::mouse_button_callback);
    }

    public static boolean isKeyDown(int key) {
        if(key == GLFW_KEY_UNKNOWN) {
            Log.CORE.error("The key [{}] is not a valid key!", key);
            return false;
        }

        return get().m_KeyMap.get(key);
    }

    public static boolean isMouseButtonDown(int button) {
        return get().m_MouseButtonMap.get(button);
    }

    public static Vector2f getMousePos() {
        return get().m_MousePos;
    }

    public static Vector2f getMouseDelta() {
        return get().m_MouseDelta;
    }

    private static Input get() {
        return ms_Instance;
    }

    private static void key_callback(long window, int key, int scancode, int action, int mods) {
        Input instance = get();
        instance.m_KeyMap.put(key, action != GLFW_RELEASE);
    }

    private static void cursor_pos_callback(long window, double x, double y) {
        Input instance = get();
        Vector2f last_pos = new Vector2f(instance.m_MousePos);
        Vector2f curr_pos = new Vector2f((float) x, (float) y);

        instance.m_MousePos.set(curr_pos);
        instance.m_MouseDelta.set(curr_pos).sub(last_pos);
    }

    private static void mouse_button_callback(long window, int button, int action, int mods) {
        Input instance = get();
        instance.m_MouseButtonMap.put(button, action != GLFW_RELEASE);
    }

}
