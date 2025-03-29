package pacman.model.entity.dynamic.ghost.observer;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface GhostPositionObserver {
    void updateOtherGhostPosition(Vector2D position, String nickname);
}
