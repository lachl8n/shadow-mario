import bagel.*;
import java.util.Properties;

/**
 * Class for the invincibility power-up.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class InvinciblePower extends PowerUp {

    /**
     * Constructor method for invincibility power-up class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public InvinciblePower(double x, double y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.invinciblePower.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.invinciblePower.speed")),
                new Image(PROPS.getProperty("gameObjects.invinciblePower.image")),
                Integer.parseInt(PROPS.getProperty("gameObjects.invinciblePower.maxFrames")));
    }

    /***
     * Method that implements abstract method defined in super-class to execute power-up functionality based
     * on specific power-up. Calls method to initialise invincibility power-up.
     * @param target Reference to the Player object
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public void executePowerUp(Player target, int MAX_FRAMES) {
        initialiseInvinciblePower(target, MAX_FRAMES);
    }

    /***
     * Method that initialises the invincibility power-up by starting the timer and setting the status to indicate
     * that invincibility is active
     * @param target Reference to the Player object
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    private void initialiseInvinciblePower(Player target, int MAX_FRAMES) {
        target.setInvincibilityStatus();
        target.setInvincibilityTimer(MAX_FRAMES);
    }
}
