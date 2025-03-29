package pacman.model.entity.dynamic.ghost.State;

public interface State {
    void trigger();

    void tick();

    void setLength(int length);
}
