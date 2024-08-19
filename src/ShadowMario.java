import bagel.*;
import java.util.Properties;

/**
 * My Solution for SWEN20003 Project 2, Semester 1, 2024
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class ShadowMario extends AbstractGame {
    private final int WINDOW_HEIGHT;
    private final String GAME_TITLE;
    private final Image BACKGROUND_IMAGE;
    private final String FONT_FILE;
    private final Font TITLE_FONT;
    private final int TITLE_X;
    private final int TITLE_Y;
    private final String INSTRUCTION;
    private final Font INSTRUCTION_FONT;
    private final int INS_Y;
    private final Font MESSAGE_FONT;
    private final int MESSAGE_Y;
    private final Properties PROPS, MESSAGE_PROPS;
    private boolean finished = false, started = false;
    private Level level;

    /**
     * Constructor method for Shadow Mario class to initialise object when instantiated
     * @param game_props Game properties to access information about game object
     * @param message_props Message properties to access message values for Font objects
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
                Integer.parseInt(game_props.getProperty("windowHeight")),
                message_props.getProperty("title"));

        // Get and store all necessary game and message properties
        WINDOW_HEIGHT = Integer.parseInt(game_props.getProperty("windowHeight"));
        GAME_TITLE = message_props.getProperty("title");
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        FONT_FILE = game_props.getProperty("font");

        // Instantiate all Font objects to display on game window. Store all relevant information (e.g. position)
        TITLE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("title.fontSize")));
        TITLE_X = Integer.parseInt(game_props.getProperty("title.x"));
        TITLE_Y = Integer.parseInt(game_props.getProperty("title.y"));

        INSTRUCTION = message_props.getProperty("instruction");
        INSTRUCTION_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("instruction.fontSize")));
        INS_Y = Integer.parseInt(game_props.getProperty("instruction.y"));

        MESSAGE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("message.fontSize")));
        MESSAGE_Y = Integer.parseInt(game_props.getProperty("message.y"));

        // Store game and message properties as constants
        this.PROPS = game_props;
        this.MESSAGE_PROPS = message_props;

        // Instantiate new Level object
        level = new Level(game_props, message_props);
    }

    /**
     * The entry point for the program.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * @param input Input provided by the user
     */
    @Override
    protected void update(Input input) {
        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Draws the background image in the game environment
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // If the game has not been started by the user, display the start screen and set the relevant level
            // based on the user's input
            displayStart();

            if (level.setLevel(input)) {
                // When a level has been selected, indicate the game has started
                started = true;
                finished = false;
            }
        } else {
            // Determine the current state of the game
            if (finished) {
                // If the game is finished, display the end screen to indicate game victory
                endGame(input, "gameWon");
            } else if (level.gameOver(WINDOW_HEIGHT)) {
                // If the player was killed in the game, display game over screen
                endGame(input, "gameOver");
            } else {
                // Otherwise, continue running the game loop and render the game environment
                level.renderEnvironment(input);

                // If the player successfully reaches the End Flag, the game is won
                if (level.endFlagCollided()) {
                    finished = true;
                }
            }
        }
    }

    /***
     * Method that displays the end screen of the game and prompts user to reset the game
     * @param input Input provided by the user
     * @param messageValue String directory in message properties to locate message to display on the screen
     */
    private void endGame(Input input, String messageValue) {
        // Find relevant message depending on if the game is won or lost and prompt player to restart
        String message = MESSAGE_PROPS.getProperty(messageValue);
        MESSAGE_FONT.drawString(message, Window.getWidth() / 2 - MESSAGE_FONT.getWidth(message)/2, MESSAGE_Y);
        if (input.wasPressed(Keys.SPACE)) {
            level.reset();
            started = false;
        }
    }

    /***
     * Method that displays the start screen of the game
     */
    private void displayStart() {
        // Draw title and starting instructions of the game on the screen
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION,
                Window.getWidth() / 2 - INSTRUCTION_FONT.getWidth(INSTRUCTION)/2, INS_Y);
    }
}