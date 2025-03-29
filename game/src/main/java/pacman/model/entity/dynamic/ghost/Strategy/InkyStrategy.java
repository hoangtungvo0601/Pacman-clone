package pacman.model.entity.dynamic.ghost.Strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Map;

public class InkyStrategy implements TargetStrategy {

    @Override
    public Vector2D lockingTarget(Vector2D playerPosition, Direction playerDirection, Vector2D targetCorner, Vector2D ghostLocation, Map<String, Vector2D> otherGhostPositions){
        int X = (int) playerPosition.getX()/16;
        int Y = (int) playerPosition.getY()/16;

        double currentGridX = (double) X;
        double currentGridY = (double) Y;

        double nextGridX = currentGridX;
        double nextGridY = currentGridY;

        if(playerDirection == Direction.LEFT) nextGridX -= 2;
        else if(playerDirection == Direction.RIGHT) nextGridX += 2;
        else if(playerDirection == Direction.UP) nextGridY -= 2;
        else if(playerDirection == Direction.DOWN) nextGridY += 2;

//        System.out.println("Current pacman grid: " + currentGridX + ", " + currentGridY);
//        System.out.println("Next pacman grid: " + nextGridX + ", " + nextGridY);

        double nextPosX = nextGridX * 16;
        double nextPosY = nextGridY * 16;

        double blinkyX = ghostLocation.getX();
        double blinkyY = ghostLocation.getY();

        if(otherGhostPositions.containsKey("blinky")){
            blinkyX = otherGhostPositions.get("blinky").getX();
            blinkyY = otherGhostPositions.get("blinky").getY();
        }

        double targetX = blinkyX + (nextPosX - blinkyX) * 2;
        double targetY = blinkyY + (nextPosY - blinkyY) * 2;

        return new Vector2D(targetX, targetY);
    }
}
