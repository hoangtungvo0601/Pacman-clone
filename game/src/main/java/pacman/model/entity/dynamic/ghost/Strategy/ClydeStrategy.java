package pacman.model.entity.dynamic.ghost.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Map;

public class ClydeStrategy implements TargetStrategy {
    @Override
    public Vector2D lockingTarget(Vector2D playerPosition, Direction playerDirection, Vector2D targetCorner, Vector2D ghostPosition, Map<String, Vector2D> otherGhostPositions){
        double distance = Vector2D.calculateEuclideanDistance(playerPosition, ghostPosition);
        if(distance / 16 > 8) {
            return playerPosition;
        }
        else {
            return targetCorner;
        }
    }
}
