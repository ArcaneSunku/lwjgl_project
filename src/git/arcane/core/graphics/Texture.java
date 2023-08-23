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

/**
 * Loads an image into usable OpenGL information and helps us bind it so we can render it to the screen.
 */
public class Texture {

    private static class TextureData {
        public int RenderID;
        public ByteBuffer ImageData;

        public String Path;
        public int Width, Height;
        public int Channels;
    }

    private final TextureData m_Data;

    public Texture() {
        m_Data = new TextureData();
        m_Data.RenderID = glGenTextures();
    }

    public void bind() {
        bind(0);
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, m_Data.RenderID);
    }

    public void delete() {
        glDeleteTextures(m_Data.RenderID);
        stbi_image_free(m_Data.ImageData);
    }

    public void uploadData(int width, int height, ByteBuffer data) {
        uploadData(GL_RGBA16, width, height, GL_RGBA, data);
    }

    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
    }

    public void setParameter(int name, int value) {
        setParameter(GL_TEXTURE_2D, name, value);
    }

    public void setParameter(int target, int name, int value) {
        glTexParameteri(target, name, value);
    }

    public void setImageData(ByteBuffer data) {
        m_Data.ImageData = data;
    }

    private void setPath(String path) {
        m_Data.Path = path;
    }

    public void setWidth(int width) {
        if(width < 0) return;
        m_Data.Width = width;
    }

    public void setHeight(int height) {
        if(height < 0) return;
        m_Data.Height = height;
    }

    public void setChannels(int channels) {
        if(channels < 0) return;
        m_Data.Channels = channels;
    }

    public String getPath() {
        return m_Data.Path;
    }

    public ByteBuffer getImageData() {
        return m_Data.ImageData;
    }

    public int getWidth() {
        return m_Data.Width;
    }

    public int getHeight() {
        return m_Data.Height;
    }

    public int getChannels() {
        return m_Data.Channels;
    }

    public static Texture createTexture(int width, int height, int channels, ByteBuffer data) {
        Texture texture = new Texture();

        texture.setImageData(data);
        texture.setWidth(width);
        texture.setHeight(height);
        texture.setChannels(channels);

        texture.bind();

        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        switch(texture.getChannels()) {
            case 3 -> texture.uploadData(GL_RGB16, texture.getWidth(), texture.getHeight(), GL_RGB, texture.getImageData());
            case 4 -> texture.uploadData(texture.getWidth(), texture.getHeight(), texture.getImageData());
        }

        glGenerateMipmap(GL_TEXTURE_2D);
        return texture;
    }

    public static Texture loadTexture(String path) {
        int width, height, chans;
        ByteBuffer imageData = null;
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
                imageData = stbi_load_from_memory(imageBuffer, w, h, channels, 0);
                if(imageData == null)
                    throw new IOException("Failed to read data in Texture: " + path);
            } catch (IOException e) {
                Log.RENDER.error("Failed to load Texture:\n{}", e.getMessage());
                System.exit(-1);
            }

            width = w.get(0);
            height = h.get(0);
            chans = channels.get(0);
        }

        Texture result = createTexture(width, height, chans, imageData);
        result.setPath(path);
        return result;
    }

}
