import bagel.Image;
import java.util.Properties;

/**
 * Class for the double score power-up.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class DoubleScore extends PowerUp {
    /**
     * Constructor method for double score power-up class to initialise object when instantiated
     * @param x Horizontal position of entity in game environment
     * @param y Vertical position of entity in game environment
     * @param props Game properties to access information about game object
     */
    public DoubleScore(double x, double y, Properties props) {
        // Calls the superclass constructor to instantiate object
        super(x, y, Double.parseDouble(props.getProperty("gameObjects.doubleScore.radius")),
                Integer.parseInt(props.getProperty("gameObjects.doubleScore.speed")),
                new Image(props.getProperty("gameObjects.doubleScore.image")),
                Integer.parseInt(props.getProperty("gameObjects.doubleScore.maxFrames")));
    }

    /***
     * Method that implements abstract method defined in super-class to execute power-up functionality based
     * on specific power-up. Calls method to initialise double score power-up.
     * @param target Reference to the Player object
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    public void executePowerUp(Player target, int MAX_FRAMES) {
        initialiseDoubleScore(target, MAX_FRAMES);
    }

    /***
     * Method that initialises the double score power-up by starting the timer and incrementing how many double
     * score power-ups are active
     * @param target Reference to the Player object
     * @param MAX_FRAMES Maximum number of frames for power-up to last to initialise timer
     */
    private void initialiseDoubleScore(Player target, int MAX_FRAMES) {
        target.setDoubleScoreTimer(MAX_FRAMES);
        target.incrementActiveDoubleScores();
    }
}
