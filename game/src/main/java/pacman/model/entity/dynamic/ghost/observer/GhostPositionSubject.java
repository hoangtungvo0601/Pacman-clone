package pacman.model.entity.dynamic.ghost.observer;

import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

public interface GhostPositionSubject {
    void registerGhostPositionObserver(GhostPositionObserver observer);

    void notifyOtherGhostObservers();
}
