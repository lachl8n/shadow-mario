import bagel.*;

/**
 * Abstract Super-Class for power-up collectable entities.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public abstract class PowerUp extends Collectable {
    private boolean isCollided = false;
    private final int MAX_FRAMES;

    /**
     * Constructor method for power-up class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param RADIUS Collision radius value of given entity
     * @param SPEED_X Horizontal movement speed of entity with player movement
     * @param image Image object of entity to be drawn
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public PowerUp(double x, double y, double RADIUS, int SPEED_X, Image image, int MAX_FRAMES) {
        // Calls the superclass constructor to instantiate object
        super(x, y, RADIUS, SPEED_X, image);

        // Initialise constant of maximum number of frames for the power-up to last
        this.MAX_FRAMES = MAX_FRAMES;
    }

    /***
     * Method that updates the power-up entity movement and draws it. Also checks for collisions with the player.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void updateWithTarget(Input input, Player target) {
        // Moves entity in accordance with player movement and draws entity
        updateEntity(input, target);

        // Tests if a collision with the player and a power-up has occurred using the CollisionDetector class
        if (CollisionDetector.isCollided(target, getX(), getY(), getRADIUS()) && !isCollided) {
            // If collision occurs, start moving power-up entity vertically upwards and execute relevant
            // power-up
            isCollided = true;
            setSpeedY();
            executePowerUp(target, MAX_FRAMES);
        }
    }

    /***
     * Method that defines abstract method to execute power-up functionality based on specific power-up.
     * @param target Reference to the Player object
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public abstract void executePowerUp(Player target, int MAX_FRAMES);
}
