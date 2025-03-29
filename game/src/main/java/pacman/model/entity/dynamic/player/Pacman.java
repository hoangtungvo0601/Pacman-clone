package pacman.model.entity.dynamic.player;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.observer.FrightenedObserver;
import pacman.model.entity.dynamic.player.observer.FrightenedSubject;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.entity.dynamic.player.observer.PlayerPositionSubject;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pacman implements Controllable, PlayerPositionSubject{

    public static final int PACMAN_IMAGE_SWAP_TICK_COUNT = 8;
    private final Layer layer = Layer.FOREGROUND;
    private final Map<PacmanVisual, Image> images;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Set<PlayerPositionObserver> observers;
    private final Set<FrightenedObserver> frightenedObservers;
    private final MovementInvoker movementInvoker;
    private KinematicState kinematicState;
    private Image currentImage;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private boolean isClosedImage;
    private int consecutive_counter;
    private static final int[] consecutive_points = {200, 400, 800, 1600};

    public Pacman(
            Image currentImage,
            Map<PacmanVisual, Image> images,
            BoundingBox boundingBox,
            KinematicState kinematicState
    ) {
        this.currentImage = currentImage;
        this.images = images;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.observers = new HashSet<>();
        this.frightenedObservers = new HashSet<>();
        this.possibleDirections = new HashSet<>();
        this.isClosedImage = false;
        this.movementInvoker = MovementInvoker.getInstance();
        this.consecutive_counter = 0;
    }

    @Override
    public Image getImage() {
        if (isClosedImage) {
            return images.get(PacmanVisual.CLOSED);
        } else {
            return currentImage;
        }
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
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    public void update() {
        movementInvoker.update(this.possibleDirections);
        kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
        notifyObservers();
    }

    @Override
    public void setSpeed(double speed) {
        this.kinematicState.setSpeed(speed);
    }

    @Override
    public void up() {
        this.kinematicState.up();
        this.currentDirection = Direction.UP;
        this.currentImage = images.get(PacmanVisual.UP);
    }

    @Override
    public void down() {
        this.kinematicState.down();
        this.currentDirection = Direction.DOWN;
        this.currentImage = images.get(PacmanVisual.DOWN);
    }

    @Override
    public void left() {
        this.kinematicState.left();
        this.currentDirection = Direction.LEFT;
        this.currentImage = images.get(PacmanVisual.LEFT);
    }

    @Override
    public void right() {
        this.kinematicState.right();
        this.currentDirection = Direction.RIGHT;
        this.currentImage = images.get(PacmanVisual.RIGHT);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isCollectable(renderable)) {
            Collectable collectable = (Collectable) renderable;
            level.collect(collectable);
            collectable.collect();
            if(collectable.isPower()) {
                consecutive_counter = 0;
                notifyFrightenedObservers();
            }
        }
    }

    @Override
    public void registerFrightenedObserver(FrightenedObserver observer){
        this.frightenedObservers.add(observer);
    }

    @Override
    public void notifyFrightenedObservers(){
        for(FrightenedObserver observer : frightenedObservers){
            observer.FrightenedTrigger();
        }
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void reset() {
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .setSpeed(kinematicState.getSpeed())
                .build();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());

        // go left by default
        left();
        movementInvoker.reset();
        this.isClosedImage = false;
    }

    @Override
    public void registerObserver(PlayerPositionObserver observer) {
        this.observers.add(observer);
        observer.update(this.kinematicState.getPosition(), this.kinematicState.getDirection());
    }

    @Override
    public void removeObserver(PlayerPositionObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        for (PlayerPositionObserver playerPositionObserver : observers) {
            playerPositionObserver.update(this.kinematicState.getPosition(), this.currentDirection);
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
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
    public void switchImage() {
        this.isClosedImage = !this.isClosedImage;
    }

    @Override
    public int consecutiveEatenGhost(){
        int res = consecutive_points[consecutive_counter];
        consecutive_counter++;
        if(consecutive_counter == 4) consecutive_counter = 0;
        return res;
    }
}
