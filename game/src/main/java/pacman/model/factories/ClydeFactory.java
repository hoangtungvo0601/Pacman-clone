package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.Strategy.BlinkyStrategy;
import pacman.model.entity.dynamic.ghost.Strategy.ClydeStrategy;
import pacman.model.entity.dynamic.ghost.Strategy.TargetStrategy;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete renderable factory for Clyde Ghost objects
 */
public class ClydeFactory extends GhostFactory implements RenderableFactory {
    @Override
    protected Vector2D getTargetPos(){
        return targetCorners.get(2);
    }

    @Override
    protected Image getGhostImage(){
        return CLYDE_IMAGE;
    }

    @Override
    protected TargetStrategy getStrategy(){
        return new ClydeStrategy();
    }

    @Override
    protected String getNickname(){
        return "clyde";
    }
}
