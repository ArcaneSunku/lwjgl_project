package git.arcane.core;

public interface IGame {

    void initialize();
    void dispose();

    void update(double dt);
    void render(double alpha);

}
