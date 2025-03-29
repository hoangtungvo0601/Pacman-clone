package pacman.model.factories;

import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.CollectableDecorator;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;

public class PowerPelletFactory implements RenderableFactory{
    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        PelletFactory pelletFactory = new PelletFactory();
        Collectable pellet = (Collectable) pelletFactory.createRenderable(position);

        Collectable powerPellet = new PowerPellet(pellet);
        return powerPellet;
    }
}
