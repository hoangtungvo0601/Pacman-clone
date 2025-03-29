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

import java.util.Arrays;
import java.util.List;

  /**
 * Concrete renderable factory for Blinky Ghost objects
 */
public class BlinkyFactory extends GhostFactory implements RenderableFactory {
    @Override
    protected Vector2D getTargetPos(){
          return targetCorners.get(1);
    }

    @Override
    protected Image getGhostImage(){
          return BLINKY_IMAGE;
    }

    @Override
    protected TargetStrategy getStrategy(){
        return new BlinkyStrategy();
    }

    @Override
    protected String getNickname(){
        return "blinky";
    }
}
