package src.gameobjects;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class displays a graphic object on the game window showing as many widgets as lives left.
 */
public class GraphicLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private final int numOfLives;
    private static final int HEART_SIZE = 20;
    private static final int DISTANCE_BETWEEN_HEARTS = 3;
    private final GameObject[] hearts;
    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param livesCounter  The counter of lives
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjectsCollection The global game object collection managed by game manager.
     * @param numOfLives The number of lives
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter livesCounter,
                              Renderable renderable,
                              GameObjectCollection gameObjectsCollection, int numOfLives) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        hearts = new GameObject[numOfLives];
        // add hearts to game and also to array for removing
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = new GameObject(new Vector2(topLeftCorner.x() + i *
                    (HEART_SIZE + DISTANCE_BETWEEN_HEARTS),
                        topLeftCorner.y()),
                    new Vector2(HEART_SIZE, HEART_SIZE), renderable);
            hearts[i] = heart;
            gameObjectsCollection.addGameObject(heart, Layer.BACKGROUND);
        }
    }

    /**
     * Updates a state of lives. Removes a life from GameObjectsCollection when player lost it
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // if player lost a life, remove object
        if(livesCounter.value() < numOfLives)
        {
            gameObjectsCollection.removeGameObject(hearts[livesCounter.value()], Layer.BACKGROUND);
        }
    }
}
