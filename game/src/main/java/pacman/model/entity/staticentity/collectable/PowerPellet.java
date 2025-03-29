package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.StaticEntityImpl;

public class PowerPellet extends StaticEntityImpl implements CollectableDecorator{
    Collectable collectable;

    public PowerPellet(Collectable collectable) {
        super(collectable.getBoundingBox(), collectable.getLayer(), collectable.getImage());

        this.collectable = collectable;
    }

    @Override
    public double getWidth(){
        return this.collectable.getWidth() * 2;
    }

    @Override
    public double getHeight(){
        return this.collectable.getHeight() * 2;
    }

    @Override
    public Vector2D getPosition(){
        return this.collectable.getPosition().add(new Vector2D(-8, -8));
    }

    @Override
    public Renderable.Layer getLayer(){
        return this.collectable.getLayer();
    }

    @Override
    public BoundingBox getBoundingBox(){
        BoundingBox newBox = new BoundingBoxImpl(this.getPosition(), this.getWidth(), this.getHeight());
        return newBox;
    }

    @Override
    public void collect() {
        this.collectable.collect();
    }

    @Override
    public void reset() {
        this.collectable.reset();
    }

    @Override
    public boolean isCollectable() {
        return this.collectable.isCollectable();
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.collectable.getPoints() * 5;
    }

    @Override
    public boolean isPower(){
        return true;
    }
}
