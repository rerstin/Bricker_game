package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces several pucks instead of brick once removed.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator implements CollisionStrategy{
    private static final String BUBBLE5_4_SOUND_PATH = "assets/Bubble5_4.wav";
    private static final String BALL_IMAGE_PATH = "assets/mockBall.png" ;
    private static final String MOCK_BALL_TAG = "mockBall";
    private static final int BALL_RADIUS = 20;
    private static final float BALL_SPEED = 150;
    private static final float MOCK_BALL_SIZE = (float)BALL_RADIUS /  3;
    private static final int NUM_OF_PUCKS = 3;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final CollisionStrategy toBeDecorated;

    /**
     * Constructor
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Controls collision between objects in game (for example a case when bal collies into a brick)
     * @param thisObj first object in collision.
     * @param otherObj second object in collision.
     * @param bricksCounter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter)
    {
        // remove a brick
        toBeDecorated.onCollision(thisObj, otherObj, bricksCounter);

        // create a balls
        for (int i = 0; i < NUM_OF_PUCKS; i++) {
            createPuck(thisObj);

        }
    }

    /**
     * creates a mock balls
     * @param thisObj removed brick
     */
    private void createPuck(GameObject thisObj) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BUBBLE5_4_SOUND_PATH);
        GameObject puck = new Puck(
                Vector2.ZERO, new Vector2(MOCK_BALL_SIZE, MOCK_BALL_SIZE), ballImage, collisionSound);
        toBeDecorated.getGameObjectCollection().addGameObject(puck);
        // put balls into sye the center of removed brick
        puck.setCenter(thisObj.getCenter());
        puck.setTag(MOCK_BALL_TAG);
        //set velocity of ball
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean())
            ballVelX *= -1;
        if(rand.nextBoolean())
            ballVelY *= -1;
        puck.setVelocity(new Vector2(ballVelX, ballVelY));
    }

}
