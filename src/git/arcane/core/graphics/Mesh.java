package git.arcane.core.graphics;

import org.lwjgl.system.MemoryStack;

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
import static org.lwjgl.system.MemoryStack.stackPush;

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
    }

    public int getVAO() {
        return m_Data.VAO;
    }

    public int getVertexCount() {
        return m_Data.Indices.length;
    }

    private void load() {
        try (MemoryStack stack = stackPush()) {
            m_Data.VAO = glCreateVertexArrays();
            glBindVertexArray(m_Data.VAO);

            m_Data.VBO = glCreateBuffers();
            FloatBuffer vertexBuffer = stack.mallocFloat(m_Data.Vertices.length);
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
            IntBuffer indexBuffer = stack.mallocInt(m_Data.Indices.length);
            indexBuffer.put(0, m_Data.Indices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_Data.EBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
        }
    }

}
