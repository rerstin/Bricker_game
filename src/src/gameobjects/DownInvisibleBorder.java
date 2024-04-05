package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class represents a down invisible border for deleting status objects and mock ball
 */
public class DownInvisibleBorder extends GameObject {
    private static final String MAIN_BALL_TEG = "mainBall";
    private static final String DOWN_BORDER_TAG = "downBorder";
    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjectCollection collection of game objects.
     */
    public DownInvisibleBorder(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                               GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.setTag(DOWN_BORDER_TAG);
    }

    /**
     * Controlls that main ball won't be removed bt the border
     * @param other game object
     * @return true if object is nit a main ball
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !other.getTag().equals(MAIN_BALL_TEG);
    }

    /**
     * Removes an object that has been caught (It can be only mock ball, main ball or status)
     * @param other mock ball, main ball or status
     * @param collision collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjectCollection.removeGameObject(other);
    }
}
