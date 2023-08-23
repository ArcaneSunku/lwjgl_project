package git.arcane.core.graphics.rendering.font;

public class Glyph {

    public final int x, y;
    public final int width, height;
    public final float advance;

    public Glyph(int x, int y, int width, int height, float advance) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.advance = advance;
    }

}
