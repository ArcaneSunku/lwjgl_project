package git.arcane.core.graphics;

import org.joml.Vector2f;
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

public class Mesh {

    private static class MeshData {
        public int VAO, VBO, EBO;

        public float[] Vertices;
        public int[] Indices;
    }

    private final MeshData m_Data;

    public Mesh(float[] vertices, int[] indices) {
        m_Data = new MeshData();

        m_Data.Vertices = vertices;
        m_Data.Indices = indices;

        load();
    }

    public void dispose() {
        glDeleteVertexArrays(m_Data.VAO);
        glDeleteBuffers(m_Data.VBO);
        glDeleteBuffers(m_Data.EBO);

        m_Data.Vertices = null;
        m_Data.Indices = null;
    }

    public int getVAO() {
        return m_Data.VAO;
    }

    public int getVertexCount() {
        return m_Data.Indices.length;
    }

    private void load() {
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            m_Data.VAO = glCreateVertexArrays();
            glBindVertexArray(m_Data.VAO);

            m_Data.VBO = glCreateBuffers();
            vertexBuffer = MemoryUtil.memAllocFloat(m_Data.Vertices.length);
            vertexBuffer.put(0, m_Data.Vertices);

            glBindBuffer(GL_ARRAY_BUFFER, m_Data.VBO);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);

            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);

            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);

            m_Data.EBO = glCreateBuffers();
            indexBuffer = MemoryUtil.memAllocInt(m_Data.Indices.length);
            indexBuffer.put(0, m_Data.Indices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_Data.EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
        } finally {
            if(vertexBuffer != null)
                MemoryUtil.memFree(vertexBuffer);

            if(indexBuffer != null)
                MemoryUtil.memFree(indexBuffer);
        }
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
