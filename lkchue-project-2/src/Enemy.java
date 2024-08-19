import bagel.Image;
import bagel.Input;
import java.util.Properties;

/**
 * Class for the enemy.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Enemy extends RandomlyMovingEntity {
    private final double DAMAGE_SIZE;
    private boolean killedTarget = false, hitPlayer = false;

    /**
     * Constructor method for enemy class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public Enemy(double x, double y, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.enemy.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.enemy.speed")),
                new Image(PROPS.getProperty("gameObjects.enemy.image")),
                Double.parseDouble(PROPS.getProperty("gameObjects.enemy.randomSpeed")),
                Double.parseDouble(PROPS.getProperty("gameObjects.enemy.maxRandomDisplacementX")));

        // Initialise damage size constant to inflict on player
        this.DAMAGE_SIZE = Double.parseDouble(PROPS.getProperty("gameObjects.enemy.damageSize"));
    }

    /***
     * Method that updates the enemy movement and draws it. Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void updateWithTarget(Input input, Player target) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Moves entity in random direction
        updateRandomMovement();

        // Tests if a collision with the player and the enemy has occurred using the CollisionDetector class.
        // Condition also requires the player to not be invincible and to have not collided with this entity prior.
        if (target != null && CollisionDetector.isCollided(target, getX(), getY(), getRADIUS()) && !hitPlayer &&
                !target.isInvincibilityActive()) {
            // Indicate that the enemy has been collided with and damage the player
            hitPlayer = true;
            damageTarget(target);
        }
    }

    /***
     * Method that damages the player provided the player is not currently invincible.
     * If the health of the player is less than or equal to 0, the player will be marked as dead.
     * @param target Reference to the Player object
     */
    private void damageTarget(Player target) {
        if (!target.isInvincibilityActive()) {
            // If player is not invincible, reduce the player's health by the damage size of the entity
            double newHealth = target.getHealth() - DAMAGE_SIZE;
            target.setHealth(newHealth);

            // If the player's health reduces past zero, indicate the player has died
            if (newHealth <= 0 && !killedTarget) {
                target.dead();
                killedTarget = true;
            }
        }
    }
}