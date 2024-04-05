package src;
import src.brick_strategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;
import java.awt.*;
import java.util.Random;


/**
 * This class implements a core of game Bricker
 */
public class BrickerGameManager extends GameManager {
    public static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_WIDTH = 100;
    private static final int BALL_RADIUS = 20;
    private static final float BALL_SPEED = 150;
    private static final float CENTER_INDEX = 0.5f;
    private static final int PADDLE_X_INDEX = 2;
    private static final float PADDLE_DISTANCE_FROM_DOWN_BORDER = 20;
    private static final int[] WINDOW_DIMENSIONS = {700, 500};
    private static final int NUMBER_BRICKS_IN_ROW = 5;
    private static final int NUMBER_BRICKS_IN_COL = 8;
    private static final int LIVES_NUMBER = 3;
    private static final float DISTANCE_BETWEEN_BRICKS = 3f;
    private static final int BRICK_HEIGHT = 15;
    private static final int GRAPHIC_LIVES_DISTANCE_FROM_DOWN_BORDER = 50;
    private static final int GRAPHIC_LIVES_DISTANCE_FROM_LEFT_BORDER = 20;
    private static final int NUMERIC_LIVES_DISTANCE_FROM_LEFT_BORDER = 20;
    private static final int NUMERIC_LIVES_DISTANCE_FROM_DOWN_BORDER = 80;
    private static final int FRAME_RATE = 80;
    private static final int NUM_OF_VERTICAL_BORDERS = 2;
    private static final String GAME_TITLE = "Bricker";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BLOP_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String LIFE_IMAGE_PATH = "assets/heart.png";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String WIN_MESSAGE = "You Win!";
    private static final String LOSE_MESSAGE = "You Lose!";
    private static final String PLAY_AGAIN_MESSAGE = " Play again?";
    private static final String MAIN_BALL_TEG = "mainBall";
    private Counter livesCounter;
    private Counter bricksCounter;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private GraphicLifeCounter graphicLifeCounter;
    private NumericLifeCounter numericLifeCounter;
    private BrickStrategyFactory brickStrategyFactory;

    /**
     * Constructor
     * @param windowTitle title of window
     * @param windowDimensions Dimensions of window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }
    /**
     * Initializes a game objects
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param inputListener a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param windowController windowController - controls visual rendering of the game window and object renderables.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        // do less frame rate
        windowController.setTargetFramerate(FRAME_RATE);
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        brickStrategyFactory = new BrickStrategyFactory(gameObjects(), this, imageReader,
                soundReader, inputListener, windowController, windowDimensions);
        //create ball
        createBall(imageReader, soundReader);

        //create paddle
        createPaddle(imageReader, inputListener);

        //create borders
        createBorders();

        //create background
        createBackground(imageReader);

        //create lives
        livesCounter = new Counter(LIVES_NUMBER);
        createLivesCounters(imageReader);

        //create bricks
        bricksCounter = new Counter(NUMBER_BRICKS_IN_COL * NUMBER_BRICKS_IN_ROW);
        createBricks(imageReader);
    }

    /**
     * Creates a background of game
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     */
    private void createBackground(ImageReader imageReader) {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage(BACKGROUND_IMAGE_PATH, false));
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates a graphicLifeCounter and a numericLifeCounter of game
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     */
    private void createLivesCounters(ImageReader imageReader) {
        graphicLifeCounter = new GraphicLifeCounter(new Vector2(GRAPHIC_LIVES_DISTANCE_FROM_LEFT_BORDER,
                windowDimensions.y() - GRAPHIC_LIVES_DISTANCE_FROM_DOWN_BORDER), windowDimensions, livesCounter,
                imageReader.readImage(LIFE_IMAGE_PATH, true), gameObjects(),
                LIVES_NUMBER);
        numericLifeCounter = new NumericLifeCounter(livesCounter, new Vector2(NUMERIC_LIVES_DISTANCE_FROM_LEFT_BORDER,
                windowDimensions.y() - NUMERIC_LIVES_DISTANCE_FROM_DOWN_BORDER),windowDimensions, gameObjects());
    }

