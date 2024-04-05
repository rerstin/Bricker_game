package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Ball is the main game object.
 * It's positioned in game window as part of game initialization and given initial velocity.
 * On collision, it's velocity is updated to be reflected about the normal vector of the surface it collides with.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Counter collisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param collisionSound The sound of collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter();
    }

    /**
     * Controls collision between ball and other object.
     * @param other the other object
     * @param collision instance of Collision that controls a collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // if ball collies into border It's velocity will change a velocity to opposite side
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCounter.increment();
        if(collisionSound != null){
            collisionSound.play();
        }
    }

    /**
     *
     * Ball object maintains a counter which keeps count of collisions from start of game
     * @return count of collisions from start of game.
     */
    public int getCollisionCount(){
        return collisionCounter.value();
    }
}
