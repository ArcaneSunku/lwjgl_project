package git.arcane.core.graphics;

import git.arcane.core.util.Log;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Reads shader data from shader paths we pass to it on creation.<br>
 * Allows us to bind our shader program and set unique uniform variables per program.
 */
public class Shaders {
    private static class ShaderData {
        private final Map<Integer, String> m_ShaderPath;

        public int ProgramID = -1;
        public int VertexID = -1, FragmentID = -1;

        protected ShaderData(String vertPath, String fragPath) {
            m_ShaderPath = new HashMap<>();

            m_ShaderPath.put(0, vertPath);
            m_ShaderPath.put(1, fragPath);
        }

        public String VertexPath()   { return m_ShaderPath.get(0); }
        public String FragmentPath() { return m_ShaderPath.get(1); }
    }

    private final ShaderData m_Data;
    private final Map<String, Integer> m_UniformMap;

    public Shaders(String vertPath, String fragPath) {
        m_Data = new ShaderData(vertPath, fragPath);
        m_UniformMap = new HashMap<>();

        initialize();
    }

    private void initialize() {
        m_Data.VertexID = createShader(m_Data.VertexPath(), GL_VERTEX_SHADER);
        m_Data.FragmentID = createShader(m_Data.FragmentPath(), GL_FRAGMENT_SHADER);

        m_Data.ProgramID = createProgram(m_Data.VertexID, m_Data.FragmentID);
    }

    public void bind() {
        glUseProgram(m_Data.ProgramID);
    }

    public void dispose() {
        glDeleteProgram(m_Data.ProgramID);

        glDeleteShader(m_Data.VertexID);
        glDeleteShader(m_Data.FragmentID);
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniformBool(String name, boolean value) {
        setUniform1i(name, value ? GL_TRUE : GL_FALSE);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform2f(String name, float val1, float val2) {
        glUniform2f(getUniformLocation(name), val1, val2);
    }

    public void setUniform3f(String name, float val1, float val2, float val3) {
        glUniform3f(getUniformLocation(name), val1, val2, val3);
    }

    public void setUniformMat4(String name, Matrix4f value) {
        try(MemoryStack stack = stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(getUniformLocation(name), false, buffer);
        }
    }

    public int createUniform(String name) {
        int location = glGetUniformLocation(m_Data.ProgramID, name);
        if(location < 0) {
            Log.RENDER.error("Failed to create uniform!");
            System.exit(-1);
        }

        m_UniformMap.put(name, location);
        return getUniformLocation(name);
    }

    private int getUniformLocation(String name) {
        if(!m_UniformMap.containsKey(name))
            return createUniform(name);

        return m_UniformMap.get(name);
    }

    private int createProgram(int vertex, int fragment) {
        int program = glCreateProgram();

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            Log.RENDER.error("Failed to link Shaders to our Program!\n{}", glGetProgramInfoLog(program, 1024));
            throw new RuntimeException("Failed to link Shaders!");
        }

        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE)
            Log.RENDER.warn("Problem validating Shaders!\n{}", glGetProgramInfoLog(program, 1024));

        glDetachShader(program, vertex);
        glDetachShader(program, fragment);

        return program;
    }

    private int createShader(String path, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, readSrcFromFile(path));

        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            Log.RENDER.error("Failed to compile shader: \n{}", glGetShaderInfoLog(shader, 1024));
            throw new RuntimeException("Failed to load OpenGL Shader");
        }

        return shader;
    }

    private String readSrcFromFile(String shaderPath) {
        String result;
        try (InputStream is = Shaders.class.getResourceAsStream(shaderPath)) {
            if(is == null) {
                Log.RENDER.error("Failed to read Source from [{}]!", shaderPath);
                throw new IOException("Failed to read Shader Source!");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();
            result = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
