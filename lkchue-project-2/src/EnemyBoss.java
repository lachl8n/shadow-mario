import bagel.Image;
import bagel.Input;
import java.util.Random;
import java.util.Properties;

/**
 * Class for the enemy boss.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class EnemyBoss extends FireThrower {
    private int fireballTimer = 0;
    private final int FIREBALL_COOLDOWN_LIMIT = 100;

    /**
     * Constructor method for enemy boss class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public EnemyBoss(int x, int y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.enemyBoss.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.enemyBoss.speed")),
                new Image(PROPS.getProperty("gameObjects.enemyBoss.image")),
                Double.parseDouble(PROPS.getProperty("gameObjects.enemyBoss.health")), PROPS);
    }

    /***
     * Method that updates the enemy boss movement and draws it. Also updates thrown fireballs and randomly
     * throws fireballs at the player
     * @param input Input provided by the user
     * @param target Reference to the Player object
     * @param PROPS Game properties to access information about game object
     */
    public void updateWithTarget(Input input, Player target, Properties PROPS) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Moves the player vertically to animate death
        moveY();

        // Increment fireball cool-down timer for each frame of the game
        incrementTimer();

        // Update the movement of the fireball
        updateFireballs(input, target);

        // Once the cool-down period has lapsed, the Enemy Boss randomly throws a fireball and the timer restarts
        if (fireballTimer >= FIREBALL_COOLDOWN_LIMIT) {
            if (getRandomBoolean()) {
                throwFireball(target, getX(), getY(), PROPS);
            }
            fireballTimer = 0;
        }
    }

    /***
     * Method that increments the timer cool-down for fireball throwing
     */
    private void incrementTimer() {
        this.fireballTimer++;
    }

    /***
     * Method that randomly determines if a fireball is going to be thrown
     * @return boolean Returns a random boolean value, being either true or false
     */
    private boolean getRandomBoolean() {
        // Uses Random to find a random Boolean value
        Random random = new Random();
        return random.nextBoolean();
    }
}
