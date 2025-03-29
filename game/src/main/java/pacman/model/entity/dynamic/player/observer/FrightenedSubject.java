package pacman.model.entity.dynamic.player.observer;

public interface FrightenedSubject {
    /**
     * Adds an observer to list of frightened observers for subject
     *
     * @param observer FrightenedObserver
     */
    void registerFrightenedObserver(FrightenedObserver observer);

    /**
     * Notifies observer of that Pacman just have eaten power pellet
     */
    void notifyFrightenedObservers();
}
