package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.GameStatus;

import java.util.Random;

/**
 * Class represents a Changing paddle dimensions strategy.
 */
public class ChangeDimensionaOfPadlleStrategy extends RemoveBrickStrategyDecorator{
    private static final float STATUS_WIDTH = 30;
    private static final float STATUS_HEIGHT = 20;
    private static final int NUM_OF_CHANGES = 2;
    private static final String[] PADDLE_CHANGES_PATHES = {"assets/buffNarrow.png", "assets/buffWiden.png"};
    private static final String[] PADDLE_CHANGES_TAGS = {"Narrow", "Widen"};
    private static final float UP_DIMENSIONS = 1.2f;
    private static final float DOWN_DIMENSIONS = 0.5f;
    private final ImageReader imageReader;
    private final CollisionStrategy toBeDecorated;

    /**
     * Constructor
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     */
    public ChangeDimensionaOfPadlleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Controls a collision with brick.
     * @param thisObj first object in collision.
     * @param otherObj second object in collision.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        // drop a status with random behaviour (narrowing or widening)
        toBeDecorated.onCollision(thisObj, otherObj, counter);
        Random random = new Random();
        int rand_int = random.nextInt(NUM_OF_CHANGES);
        Renderable renderable = imageReader.readImage(PADDLE_CHANGES_PATHES[rand_int], true);
        GameStatus gameStatus = new GameStatus(thisObj.getTopLeftCorner(),
                new Vector2(STATUS_WIDTH, STATUS_HEIGHT), renderable, this);
        gameStatus.setTag(PADDLE_CHANGES_TAGS[rand_int]);
        toBeDecorated.getGameObjectCollection().addGameObject(gameStatus);
    }

    /**
     * Changes a paddle dimensions according to received status.
     * @param paddle paddle object.
     * @param changesType Tag of received status.
     */
    public void changePaddleDimensions(GameObject paddle, String changesType) {
        if(changesType.equals(PADDLE_CHANGES_TAGS[1])) {
            paddle.setDimensions(paddle.getDimensions().multX(UP_DIMENSIONS));
        }
        else{
            paddle.setDimensions(paddle.getDimensions().multX(DOWN_DIMENSIONS));
        }
    }
}
