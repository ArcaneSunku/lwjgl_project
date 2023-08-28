package git.arcane.core;

import git.arcane.core.util.Input;
import git.arcane.core.util.Log;
import git.arcane.core.graphics.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GLDebugMessageCallback.getMessage;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

public class LWJGLProject implements Runnable {

    private final IGame m_Game;
    private final GameSettings m_Settings;

    private Thread m_Thread;
    private Window m_Window;

    private volatile boolean m_Running;

    public LWJGLProject(IGame game, GameSettings settings) {
        m_Game = game;
        m_Settings = settings;
    }

    public synchronized void start() {
        if(m_Running) {
            Log.CORE.error("An instance is already running!");
            System.exit(-1);
        }

        Log.CORE.info("Starting Up!");

        m_Thread = new Thread(this, "Main_Thread");
        m_Running = true;

        m_Thread.start();
    }

    public synchronized void stop() {
        m_Game.dispose();

        Log.CORE.info("Shutting Down!");
        try {
            m_Window.dispose();
            m_Thread.join(1L);

            glfwTerminate();
            System.exit(0);
        } catch (Exception e) {
            Log.CORE.error(e.getMessage());
            System.exit(-1);
        }
    }

    private void gameLoop() {
        m_Window = new Window(m_Settings);
        final Input input = new Input(m_Window);

        input.initialize();
        m_Game.initialize();
        m_Window.show();

        double delta, accumulator = 0.0;
        double lastTime = glfwGetTime();
        boolean shouldRender = false;

        final double TARGET_UPS = 1.0 / 30.0, TARGET_FPS = 1.0 / 75.0;
        while(m_Running) {
            if(m_Window.shouldClose()) {
                m_Running = false;
                continue;
            }
            double now = glfwGetTime();
            delta = now - lastTime;
            lastTime = now;

            accumulator += delta;
            while(accumulator >= TARGET_UPS) {
                m_Game.update(accumulator);
                accumulator -= delta;
                shouldRender = true;
            }

            if(shouldRender) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                
                double alpha = accumulator / TARGET_FPS;
                m_Game.render(alpha);
                shouldRender = false;
            }

            m_Window.update();
        }
    }

    @Override
    public void run() {
        GLFWErrorCallback.create((error, description) -> Log.RENDER.error("OpenGL Error Occurred[{}]:\n{}", error, GLFWErrorCallback.getDescription(description))).set();

        if(!glfwInit()) {
            Log.CORE.error("Failed to initialize GLFW! Are you drivers up to date?");
            System.exit(-1);
        }

        gameLoop();
        stop();
    }
}
