package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class represents an additional paddle in game.
 */
public class MockPaddle extends Paddle{

    public static boolean isInstantiated = false;
    private final GameObjectCollection gameObjectCollection;
    private final Counter collisionCounter;

    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object. Can be null, in which case
     * @param inputListener       Input from keyboard
     * @param windowDimensions    Width and height of window.
     * @param gameObjectCollection instance of gameObjectCollection for removing an objects
     * @param minDistanceFromEdge minimum distance from edge of window
     * @param numCollisionsToDisappear number of collisions before disappearing
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions, GameObjectCollection gameObjectCollection, int minDistanceFromEdge, int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        collisionCounter = new Counter(numCollisionsToDisappear);
        isInstantiated = true;
    }

    /**
     * Controls collision between bal and brick)
     * @param other the ball object
     * @param collision instance of Collision that controls a collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter.decrement();
        if(collisionCounter.value() <= 0){
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }
}
