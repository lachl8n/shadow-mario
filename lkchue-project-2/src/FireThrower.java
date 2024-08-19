import bagel.Image;
import bagel.Input;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Abstract Super-Class for entities that can throw fireballs and be killed.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public abstract class FireThrower extends Entity implements VerticallyMovable {
    private double health;
    private ArrayList<Fireball> fireballs;
    private final int FALL_SPEED = 2, ACTIVATION_RADIUS;
    private int speedY = 0;

    /**
     * Constructor method for collectable entity class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param RADIUS Collision radius value of given entity
     * @param SPEED_X Horizontal movement speed of entity with player movement
     * @param image Image object of entity to be drawn
     * @param health Starting health of entity
     * @param PROPS Game properties to access information about game object
     */
    public FireThrower(double x, double y, double RADIUS, int SPEED_X, Image image, double health, Properties PROPS) {
        // Calls the superclass constructor to instantiate object
        super(x, y, RADIUS, SPEED_X, image);
        this.fireballs = new ArrayList<Fireball>();
        this.health = health;
        this.ACTIVATION_RADIUS = Integer.parseInt(PROPS.getProperty("gameObjects.enemyBoss.activationRadius"));
    }

    /***
     * Method that updates the fireballs thrown by the entity
     * @param input Input provided by the user
     * @param target Reference to opponent FireThrower object (Player/Enemy Boss) receiving fireballs
     */
    public void updateFireballs(Input input, FireThrower target) {
        for (Fireball f : fireballs) {
            f.updateWithTarget(input, target);
        }
    }

    /***
     * Method that throws fireballs at opposing entity if within activation range. Creates new fireball
     * object and adds it to a list of active fireballs
     * @param target Reference to an opponent FireThrower object (Player/Enemy Boss) to throw fireballs at
     * @param x Horizontal position of this entity in game environment
     * @param y Vertical position of this entity in game environment
     * @param PROPS Game properties to access information about game object
     */
    public void throwFireball(FireThrower target, double x, double y, Properties PROPS) {
        if (canThrowFireball(target, x, y)) {
            Fireball fireball = new Fireball(x, y, PROPS, getTargetDirection(target, x));
            fireballs.add(fireball);
        }
    }

    /***
     * Method that tests if entity is within activation range of their opponent to throw a fireball via
     * calculating Euclidean distance
     * @param target Reference to an opponent FireThrower object (Player/Enemy Boss) to throw fireballs at
     * @param x Horizontal position of this entity in game environment
     * @param y Vertical position of this entity in game environment
     * @return boolean Returns if entity is within fireball activation range of opposing entity
     */
    public boolean canThrowFireball(FireThrower target, double x, double y) {
        return Math.sqrt(Math.pow(target.getX() - x, 2) + Math.pow(target.getY() - y, 2)) < ACTIVATION_RADIUS;
    }

    /***
     * Method that determines the direction of the target to know which direction to throw the fireball, where
     * true represents the target being to the right of this entity and vice versa
     * @param target Reference to a FireThrower object being the fireball's target (Player/Enemy Boss)
     * @param x Horizontal position of this entity in game environment
     * @return boolean Returns boolean representing direction of travel of this fireball
     */
    public boolean getTargetDirection(FireThrower target, double x) {
        return (x <= target.getX());
    }

    /***
     * Method that gets and returns the health of this entity
     * @return double Returns the current health of this entity
     */
    public double getHealth() {
        return this.health;
    }

    /***
     * Method that sets the health of this entity
     * @param health Given health to assign to this entity
     */
    public void setHealth(double health) {
        this.health = health;
    }


    /***
     * Method that determines if the entity has been successfully killed by testing if its health is below or
     * at zero
     * @return boolean Returns if the entity's health is below zero
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    /***
     * Method that determines if the entity has surpassed the window height and therefore concluded the death
     * animation
     * @param WINDOW_HEIGHT Height of the game window
     */
    public boolean concludedDeathAnimation(int WINDOW_HEIGHT) {
        return getY() > WINDOW_HEIGHT;
    }

    /**
     * Method that sets the fall speed to initialise death animation if the entity's health has reached 0,
     * or when the entity has died
     */
    public void dead() {
        this.speedY = FALL_SPEED;
    }

    /***
     * Method that implements vertical movement up and down the window by the given vertical speed
     */
    public void moveY() {
        setY(getY() + this.speedY);
    }

    /***
     * Method that resets all fireball objects by iterating through the thrown fireballs and resetting them
     * individually
     */
    public void resetFireballs() {
        for(Fireball f: fireballs) {
            f.reset();
        }
    }

    /***
     * Method that assigns the entity's vertical speed to the given speed
     * @param speedY Vertical speed of the entity
     */
    public void setVerticalSpeed(int speedY) {
        this.speedY = speedY;
    }

    /***
     * Method that gets and returns the vertical speed of the entity
     * @return int Vertical speed of the entity
     */
    public int getVerticalSpeed() {
        return this.speedY;
    }

}
