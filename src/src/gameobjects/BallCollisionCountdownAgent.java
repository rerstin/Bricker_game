package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * Class represents an object of this class is instantiated on collision
 * of ball with a brick with a change camera strategy.
 * It checks ball's collision counter every frame, and once the it finds the ball has
 * collided countDownValue times since instantiation,
 * it calls the strategy to reset the camera to normal.
 */
public class BallCollisionCountdownAgent extends GameObject {

    private final ChangeCameraStrategy owner;
    private final int countDownValue;
    private final Ball ball;

    /**
     *
     * @param ball Ball object whose collisions are to be counted.
     * @param owner Object asking for countdown notification.
     * @param countDownValue Number of ball collisions. Notify caller object that
     *                       the ball collided countDownValue times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball,
                                        ChangeCameraStrategy owner,
                                        int countDownValue){
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.owner = owner;
        this.countDownValue = countDownValue;
        this.ball = ball;
    }

    /**
     * If count of ball's collisions is a countDownValue function resets a camera to normal state.
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(ball.getCollisionCount() >= countDownValue)
        {
            owner.turnOffCameraChange();
        }
    }
}
