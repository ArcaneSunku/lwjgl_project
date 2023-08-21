package git.arcane.core.graphics;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

/**
 * Handles the data needed for anything to be rendered to our screen.
 * We use two sets of floats to create a valid mesh we'll eventually render.
 */
public class Mesh {

    private static class MeshData {
        public int vao = -1, vbo = -1, ebo = -1;

        public float[] vertices;
        public int[] indices;
    }

    private final MeshData m_Data;
    private boolean m_Loaded;

    public Mesh(float[] vertices, int[] indices) {
        m_Data = new MeshData();
        m_Loaded = false;

        m_Data.vertices = vertices;
        m_Data.indices = indices;

        create();
    }

    public void create() {
        if(m_Loaded) {
            dispose();
            m_Loaded = false;
        }

        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            m_Data.vao = glCreateVertexArrays();
            glBindVertexArray(m_Data.vao);

            m_Data.vbo = glCreateBuffers();
            vertexBuffer = MemoryUtil.memAllocFloat(m_Data.vertices.length);
            vertexBuffer.put(0, m_Data.vertices);

            glBindBuffer(GL_ARRAY_BUFFER, m_Data.vbo);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);

            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);

            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);

            m_Data.ebo = glCreateBuffers();
            indexBuffer = MemoryUtil.memAllocInt(m_Data.indices.length);
            indexBuffer.put(0, m_Data.indices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_Data.ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
        } finally {
            if(vertexBuffer != null)
                MemoryUtil.memFree(vertexBuffer);

            if(indexBuffer != null)
                MemoryUtil.memFree(indexBuffer);

            m_Loaded = true;
        }
    }

    public void dispose() {
        glDeleteVertexArrays(m_Data.vao);
        glDeleteBuffers(m_Data.vbo);
        glDeleteBuffers(m_Data.ebo);

        m_Data.vertices = null;
        m_Data.indices = null;
    }

    public void setVertices(float[] vertices) {
        m_Data.vertices = vertices;
    }

    public void setIndices(int[] indices) {
        m_Data.indices = indices;
    }

    public boolean isLoaded() {
        return m_Loaded;
    }

    public int getVAO() {
        return m_Data.vao;
    }

    public int getVertexCount() {
        return m_Data.indices.length;
    }

    public float[] getVertices() {
        return m_Data.vertices;
    }

    public int[] getIndices() {
        return m_Data.indices;
    }

    public static Mesh CreateMesh() {
        return CreateMesh(new Vector3f(1.0f));
    }

    public static Mesh CreateMesh(Vector3f color) {


        float[] vertices = { // x, y, z, r, g, b, u, v
                -1.0f, -1.0f, 0.0f, color.x, color.y, color.z, 0.0f, 1.0f, // Bottom Left
                -1.0f,  1.0f, 0.0f, color.x, color.y, color.z, 0.0f, 0.0f, // Top Left
                 1.0f,  1.0f, 0.0f, color.x, color.y, color.z, 1.0f, 0.0f, // Top Right
                 1.0f, -1.0f, 0.0f, color.x, color.y, color.z, 1.0f, 1.0f, // Bottom Right
        };

        int[] indices = {
                0, 1, 2,
                3, 0, 2
        };

        return new Mesh(vertices, indices);
    }

}
