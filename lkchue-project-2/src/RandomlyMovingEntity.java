import bagel.*;

/**
 * Abstract Super-Class for entities with independent random movement from the player.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public abstract class RandomlyMovingEntity extends Entity implements HorizontallyMovable {
    private double randomSpeed, cumulativeDisplacement = 0;
    private final double MAX_DISPLACEMENT;

    /**
     * Constructor method for randomly moving entity class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param RADIUS Collision radius value of given entity
     * @param SPEED_X Horizontal movement speed of entity with player movement
     * @param image Image object of entity to be drawn
     * @param randomSpeed Speed of movement for horizontal random movement
     * @param MAX_DISPLACEMENT Maximum horizontal displacement from original position in random movement
     */
    public RandomlyMovingEntity(double x, double y, double RADIUS, int SPEED_X, Image image, double randomSpeed,
                                double MAX_DISPLACEMENT) {
        // Calls the superclass constructor to instantiate object
        super(x, y, RADIUS, SPEED_X, image);

        // Initialise maximum displacement constant and the random movement speed of the entity
        this.randomSpeed = randomSpeed;
        this.MAX_DISPLACEMENT = MAX_DISPLACEMENT;
    }

    /***
     * Method that updates the random movement of the entity to move horizontally independent of the player,
     * switching directions whenever the maximum displacement has been reached.
     */
    public void updateRandomMovement() {
        // If the entity has reached the maximum displacement from its origin, switch direction to move in the
        // opposite direction
        if (atRandomMovementBorder()) {
            switchDirection();
        }

        // Increment current displacement by random horizontal movement speed and move entity accordingly
        cumulativeDisplacement += randomSpeed;
        moveX();
    }

    /***
     * Method that implements horizontal movement method. Increments the horizontal position of the entity by the
     * random movement speed
     */
    public void moveX() {
        setX(getX() + randomSpeed);
    }

    /***
     * Method that determines if the entity has reached its maximum displacement on either side of its original
     * position during its random horizontal movement
     * @return boolean Returns if the entity has reached maximum displacement in horizontal random movement
     */
    private boolean atRandomMovementBorder() {
        return (atRandomMovementBorderLeft() || atRandomMovementBorderRight());
    }

    /***
     * Method that changes the entity's direction of movement to move in the opposite direction
     */
    private void switchDirection() {
        randomSpeed = -(randomSpeed);
    }

    /***
     * Method that determines if the entity has reached its maximum displacement on the right side of its original
     * position during its random horizontal movement
     * @return boolean Returns if the entity has reached right maximum displacement in horizontal random movement
     */
    private boolean atRandomMovementBorderRight() {
        return cumulativeDisplacement >= MAX_DISPLACEMENT;
    }

    /***
     * Method that determines if the entity has reached its maximum displacement on the left side of its original
     * position during its random horizontal movement
     * @return boolean Returns if the entity has reached left maximum displacement in horizontal random movement
     */
    private boolean atRandomMovementBorderLeft() {
        return cumulativeDisplacement <= -MAX_DISPLACEMENT;
    }
}
