import bagel.*;

/**
 * Abstract Super-Class for collectable entities.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public abstract class Collectable extends Entity implements VerticallyMovable {
    private final int COLLISION_SPEED = -10;
    private int speedY = 0;

    /**
     * Constructor method for collectable entity class to initialise object when
     * instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param RADIUS Collision radius value of given entity
     * @param SPEED_X Horizontal movement speed of entity with player movement
     * @param image Image object of entity to be drawn
     */
    public Collectable(double x, double y, double RADIUS, int SPEED_X, Image image) {
        // Calls the superclass constructor to instantiate object
        super(x, y, RADIUS, SPEED_X, image);
    }

    /***
     * Method that sets the vertical speed of the collectable item to move it upwards after its collected
     */
    public void setSpeedY() {
        speedY = COLLISION_SPEED;
    }

    /***
     * Method that updates the movement of the collectable to move vertically up every frame and move horizontally
     * with the player. Also draws the collectable entity.
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    @Override
    public void updateEntity(Input input, Player target) {
        // Move the entity horizontally with the player's movement if the player is not dead and update its
        // vertical movement. Draws the entity into the game environment.
        if (!target.isDead()) {
            moveEntityWithPlayer(input, target);
        }
        moveY();
        drawEntity();
    }

    /***
     * Method that implements vertical movement method that increments the vertical position of the entity by the
     * vertical speed
     */
    public void moveY() {
        setY(getY() + speedY);
    }
}
