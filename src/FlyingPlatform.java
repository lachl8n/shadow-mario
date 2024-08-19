import bagel.Image;
import bagel.Input;
import java.lang.Math;
import java.util.Properties;

/**
 * Class for the flying platform.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class FlyingPlatform extends RandomlyMovingEntity {
    private final double HALF_LENGTH, HALF_HEIGHT;
    private boolean onPlatform;

    /**
     * Constructor method for flying platform class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public FlyingPlatform(int x, int y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, 0, Integer.parseInt(PROPS.getProperty("gameObjects.flyingPlatform.speed")),
                new Image(PROPS.getProperty("gameObjects.flyingPlatform.image")),
                Double.parseDouble(PROPS.getProperty("gameObjects.flyingPlatform.randomSpeed")),
                Double.parseDouble(PROPS.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX")));

        // Initialise constants stored in game properties to determine if player successfully lands on platform
        this.HALF_LENGTH = Double.parseDouble(PROPS.getProperty("gameObjects.flyingPlatform.halfLength"));
        this.HALF_HEIGHT = Double.parseDouble(PROPS.getProperty("gameObjects.flyingPlatform.halfHeight"));
    }

    /***
     * Method that updates the flying platform movement in relation to the player, but also randomly, and draws it.
     * Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void updateWithTarget(Input input, Player target) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Moves entity in random direction
        updateRandomMovement();

        // Tests if the player fulfils the requirements to land on a flying platform.
        // If requirements are fulfilled, player's baseline vertical position is set to platform position
        if (playerOnPlatformVertical(target) && playerOnPlatformHorizontal(target) &&
                !target.isFallingFromPlatform()) {
            target.setBaselineY(getY() - HALF_HEIGHT);
            target.setY(getY() - HALF_HEIGHT);
            onPlatform = true;
        }

        // If the player is on the platform but walks horizontally off the platform, return the player back to
        // their original vertical position on the ground. Ensure it cannot fall onto another platform
        if (onPlatform && !playerOnPlatformHorizontal(target) && playerOnPlatformVertical(target)) {
            target.setFallingFromPlatformStatus(true);
            target.resetBaseline();
            onPlatform = false;
        }
    }

    /***
     * Method that calculates and returns the horizontal distance from the flying platform and the player
     * @param target Reference to the Player object
     * @return double Returns horizontal distance between the flying platform and the player
     */
    public double findXDistance(Player target) {
        return Math.abs(getX() - target.getX());
    }

    /***
     * Method that calculates and returns the vertical distance from the flying platform and the player
     * @param target Reference to the Player object
     * @return double Returns vertical distance between the flying platform and the player
     */
    public double findYDistance(Player target) {
        return Math.abs(getY() - target.getY());
    }

    /***
     * Method that determines if the player has fulfilled the horizontal position condition to land on the flying
     * platform
     * @param target Reference to the Player object
     * @return boolean Returns success or failure to fulfil horizontal position condition
     */
    private boolean playerOnPlatformHorizontal(Player target) {
        return (findXDistance(target) < HALF_LENGTH);
    }

    /***
     * Method that determines if the player has fulfilled the vertical position condition to land on
     * the flying platform
     * @param target Reference to the Player object
     * @return boolean Returns success or failure to fulfil vertical position condition
     */
    private boolean playerOnPlatformVertical(Player target) {
        return (findYDistance(target) <= HALF_HEIGHT) && (findYDistance(target) >= HALF_HEIGHT - 1);
    }

}
