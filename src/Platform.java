import bagel.Image;
import bagel.Input;
import bagel.Window;
import java.util.Properties;

/**
 * Class for the platform entity.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Platform extends Entity {
    private final double LEFT_BORDER, RIGHT_BORDER;

    /**
     * Constructor method for platform class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public Platform(double x, double y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, 0, Integer.parseInt(PROPS.getProperty("gameObjects.platform.speed")),
                new Image(PROPS.getProperty("gameObjects.platform.image")));

        // Initialise border constants to test if player has reached a border
        LEFT_BORDER = x;
        RIGHT_BORDER = x - getImage().getWidth() + Window.getWidth();
    }

    /***
     * Method that updates the platform movement and draws it.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void updateWithTarget(Input input, Player target) {
        // Updates the platform's position in relation to the player's movement
        updateEntity(input, target);

        // Sets the status of the player to indicate if the border has been reached
        target.setPlatformBorderLeftStatus(atLeftBorder(getX()));
        target.setPlatformBorderRightStatus(atRightBorder(getX()));
    }

    /***
     * Method that gets and returns if the platform has reached its maximum movement to the left, indicating
     * the player has reached the left border of the game world
     * @param x Horizontal position of platform in game environment
     * @return boolean Returns if the player has reached the left border of the game world
     */
    private boolean atLeftBorder(double x) {
        return (x >= LEFT_BORDER);
    }

    /***
     * Method that gets and returns if the platform has reached its maximum movement to the right, indicating
     * the player has reached the right border of the game world
     * @param x Horizontal position of platform in game environment
     * @return boolean Returns if the player has reached the right border of the game world
     */
    private boolean atRightBorder(double x) {
        return (x <= RIGHT_BORDER);
    }
}