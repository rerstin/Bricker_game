package src.brick_strategies;

import danogl.collisions.GameObjectCollection;


/**
 * Abstract decorator to add functionality to the remove brick strategy, following the decorator pattern.
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {
    private final CollisionStrategy toBeDecorated;

    /**
     * Constructor
     *
     * @param toBeDecorated Collision strategy object to be decorated.
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated) {

        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Controls collision between objects in game (for example a case when bal collies into a brick)
     *
     * @param thisObj  object in collision
     * @param otherObj object in collision
     */
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj, danogl.util.Counter counter) {
    }


    /**
     * Return reference to GameObjectCollection
     * @return global game object collection whose reference is held in object.
     */
    public GameObjectCollection getGameObjectCollection() {
        return toBeDecorated.getGameObjectCollection();
    }
}
