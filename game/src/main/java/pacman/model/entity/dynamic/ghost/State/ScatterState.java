package pacman.model.entity.dynamic.ghost.State;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;

public class ScatterState implements State{
    Ghost ghost;
    public int length;
    private int tick;

    public ScatterState(Ghost ghost) {
        this.ghost = ghost;
    }

    @Override
    public void setLength(int length){
        this.length = length;
    }

    @Override
    public void trigger(){
        this.ghost.setImage(GhostMode.SCATTER);
        this.tick = this.length;
    }

    @Override
    public void tick(){
        this.tick = this.tick - 1;
        if(this.tick <= 0){
            this.ghost.setGhostMode(GhostMode.CHASE);
        }
    }
}
