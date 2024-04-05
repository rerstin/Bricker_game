package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import java.util.Objects;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator{
    private static final float CAMERA_COEFFICIENT = 1.2f;
    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private static final String MAIN_BALL_TEG = "mainBall";
    private final WindowController windowController;
    private final BrickerGameManager gameManager;
    private final CollisionStrategy toBeDecorated;
    private BallCollisionCountdownAgent ballCollisionCountdownAgent;

    /**
     * Constructor
     *
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param windowController windowController - controls visual rendering of the game window and object renderables.
     * @param gameManager an instance of BrickerGameManager
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                                WindowController windowController,
                                BrickerGameManager gameManager)
    {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * Change camere position on collision and delegate to held CollisionStrategy.
     * @param thisObj first object in collision.
     * @param otherObj second object in collision.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        toBeDecorated.onCollision(thisObj, otherObj, counter);
        if(gameManager.camera() == null) {
            Ball ball = getMainBall();
            // set camera to the ball
            gameManager.setCamera(
                    new Camera(
                            ball,            //object to follow
                            Vector2.ZERO,    //follow the center of the object
                            windowController.getWindowDimensions().mult(CAMERA_COEFFICIENT),  //widen the frame a bit
                            windowController.getWindowDimensions()   //share the window dimensions
                    ));
            // create and add an instance of BallCollisionCountdownAgent to control a camera movement
            ballCollisionCountdownAgent = new
                    BallCollisionCountdownAgent(ball, this,
                    Objects.requireNonNull(ball).getCollisionCount() + NUM_BALL_COLLISIONS_TO_TURN_OFF);
            getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
        }

    }

    /**
     * finds a main ball in GameObjectsCollection;
     * @return Main Ball
     */
    private Ball getMainBall() {
        Ball ball = null;
        for(GameObject gameObject: getGameObjectCollection())
        {
            if(gameObject.getTag().equals(MAIN_BALL_TEG)){
                // downcasting for ballCollisionCountdownAgent
                ball = (Ball)gameObject;
            }
        }
        return ball;
    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(ballCollisionCountdownAgent);
    }
}
