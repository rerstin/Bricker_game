package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;
import java.util.Random;
import static src.BrickerGameManager.*;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces extra paddle to game window which remains until colliding
 * NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_WIDTH = 100;
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE = 3;
    private static final float CENTER_INDEX = 2;
    private final CollisionStrategy toBeDecorated;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;


    /**
     *
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param inputListener Input from keyboard
     * @param windowDimensions Width and height of window.
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                             danogl.gui.ImageReader imageReader,
                             danogl.gui.UserInputListener inputListener,
                             danogl.util.Vector2 windowDimensions)
    {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Adds additional paddle to game and delegates to held object.
     * @param thisObj first object in collision.
     * @param otherObj second object in collision.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        // remove a brick
        toBeDecorated.onCollision(thisObj, otherObj, counter);
        // create a new paddle
        createMockPaddle();
    }

    /**
     * Creates a new mock paddle.
     */
    private void createMockPaddle() {
        if(!MockPaddle.isInstantiated) {
            Random random = new Random();
            // choose a random place in the board in x coordinate
            float x_coordinate = random.nextInt((int) windowDimensions.x() -
                    BORDER_WIDTH - PADDLE_WIDTH) + BORDER_WIDTH;
            // y coorrdinate in the center
            float y_coordinate = windowDimensions.y() / CENTER_INDEX;
            GameObject paddle = new MockPaddle(
                    new Vector2(x_coordinate, y_coordinate),
                    new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), imageReader.readImage(PADDLE_IMAGE_PATH, false),
                    inputListener, windowDimensions, toBeDecorated.getGameObjectCollection(),
                    BORDER_WIDTH, NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE);
            toBeDecorated.getGameObjectCollection().addGameObject(paddle);
        }
    }
}
