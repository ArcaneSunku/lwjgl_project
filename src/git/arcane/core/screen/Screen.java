package git.arcane.core.screen;

public interface Screen {

    void show();
    void hide();
    void dispose();

    void update(double dt);
    void render(double alpha);

}
