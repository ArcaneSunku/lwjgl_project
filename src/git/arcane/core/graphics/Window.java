package git.arcane.core.graphics;

import git.arcane.core.GameSettings;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class.getSimpleName());

    private static class WindowData {
        public long GLFWwindow = -1L;

        public String Title = "Window";
        public int Width = 100, Height = 100;
        boolean Resizable = false, Fullscreen = false, VSync = true;
    }

    private final WindowData m_Data;

    public Window() {
        m_Data = new WindowData();
        initialize();
    }

    public Window(String title, int width, int height) {
        m_Data = new WindowData();
        m_Data.Title = title;

        m_Data.Width = width;
        m_Data.Height = height;
        initialize();
    }

    public Window(String title, int width, int height, boolean vSync, boolean resizable) {
        m_Data = new WindowData();
        m_Data.Title = title;

        m_Data.Width = width;
        m_Data.Height = height;

        m_Data.VSync = vSync;
        m_Data.Resizable = resizable;
        initialize();
    }

    public Window(GameSettings setting) {
        this(setting.GameName, setting.WinWidth, setting.WinHeight, setting.EnableVSync, setting.EnableResize);
    }

    private void initialize() {
        String title = m_Data.Title;
        int width = m_Data.Width, height = m_Data.Height;
        boolean vSync = m_Data.VSync, resizable = m_Data.Resizable;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        m_Data.GLFWwindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(m_Data.GLFWwindow == -1L || m_Data.GLFWwindow == NULL) {
            LOGGER.error("Failed to create a Window!");
            throw new RuntimeException("Failed to create GLFWwindow!");
        }

        GLFWVidMode vid_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert(vid_mode != null);
        glfwSetWindowPos(m_Data.GLFWwindow, (vid_mode.width() - width) / 2, (vid_mode.height() - height) / 2);

        glfwMakeContextCurrent(m_Data.GLFWwindow);
        glfwSwapInterval(vSync ? 1 : 0);

        GL.createCapabilities();
    }

    public void show() {
        glfwShowWindow(m_Data.GLFWwindow);
    }

    public void update() {
        glfwSwapBuffers(m_Data.GLFWwindow);
        glfwPollEvents();
    }

    public void dispose() {
        if(m_Data.GLFWwindow != NULL && m_Data.GLFWwindow != -1L) {
            glfwDestroyWindow(m_Data.GLFWwindow);
            m_Data.GLFWwindow = -1L;
        }
    }

    public String getTitle() {
        return m_Data.Title;
    }

    public int getWidth() {
        return m_Data.Width;
    }

    public int getHeight() {
        return  m_Data.Height;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(m_Data.GLFWwindow);
    }

    public boolean isResizable() {
        return m_Data.Resizable;
    }

    public boolean isFullscreen() {
        return m_Data.Fullscreen;
    }

    public boolean vSyncEnabled() {
        return m_Data.VSync;
    }

}
