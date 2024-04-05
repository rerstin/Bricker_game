package src.brick_strategies;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * This class represents a collisions between objects in game and behavior of them
 */
public class RemoveBrickStrategy implements CollisionStrategy{
    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructor
     * @param gameObjectCollection collection of objects in game
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * Controls collision between objects in game (for example a case when bal collies into a brick)
     * @param thisObj object in collision
     * @param otherObj object in collision
     * @param bricksCounter global brick counter.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        // delete brick after collision
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            // if here for the case when two balls collies into brick. so only if we really removed a brick,
            // we will decrease a counter. to prevent double decreasing
            bricksCounter.decrement();
        }

    }

    /**
     * Return reference to GameObjectCollection
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }
}
