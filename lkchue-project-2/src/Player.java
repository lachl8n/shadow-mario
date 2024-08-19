import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the player.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Player extends FireThrower {
    private final Properties PROPS;
    private final int INITIAL_Y, INITIAL_JUMP_SPEED = -20;
    private double baselineY;
    private boolean invincibilityActive = false, fallingFromPlatform, atPlatformBorderLeft, atPlatformBorderRight;
    private int invincibilityTimer = 0, doubleScoreTimer = 0, activeDoubleScores = 0;

    /**
     * Constructor method for player class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public Player(int x, int y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.player.radius")), 0,
                new Image(PROPS.getProperty("gameObjects.player.imageRight")),
                Double.parseDouble(PROPS.getProperty("gameObjects.player.health")), PROPS);

        // Initialise constants and set the vertical baseline to the initial vertical position of the player
        this.INITIAL_Y = y;
        this.baselineY = INITIAL_Y;
        this.PROPS = PROPS;
    }

    /***
     * Method that updates the Player entity. Changes the direction of the image to align with player movement.
     * Draws entity in game environment. Updates power-up statuses. Moves vertically during jump or death animation.
     * @param input Input provided by the user
     * @param target Reference to Enemy Boss object
     * @param enemyBossExists Boolean indicating if the Enemy Boss exists in the level
     */
    public void update(Input input, EnemyBoss target, boolean enemyBossExists) {
        // Changes the direction of the image dependent on which arrow is being pressed by the user
        if (input.wasPressed(Keys.LEFT)) {
            setImage(new Image(this.PROPS.getProperty("gameObjects.player.imageLeft")));
        }
        if (input.wasPressed(Keys.RIGHT)) {
            setImage(new Image(this.PROPS.getProperty("gameObjects.player.imageRight")));
        }

        // Draws the player in the game environment
        drawEntity();
        // Moves the player vertically if jump has been initialised
        jump(input);

        // Moves the player vertically to animate death
        if (isDead()) {
            moveY();
        }

        // Decrease invincibility timer by one and indicate if the player is invincible
        decrementInvincibilityTimer();
        setInvincibilityStatus();

        // If the double score power-up is active, decrement the timer
        if (doubleScoreActive()) {
            doubleScoreTimer -= 1;
            if (doubleScoreTimer <= 0) {
                // If the double score timer lapses, indicate there being no active double score power-ups
                activeDoubleScores = 0;
            }
        }

        // If there is an enemy boss, update the player's thrown fireballs and throw fireballs when prompted by user
        if (enemyBossExists) {
            updateFireballs(input, target);
            if (input.wasPressed(Keys.S)) {
                throwFireball(target, getX(), getY(), PROPS);
            }
        }
    }

    /***
     * Method that handles the player's jumping movement.
     * @param input Input provided by the user
     */
    public void jump(Input input) {
        // Player can only jump if they are not dead
        if (!isDead()) {
            // On a platform and up arrow key is pressed
            if (input.wasPressed(Keys.UP) && getY() == baselineY) {
                setVerticalSpeed(INITIAL_JUMP_SPEED);
            }

            // Mid-jump. Increment the player vertical movement by one until it returns back to the vertical baseline
            if (getY() < baselineY) {
                setVerticalSpeed(getVerticalSpeed() + 1);
            }

            // If the next vertical movement downwards in the jump animation will cause the player to fall through
            // the platform, set the player on the platform directly
            if (getVerticalSpeed() + getY() > baselineY) {
                setY(baselineY);
                setFallingFromPlatformStatus(false);
            }

            // Finishing jump. Player is returned back to their starting vertical position and jump ends.
            if (getVerticalSpeed() > 0 && getY() >= baselineY && !isDead()) {
                setVerticalSpeed(0);
                setY(baselineY);
                setFallingFromPlatformStatus(false);
            }

            // Move the player vertically to animate jump
            moveY();
        }
    }

    /**
     * Method that sets the invincibility status of the player and tests if the timer has lapsed
     */
    public void setInvincibilityStatus() {
        invincibilityActive = getInvincibilityTimer() > 0;
    }

    /**
     * Method that returns the status of if the player is currently using invincibility power-up
     * @return boolean Returns invincibility status of player
     */
    public boolean isInvincibilityActive() {
        return invincibilityActive;
    }

    /**
     * Method that sets the invincibility timer for when an Invincible Power entity is encountered to the given
     * maximum frames
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public void setInvincibilityTimer(int MAX_FRAMES) {
        this.invincibilityTimer = MAX_FRAMES;
    }

    /**
     * Method that gets and returns the current invincibility time left on the timer
     * @return int Returns the current time remaining of player invincibility
     */
    public int getInvincibilityTimer() {
        return this.invincibilityTimer;
    }

    /**
     * Method that decrements the invincibility timer by one
     */
    private void decrementInvincibilityTimer() {
        setInvincibilityTimer(getInvincibilityTimer() - 1);
    }

    /**
     * Method that gets and returns the current double score time left on the timer
     * @return int Returns the current time remaining of double score power-up
     */
    public int getDoubleScoreTimer() {
        return this.doubleScoreTimer;
    }

    /**
     * Method that sets the double score timer for when a Double Score entity is encountered to the given
     * maximum frames
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public void setDoubleScoreTimer(int MAX_FRAMES) {
        this.doubleScoreTimer = MAX_FRAMES;
    }

    /**
     * Method that returns the status of if the player is currently using double score power-up by determining if
     * the timer has not fully lapsed
     * @return boolean Returns if the player has an active double score power-up
     */
    public boolean doubleScoreActive() {
        return (doubleScoreTimer > 0);
    }

    /**
     * Method that gets and returns the number of active double score power-ups
     * @return double Returns the number of active double score power-ups
     */
    public double getActiveDoubleScores() {
        return this.activeDoubleScores;
    }

    /**
     * Method that increments the number of active double score power-ups
     */
    public void incrementActiveDoubleScores() {
        this.activeDoubleScores++;
    }

    /**
     * Method that sets the player's baseline to given y to know when to end jump animation
     * @param y New vertical baseline for the player to land at in the jump animation
     */
    public void setBaselineY(double y) {
        this.baselineY = y;
    }

    /**
     * Method that resets the player's vertical baseline to the initial y-value when returning to the main platform
     */
    public void resetBaseline() {
        baselineY = INITIAL_Y;
    }

    /**
     * Method that sets the boolean to indicate if the player is falling from a platform
     * @param fallingFromPlatformStatus Indication of whether the player is or is not falling from a platform
     */
    public void setFallingFromPlatformStatus(boolean fallingFromPlatformStatus) {
        this.fallingFromPlatform = fallingFromPlatformStatus;
    }

    /**
     * Method that gets and returns player status of falling from a platform or not
     * @return boolean Indication of whether the player is or is not falling from a platform
     */
    public boolean isFallingFromPlatform() {
        return this.fallingFromPlatform;
    }

    /***
     * Method that tests sets the status of the player in relation to whether it has reached the left border of
     * the game world
     * @param atPlatformBorderLeft Determines if the player has reached the left border of the game world
     */
    public void setPlatformBorderLeftStatus(boolean atPlatformBorderLeft) {
        this.atPlatformBorderLeft = atPlatformBorderLeft;
    }

    /***
     * Method that tests sets the status of the player in relation to whether it has reached the right border of
     * the game world
     * @param atPlatformBorderRight Determines if the player has reached the right border of the game world
     */
    public void setPlatformBorderRightStatus(boolean atPlatformBorderRight) {
        this.atPlatformBorderRight = atPlatformBorderRight;
    }

    /***
     * Method that gets and returns if the player has reached the left border of the world
     * @return boolean Returns if the player has reached the left border of the game world
     */
    public boolean playerAtBorderLeft() {
        return (this.atPlatformBorderLeft);
    }

    /***
     * Method that gets and returns if the player has reached the right border of the world
     * @return boolean Returns if the player has reached the right border of the game world
     */
    public boolean playerAtBorderRight() {
        return (this.atPlatformBorderRight);
    }
}