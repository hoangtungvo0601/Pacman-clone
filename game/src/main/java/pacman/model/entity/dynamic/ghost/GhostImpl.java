package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.State.*;
import pacman.model.entity.dynamic.ghost.Strategy.TargetStrategy;
import pacman.model.entity.dynamic.ghost.observer.GhostPositionObserver;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private int currentDirectionCount = 0;
    private TargetStrategy targetStrategy;
    private Direction playerDirection;
    private State ghostState;
    private State chasteState;
    private State frightenedState;
    private State scatterState;
    private State readyState;
    private String nickname;
    private Map <String, Vector2D> otherGhostPositions;
    private Set<GhostPositionObserver> otherGhostPositionObservers;
    private final Map<GhostMode, Image> images;

    public GhostImpl(Map<GhostMode, Image> images , BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, TargetStrategy targetStrategy, String nickname) {
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.targetStrategy = targetStrategy;
        this.chasteState = new ChaseState(this);
        this.frightenedState = new FrightenedState(this);
        this.scatterState = new ScatterState(this);
        this.readyState = new ReadyState(this);
        this.nickname = nickname;
        this.otherGhostPositions = new HashMap<>();
        this.otherGhostPositionObservers = new HashSet<>();
        this.images = images;
        this.image = images.get(GhostMode.SCATTER);
        this.speeds = new HashMap<>();
        this.speeds.put(GhostMode.SCATTER, 0.0);
        this.speeds.put(GhostMode.CHASE, 0.0);
        this.speeds.put(GhostMode.FRIGHTENED, 0.0);
        this.speeds.put(GhostMode.READY, 0.0);
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {

        this.speeds = speeds;
    }

    @Override
    public void setLength(Map<GhostMode, Integer> length){
        this.chasteState.setLength(length.get(GhostMode.CHASE));
        this.scatterState.setLength(length.get(GhostMode.SCATTER));
        this.frightenedState.setLength(length.get(GhostMode.FRIGHTENED));
        this.readyState.setLength(length.get(GhostMode.READY));
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void update() {

        this.ghostState.tick();
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
//        System.out.println("Hi i am " + this.nickname + ". I am in mode: " + this.ghostMode);
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = getTargetLocation();
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    private Vector2D randomPosition(){
        Random r1 = new Random();
        double x = 0 + (448 - 0) * r1.nextDouble();
        Random r2 = new Random();
        double y = 16 * 3 + (16 * 34 - 16 * 3) * r2.nextDouble();
        return new Vector2D(x, y);
    }

    private Vector2D getTargetLocation() {
        return switch (this.ghostMode) {
            case CHASE -> this.targetStrategy.lockingTarget(this.playerPosition, this.playerDirection, this.targetCorner, this.kinematicState.getPosition(), this.otherGhostPositions);
            case SCATTER -> this.targetCorner;
            case FRIGHTENED -> randomPosition();
            case READY -> this.targetCorner;
        };
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setImage(GhostMode ghostMode){
        this.image = images.get(ghostMode);
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        if(ghostMode == GhostMode.CHASE) this.ghostState = this.chasteState;
        else if(ghostMode == GhostMode.SCATTER) this.ghostState = this.scatterState;
        else if(ghostMode == GhostMode.FRIGHTENED) this.ghostState = this.frightenedState;
        else this.ghostState = this.readyState;

        this.ghostState.trigger();

        this.ghostMode = ghostMode;
        this.kinematicState.setSpeed(speeds.get(ghostMode));
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            if (this.ghostMode == GhostMode.FRIGHTENED) {
                level.ghostEaten();
                reset();
                setGhostMode(GhostMode.READY);
            } else {
                level.handleLoseLife();
            }
        }
    }

    @Override
    public void update(Vector2D playerPosition, Direction playerDirection) {

        this.playerPosition = playerPosition;
        this.playerDirection = playerDirection;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);

        setGhostMode(GhostMode.SCATTER);
//        this.ghostState = this.scatterState;
//        this.ghostState.trigger();
//
//        this.ghostMode = GhostMode.SCATTER;
//        this.currentDirectionCount = minimumDirectionCount;

    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public void FrightenedTrigger(){
        setGhostMode(GhostMode.FRIGHTENED);
    }

    @Override
    public void registerGhostPositionObserver(GhostPositionObserver observer){
        otherGhostPositionObservers.add(observer);
        notifyOtherGhostObservers();
    };

    @Override
    public void notifyOtherGhostObservers(){
        for(GhostPositionObserver observer : otherGhostPositionObservers){
            observer.updateOtherGhostPosition(this.kinematicState.getPosition(), this.nickname);
        }
    };

    @Override
    public void updateOtherGhostPosition(Vector2D position, String nickname){
        otherGhostPositions.put(nickname, position);
    };

    @Override
    public String getNickname(){
        return this.nickname;
    }

}
