package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;
import java.util.Random;

public class BrickStrategyFactory {
    private static final int NUM_OF_STRATEGIES = 6;
    private static final int MAX_DEPTH = 3;
    private final GameObjectCollection gameObjectCollection;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;

    /**
     * Constructor
     * @param gameObjectCollection an instance of GameObjectCollection
     * @param gameManager an instance of BrickerGameManager
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param inputListener a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param windowController windowController - controls visual rendering of the game window and object renderables.
     * @param windowDimensions Width and height of window.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                 BrickerGameManager gameManager,
                                 ImageReader imageReader,
                                 SoundReader soundReader,
                                 UserInputListener inputListener,
                                 WindowController windowController,
                                 Vector2 windowDimensions)
    {
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Randomly selects between 5 strategies and returns one CollisionStrategy object
     * which is a RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies,
     * or decorated by one of the decorator strategies and a pair of additional two decorator strategies.
     * @return CollisionStrategy object
     */
    public CollisionStrategy getStrategy()
    {
        RemoveBrickStrategy removeBrickStrategy = new RemoveBrickStrategy(gameObjectCollection);
        return createStrategy(removeBrickStrategy, 0, NUM_OF_STRATEGIES);
    }

    /**
     * Recursion function that create a new strategy
     * @param strategy CollisionStrategy object to decorate
     * @param recursion_depth max number of multi strategies.
     * @param randomInterval this parameter helps to choose a strategy before step to recursion.
     *                       We need to choose the first strategy before go to choose a second and third.
     * @return CollisionStrategy object.
     */
    private CollisionStrategy createStrategy(CollisionStrategy strategy, int recursion_depth, int randomInterval) {
        if(recursion_depth == MAX_DEPTH){
            return strategy;
        }
        Random random = new Random();
        int rand_int = random.nextInt(randomInterval);
        // we want to receive a multi strategy without strategy that only brake a brick
        if(recursion_depth >= 1)
        {
            rand_int = random.nextInt(randomInterval - 1) + 1;
        }
        switch (rand_int) {
            case 0:
                return strategy;
            case 1:
                return new AddPaddleStrategy(new RemoveBrickStrategy(gameObjectCollection),
                                             imageReader, inputListener, windowDimensions);
            case 2:
                return new PuckStrategy(new RemoveBrickStrategy(gameObjectCollection), imageReader, soundReader);
            case 3:
                return new ChangeCameraStrategy(strategy, windowController, gameManager);
            case 4:
                return new ChangeDimensionaOfPadlleStrategy(strategy, imageReader);
            // inner calling to createStrategy creates an instance of CollisionStrategy that will be decorated in recursion
            case 5:
                return createStrategy(createStrategy(strategy, recursion_depth + 1, NUM_OF_STRATEGIES - 1),
                    recursion_depth + 1, NUM_OF_STRATEGIES);
            default:
                return null;
        }
    }
}
