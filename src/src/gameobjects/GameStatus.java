package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ChangeDimensionaOfPadlleStrategy;

/**
 * This class represents a status that will be dropped from a hitted brick.
 */
public class GameStatus extends GameObject {
    private static final String PADDLE_TAG = "paddle";
    private static final String DOWN_BORDER_TAG = "downBorder";
    private static final float STATUS_VELOCITY = 150;
    private final ChangeDimensionaOfPadlleStrategy collisionStrategy;

    /**
     * Constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param collisionStrategy instance of collision strategy (changeDimensionsStrategy f.e.)
     */
    public GameStatus(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      ChangeDimensionaOfPadlleStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.setVelocity(Vector2.DOWN.mult(STATUS_VELOCITY));
    }

    /**
     * overrides this method for collision only if paddle
     * @param other Paddle
     * @return true when object should colly
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PADDLE_TAG) || other.getTag().equals(DOWN_BORDER_TAG);
    }

    /**
     * Controls the collision.
     * @param other paddle
     * @param collision collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // don't won't to change a down border
        if(!other.getTag().equals(DOWN_BORDER_TAG)) {
            collisionStrategy.changePaddleDimensions(other, this.getTag());
        }
    }
}
