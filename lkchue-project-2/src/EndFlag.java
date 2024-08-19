import bagel.Image;
import bagel.Input;
import java.util.Properties;

/**
 * Class for the end flag entity.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class EndFlag extends Entity {
    private boolean isCollided = false;

    /**
     * Constructor method for end flag to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public EndFlag(double x, double y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.endFlag.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.endFlag.speed")),
                new Image(PROPS.getProperty("gameObjects.endFlag.image")));
    }

    /***
     * Method that updates the end flag movement and draws it. Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void updateWithTarget(Input input, Player target) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Tests if a collision with the player and the End Flag has occurred using the CollisionDetector class.
        if (CollisionDetector.isCollided(target, getX(), getY(), getRADIUS()) && !isCollided) {
            isCollided = true;
        }
    }

    /***
     * Method that gets and returns whether the player has collided with the end flag entity
     * @return boolean Returns the collision status of the end flag to indicate if the player has collided with it
     */
    public boolean isCollided() {
        return this.isCollided;
    }

    /***
     * Method that resets the collision status to 'false'. Prevents the player from finishing the game before the
     * enemy boss is defeated
     */
    public void resetCollision() {
        this.isCollided = false;
    }
}