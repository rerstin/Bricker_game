package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * This class represents a paddle in the game Bricker.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final Vector2 windowDimensions;
    private final int minDistanceFromEdge;
    private final UserInputListener inputListener;
    private static final String PADDLE_TAG = "paddle";
    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param inputListener Input from keyboard
     * @param windowDimensions Width and height of window.
     * @param minDistanceFromEdge minimum distance from edge of window
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                  Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
        this.setTag(PADDLE_TAG);
    }

    /**
     * Updates a state of paddle in the game/
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // update a place of paddle in the game according to user input
        // use add to get a case when both left and right bottoms is pushed
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
        {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
        {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        // paddle out of borders (return It to the closest border)
        if(getTopLeftCorner().x() < minDistanceFromEdge)
        {
            setTopLeftCorner(new Vector2(minDistanceFromEdge ,getTopLeftCorner().y()));
        }
        if(getTopLeftCorner().x() > windowDimensions.x() - minDistanceFromEdge -
                getDimensions().x())
        {
            setTopLeftCorner(new Vector2(windowDimensions.x() - minDistanceFromEdge -
                                getDimensions().x() ,getTopLeftCorner().y()));
        }

    }
}