    /**
     * Creates bricks of game
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     */
    private void createBricks(ImageReader imageReader) {
        // calculate a length of a single brick
        float singleBrickLength = (windowDimensions.x() - NUM_OF_VERTICAL_BORDERS * BORDER_WIDTH -
                (NUMBER_BRICKS_IN_COL - 1) * DISTANCE_BETWEEN_BRICKS) / NUMBER_BRICKS_IN_COL;
        // add bricks into a game lke matrix
        for (int i = 0; i < NUMBER_BRICKS_IN_ROW; i++) {
            for (int j = 0; j < NUMBER_BRICKS_IN_COL; j++) {
                Brick brick = new Brick(new Vector2(BORDER_WIDTH + j * (singleBrickLength + DISTANCE_BETWEEN_BRICKS),
                        BORDER_WIDTH + i * (BRICK_HEIGHT + DISTANCE_BETWEEN_BRICKS)),
                        new Vector2(singleBrickLength, BRICK_HEIGHT),
                        imageReader.readImage(BRICK_IMAGE_PATH, true),
                        brickStrategyFactory.getStrategy(),
                        bricksCounter);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Updates a state of a game
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //removeMockBalls();
        float ballHeight = ball.getCenter().y();
        outOfRange(ballHeight);
        if(ballHeight > windowDimensions.y())
        {
            // update a lives counters
            livesCounter.decrement();
            numericLifeCounter.update(deltaTime);
            graphicLifeCounter.update(deltaTime);
            // set the ball to start position
            setBall();
        }
        checkForGameEnd();
    }

    /**
     * Prevents a bag when ball go away from borders (in small distance and high velocity It can happen)
     * @param ballHeight height of ball
     */
    private void outOfRange(float ballHeight) {
        float ballXCoordinate = ball.getCenter().x();
        if(ballHeight < 0 || ballXCoordinate < 0 || ballXCoordinate > windowDimensions.x()){
            // set the ball to start position
            setBall();
        }
    }

    public void repositionBall(danogl.GameObject ball){}

    /**
     * Check in game is ended
     */
    private void checkForGameEnd() {
        String prompt = "";
        if(livesCounter.value() <= 0) {
            //you lose
            prompt = LOSE_MESSAGE;
        }
        if(bricksCounter.value() <= 0) {
            //you win
            prompt = WIN_MESSAGE;
        }
        if(!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_MESSAGE;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /**
     * Creates ball in game
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        ball.setTag(MAIN_BALL_TEG);
        gameObjects().addGameObject(ball);
        setBall();
    }

    /**
     * Sets a ball in center of window and set Its velocity to random direction
     */
    private void setBall() {
        // put the ball to the center
        ball.setCenter(windowDimensions.mult(CENTER_INDEX));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean())
            ballVelX *= -1;
        if(rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates a paddle in a game
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param inputListener a SoundReader instance for reading soundclips from files for rendering event sounds.
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {

        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                imageReader.readImage(PADDLE_IMAGE_PATH, false),
                inputListener, windowDimensions, BORDER_WIDTH);
        paddle.setCenter(
                new Vector2(windowDimensions.x() / PADDLE_X_INDEX,
                        windowDimensions.y() - PADDLE_DISTANCE_FROM_DOWN_BORDER));
        gameObjects().addGameObject(paddle);
    }

    /**
     * create a borders in game
     */
    private void createBorders() {
        // left border
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        new RectangleRenderable(Color.GRAY))
        );
        // right border
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        new RectangleRenderable(Color.GRAY))
        );
        // up border
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        new RectangleRenderable(Color.GRAY))
        );
        //invisible down border for removing mock balls
        GameObject removingBorder = new DownInvisibleBorder(
                new Vector2(BORDER_WIDTH, windowDimensions.y() - BORDER_WIDTH),
                new Vector2(windowDimensions.x() - NUM_OF_VERTICAL_BORDERS *
                        BORDER_WIDTH, BORDER_WIDTH), null, gameObjects());
        gameObjects().addGameObject(removingBorder);
    }

    /**
     * Run game
     */
    public static void main(String[] args) {
        BrickerGameManager brickerGameManager = new BrickerGameManager(
                GAME_TITLE,
                new Vector2(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
        brickerGameManager.run();
    }
}
