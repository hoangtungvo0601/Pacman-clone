package pacman.model.entity.dynamic.ghost.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Map;

public class BlinkyStrategy implements TargetStrategy {
    @Override
    public Vector2D lockingTarget(Vector2D playerPosition, Direction playerDirection, Vector2D targetCorner, Vector2D ghostPosition, Map<String, Vector2D> otherGhostPositions){
        return playerPosition;
    }
}
