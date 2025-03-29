package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.Strategy.BlinkyStrategy;
import pacman.model.entity.dynamic.ghost.Strategy.TargetStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.PacmanVisual;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    protected static final int RIGHT_X_POSITION_OF_MAP = 448;
    protected static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    protected static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    protected static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    protected static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    protected static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    protected static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    protected static final Image FRIGHTENED = new Image("maze/ghosts/frightened.png");

    List<Vector2D> targetCorners = Arrays.asList(
            new Vector2D(0, TOP_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP),
            new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)
    );

    protected TargetStrategy getStrategy(){
        return new BlinkyStrategy();
    }

    protected Vector2D getTargetPos(){
        return targetCorners.get(getRandomNumber(0, targetCorners.size() - 1));
    }

    protected Image getGhostImage(){
        return BLINKY_IMAGE;
    }

    protected int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    protected String getNickname(){
        return "blinky";
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            Map<GhostMode, Image> images = new HashMap<>();
            images.put(GhostMode.CHASE, getGhostImage());
            images.put(GhostMode.SCATTER, getGhostImage());
            images.put(GhostMode.FRIGHTENED, FRIGHTENED);
            images.put(GhostMode.READY, getGhostImage());

            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    getGhostImage().getHeight(),
                    getGhostImage().getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();

            return new GhostImpl(
                    images,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    getTargetPos(),
                    getStrategy(),
                    getNickname());


        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
