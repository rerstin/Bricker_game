package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class displays a graphic object on the game window showing a numeric count of lives left.
 */
public class NumericLifeCounter extends GameObject {
    private final Counter livesCounter;
    private static final String LIVES_MESSAGE = "Lives left: ";
    private static final int LIVES_WIDGET_LENGTH = 50;
    private static final int LIVES_WIDGET_HEIGHT = 20;
    private final TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param livesCounter  Counter of lives in game.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjectCollection Global game object collection.
     */
    public NumericLifeCounter(Counter livesCounter,
                               Vector2 topLeftCorner,
                               Vector2 dimensions,
                               GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        textRenderable = new TextRenderable(LIVES_MESSAGE + livesCounter.value());
        textRenderable.setColor(Color.ORANGE);
        GameObject livesLeft = new GameObject(topLeftCorner,
                new Vector2(LIVES_WIDGET_LENGTH, LIVES_WIDGET_HEIGHT), textRenderable);
        gameObjectCollection.addGameObject(livesLeft, Layer.BACKGROUND);
    }

    /**
     * Updates a state of lives.
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRenderable.setString(LIVES_MESSAGE + livesCounter.value());
    }
}
