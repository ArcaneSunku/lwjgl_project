package git.arcane.core.graphics;

import git.arcane.core.util.Log;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL45.glCreateTextures;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {

    private static class TextureData {
        public int RenderID;
        public ByteBuffer ImageData;

        public String FilePath;
        public int Width, Height;
        public int Channels;
    }

    private final TextureData m_Data;

    public Texture(String path) {
        m_Data = new TextureData();
        m_Data.FilePath = path;

        load();
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, m_Data.RenderID);
    }

    public void dispose() {
        glDeleteTextures(m_Data.RenderID);
        stbi_image_free(m_Data.ImageData);
    }

    private void load() {
        String path = m_Data.FilePath;

        try(MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            try(InputStream is = Texture.class.getResourceAsStream(path)) {
                if(is == null)
                    throw new IOException("Failed to read Texture: " + path);

                byte[] data = is.readAllBytes();
                ByteBuffer imageBuffer = BufferUtils.createByteBuffer(data.length);
                imageBuffer.put(0, data);
                m_Data.ImageData = stbi_load_from_memory(imageBuffer, w, h, channels, 0);
                if(m_Data.ImageData == null)
                    throw new IOException("Failed to read data in Texture: " + path);
            } catch (IOException e) {
                Log.RENDER.error("Failed to load Texture:\n{}", e.getMessage());
                System.exit(-1);
            }

            m_Data.Width = w.get(0);
            m_Data.Height = h.get(0);
            m_Data.Channels = channels.get(0);
        }

        m_Data.RenderID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_Data.RenderID);

        if(m_Data.Channels == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16, m_Data.Width, m_Data.Height, 0, GL_RGB, GL_UNSIGNED_BYTE, m_Data.ImageData);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        else if(m_Data.Channels == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, m_Data.Width, m_Data.Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, m_Data.ImageData);
            glGenerateMipmap(GL_TEXTURE_2D);
        }

        setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP);
        setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP);

        setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void setParameter(int pName, int param) {
        setParameter(GL_TEXTURE_2D, pName, param);
    }

    private void setParameter(int target, int pName, int param) {
        glTexParameteri(target, pName, param);
    }

}
