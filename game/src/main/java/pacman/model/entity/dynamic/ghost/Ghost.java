package pacman.model.entity.dynamic.ghost;

import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.observer.GhostPositionObserver;
import pacman.model.entity.dynamic.ghost.observer.GhostPositionSubject;
import pacman.model.entity.dynamic.player.observer.FrightenedObserver;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver, FrightenedObserver, GhostPositionObserver, GhostPositionSubject {

    /***
     * Sets the speeds of the Ghost for each GhostMode
     * @param speeds speeds of the Ghost for each GhostMode
     */
    void setSpeeds(Map<GhostMode, Double> speeds);

    void setLength(Map<GhostMode, Integer> length);

    /**
     * Sets the mode of the Ghost used to calculate target position
     *
     * @param ghostMode mode of the Ghost
     */
    void setGhostMode(GhostMode ghostMode);

    void setImage(GhostMode ghostMode);

    String getNickname();
}
