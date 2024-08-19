import bagel.Image;
import bagel.Input;
import java.util.Properties;

/**
 * Class for the coin entity.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Coin extends Collectable {
    private final int VALUE;
    private boolean isCollided = false;

    /**
     * Constructor method for coin class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public Coin(double x, double y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.coin.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.coin.speed")),
                new Image(PROPS.getProperty("gameObjects.coin.image")));

        // Assigns coin with value stored in game properties file
        this.VALUE = Integer.parseInt(PROPS.getProperty("gameObjects.coin.value"));
    }

    /***
     * Method that updates the coin movement and draws it. Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     * @return int Value to be added towards the total score
     */
    public int updateWithTarget(Input input, Player target) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Tests if a collision with the player and the coin has occurred using the CollisionDetector class
        if (CollisionDetector.isCollided(target, getX(), getY(), getRADIUS()) && !isCollided) {
            // If collision occurs, start moving coin vertically upwards
            isCollided = true;
            setSpeedY();

            // Tests if double score power-up is active to determine coin collection value
            if (target.doubleScoreActive()) {
                return 2 * VALUE;
            }
            return VALUE;
        }
        return 0;
    }


}