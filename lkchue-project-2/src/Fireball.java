import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.Properties;

/**
 * Class for the fireball.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Fireball extends Entity implements HorizontallyMovable {
    private boolean isCollided = false;
    private final boolean DIRECTION;
    private static boolean killedTarget;
    private final double DAMAGE_SIZE;
    private final int GAME_SPEED;

    /**
     * Constructor method for fireball class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param PROPS Game properties to access information about game object
     * @param DIRECTION Boolean indicating the fireball's direction of travel across the window
     */
    public Fireball(double x, double y, Properties PROPS, boolean DIRECTION) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(PROPS.getProperty("gameObjects.fireball.radius")),
                Integer.parseInt(PROPS.getProperty("gameObjects.fireball.speed")),
                new Image(PROPS.getProperty("gameObjects.fireball.image")));

        // Initialise constants to indicate damage size, speed of movement in respect to the player and the
        // fireball's direction of travel
        this.DAMAGE_SIZE = Double.parseDouble(PROPS.getProperty("gameObjects.fireball.damageSize"));
        this.GAME_SPEED = Integer.parseInt(PROPS.getProperty("gameObjects.enemyBoss.speed"));
        this.DIRECTION = DIRECTION;
    }

    /***
     * Method that updates the fireball movement and draws it. Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to an opponent FireThrower object (Player/Enemy Boss) to throw fireballs at
     */
    public void updateWithTarget(Input input, FireThrower target) {
        // Move fireball horizontally at defined speed in game properties but also in respect to the movement of
        // the player via user input
        moveX();
        moveEntityWithPlayer(input, target);

        // Provided the fireball has not collided with the opponent, draw the fireball
        if (!isCollided) {
            drawEntity();
        }

        // Tests if a collision with an entity and a fireball has occurred using the CollisionDetector class,
        // provided the fireball has not collided with this entity prior and the entity is not dead
        if (CollisionDetector.isCollided(target, getX(), getY(), getRADIUS()) && !isCollided && !killedTarget) {
            // Indicate that the fireball has been collided with and damage the player
            isCollided = true;
            damageTarget(target);
        }
    }

    /***
     * Method that moves the fireball horizontally across the window in the direction initially fired in
     */
    public void moveX() {
        // Determines which direction to move the fireball by testing which direction the fireball was initially
        // fired in
        if (DIRECTION) {
            setX(getX() + getSPEED_X());
        } else {
            setX(getX() - getSPEED_X());
        }
    }

    /***
     * Method that damages the given entity. If it is a player, only damages if it is not invincible.
     * If the health of the entity is less than or equal to 0, the entity will be marked as dead.
     * @param target Reference to a FireThrower object being damaged (Player/Enemy Boss)
     */
    private void damageTarget(FireThrower target) {
        // If the given entity is the player, damage can only be inflicted if it is not invincible. Tests this
        // requirement and exits function if the player is invincible
        if (target instanceof Player) {
            if (((Player) target).isInvincibilityActive()) {
                return;
            }
        }

        // Reduce the entity's health by the damage size of the fireball
        double newHealth = target.getHealth() - DAMAGE_SIZE;
        target.setHealth(newHealth);

        // If the entity's health reduces past zero, indicate the entity has died
        if (newHealth <= 0 && !killedTarget) {
            // Due to the fireball's damage, if the damage exceeds the health of the entity, set the health to
            // simply zero to avoid negative health values
            if (newHealth < 0) {
                newHealth = 0;
                target.setHealth(newHealth);
            }
            target.dead();
            killedTarget = true;
        }
    }

    /***
     * Method that resets the fireball by indicating the entity has not been killed
     */
    public void reset() {
        killedTarget = false;
    }

    /***
     * Method that gets and returns whether the fireballs have successfully killed an entity
     * @return boolean Returns status of whether the fireballs have successfully killed an entity
     */
    public boolean getKilledTarget() {
        return killedTarget;
    }

    /***
     * Method that to moves fireball with the player's movement via user input. Entity is moved according to game
     * environment movement rather than speed of fireball.
     * @param input Input provided by the user
     * @param target Reference to a FireThrower object (Player/Enemy Boss)
     */
    public void moveEntityWithPlayer(Input input, FireThrower target) {
        // If the entity is a player, it must determine if the player has reached the border of the game world
        // If it is not a player, it will be false and movement will be valid
        boolean playerAtBorderLeft = false, playerAtBorderRight = false;
        if (target instanceof Player) {
            playerAtBorderLeft = ((Player) target).playerAtBorderLeft();
            playerAtBorderRight = ((Player) target).playerAtBorderRight();
        }

        // Determines which direction to move the entity based on the key provided by the user's input and if the
        // player will not exceed the border limits
        if (input.isDown(Keys.RIGHT) && !playerAtBorderRight){
            setX(getX() - GAME_SPEED);
        } else if (input.isDown(Keys.LEFT) && !playerAtBorderLeft){
            setX(getX() + GAME_SPEED);
        }
    }
}
