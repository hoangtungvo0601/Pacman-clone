package pacman.model.entity.dynamic.ghost.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Map;

public class PinkyStrategy implements TargetStrategy {

    @Override
    public Vector2D lockingTarget(Vector2D playerPosition, Direction playerDirection, Vector2D targetCorner, Vector2D ghostPosition, Map<String, Vector2D> otherGhostPositions){
        int X = (int) playerPosition.getX()/16;
        int Y = (int) playerPosition.getY()/16;

        double currentGridX = (double) X;
        double currentGridY = (double) Y;


        if(playerDirection == Direction.LEFT){
            return new Vector2D((currentGridX - 4) * 16, currentGridY * 16);
        }
        else if(playerDirection == Direction.RIGHT){
            return new Vector2D((currentGridX + 4) * 16, currentGridY * 16);
        }
        else if(playerDirection == Direction.UP){
            return new Vector2D(currentGridX * 16, (currentGridY - 4) * 16);
        }
        else {
            return new Vector2D(currentGridX * 16, (currentGridY + 4) * 16);
        }
    }
}
