import bagel.*;

/**
 * Abstract Super-Class for game objects or entities.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public abstract class Entity {
    private final double RADIUS;
    private final int SPEED_X;
    private double x, y;
    private Image image;

    /**
     * Constructor method for entity class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param RADIUS Collision radius value of given entity
     * @param SPEED_X Horizontal movement speed of entity with player movement
     * @param image Image object of entity to be drawn
     */
    public Entity(double x, double y, double RADIUS, int SPEED_X, Image image) {
        this.x = x;
        this.y = y;
        this.RADIUS = RADIUS;
        this.SPEED_X = SPEED_X;
        this.image = image;
    }

    /***
     * Method that updates an entity's movement in respect to the player and draws it.
     * @param input Input provided by the user
     */
    public void updateEntity(Input input, Player target) {
        // Move the entity horizontally with the player's movement if the player is not dead.
        // Also draws the entity into the game environment.
        if (!target.isDead()) {
            moveEntityWithPlayer(input, target);
        }
        drawEntity();
    }

    /***
     * Method that gets and returns the entity's x-value (horizontal position)
     * @return double Returns the entity's x-value
     */
    public double getX() {
        return this.x;
    }

    /***
     * Method that gets and returns the entity's y-value (vertical position)
     * @return double Returns the entity's y-value
     */
    public double getY() {
        return this.y;
    }

    /***
     * Method that gets and returns the entity's image object
     * @return Image Returns the entity's image object
     */
    public Image getImage() {
        return this.image;
    }

    /***
     * Method that gets and returns the entity's radius value from the game properties
     * @return double Returns the entity's radius
     */
    public double getRADIUS() {
        return this.RADIUS;
    }

    /***
     * Method that gets and returns the entity's horizontal speed in respect to the player
     * @return int Returns the entity's horizontal speed
     */
    public int getSPEED_X() {
        return this.SPEED_X;
    }

    /***
     * Method that assigns the entity's x-value with the given argument
     * @param x Given horizontal position to assign to the entity
     */
    public void setX(double x) {
        this.x = x;
    }

    /***
     * Method that assigns the entity's y-value with the given argument
     * @param y Given vertical position to assign to the entity
     */
    public void setY(double y) {
        this.y = y;
    }

    /***
     * Method that assigns the entity's image object with the given argument
     * @param image Given image object to assign to the entity
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /***
     * Method that updates the entity's horizontal position (x-value) based on the movement of the player via
     * the user input
     * @param input Input provided by the user
     * @param target Reference to the Player object
     */
    public void moveEntityWithPlayer(Input input, Player target) {
        // Determines which direction to move the entity based on the user's input, provided
        // the player is not at the border
        if (input.isDown(Keys.RIGHT) && !target.playerAtBorderRight()){
            this.x -= SPEED_X;
        } else if (input.isDown(Keys.LEFT) && !target.playerAtBorderLeft()){
            this.x += SPEED_X;
        }
    }

    /***
     * Method that draws the entity's image at their current position in the game environment
     */
    public void drawEntity() {
        this.image.draw(x, y);
    }
}
