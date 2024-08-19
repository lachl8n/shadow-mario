import bagel.*;
import bagel.util.Colour;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class for the game level.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class Level {
    private final String FONT_FILE;
    private final Font SCORE_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final Font HEALTH_FONT;
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private final Font BOSS_HEALTH_FONT;
    private final int BOSS_HEALTH_X;
    private final int BOSS_HEALTH_Y;
    private int score;
    private Player player;
    private Platform platform;
    private EnemyBoss enemyBoss;
    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;
    private ArrayList<InvinciblePower> invinciblePowers;
    private ArrayList<DoubleScore> doubleScores;
    private ArrayList<FlyingPlatform> flyingPlatforms;
    private EndFlag endFlag;
    private final Properties PROPS, MESSAGE_PROPS;
    private boolean enemyBossExists = false;
    DrawOptions options;

    /**
     * Constructor method for level class to initialise object when instantiated
     * @param game_props Game properties to access information about game object
     * @param message_props Message properties to access message values for Font objects
     */
    public Level(Properties game_props, Properties message_props) {
        // Initialise Font objects to display text in window during game run-through
        FONT_FILE = game_props.getProperty("font");

        SCORE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("score.fontSize")));
        SCORE_X = Integer.parseInt(game_props.getProperty("score.x"));
        SCORE_Y = Integer.parseInt(game_props.getProperty("score.y"));

        HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("playerHealth.fontSize")));
        HEALTH_X = Integer.parseInt(game_props.getProperty("playerHealth.x"));
        HEALTH_Y = Integer.parseInt(game_props.getProperty("playerHealth.y"));

        BOSS_HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("enemyBossHealth.fontSize")));
        BOSS_HEALTH_X = Integer.parseInt(game_props.getProperty("enemyBossHealth.x"));
        BOSS_HEALTH_Y = Integer.parseInt(game_props.getProperty("enemyBossHealth.y"));

        // Initialise options to change colour of Enemy Boss health
        options = new DrawOptions();

        // Initialise constants to refer back to the game and message properties
        PROPS = game_props;
        MESSAGE_PROPS = message_props;
    }

    /***
     * Method that renders the game environment for a given game run-through. Called for each frame via the update
     * method
     * @param input Input provided by the user
     */
    public void renderEnvironment(Input input) {
        // Display score and health objects
        SCORE_FONT.drawString(MESSAGE_PROPS.getProperty("score") + score, SCORE_X, SCORE_Y);
        HEALTH_FONT.drawString(MESSAGE_PROPS.getProperty("health") + Math.round(player.getHealth()*100),
                HEALTH_X, HEALTH_Y);
        if (enemyBossExists) {
            // Display Enemy Boss health only if it exists in the level. Display in red.
            BOSS_HEALTH_FONT.drawString(MESSAGE_PROPS.getProperty("health") + Math.round(enemyBoss.getHealth() * 100),
                    BOSS_HEALTH_X, BOSS_HEALTH_Y, options);
            options.setBlendColour(Colour.RED);
        }
        // Update all game objects for each frame
        updateGameObjects(input);
    }

    /**
     * Method that reads a level file depending on the selected level by the user and populates the game environment
     * with the relevant game objects
     * @param input Input provided by the user
     * @return boolean Indicates if a level has been selected by the user
     */
    public boolean setLevel(Input input) {
        // Determine which level file to read depending on the user input and initialises the game using
        // the relevant CSV level file. User selects level via number.
        if (input.wasPressed(Keys.NUM_1)) {
            List<String[]> lines = IOUtils.readCsv(PROPS.getProperty("level1File"));
            initialiseLevel(lines);
            return true;
        } else if (input.wasPressed(Keys.NUM_2)) {
            List<String[]> lines = IOUtils.readCsv(PROPS.getProperty("level2File"));
            initialiseLevel(lines);
            return true;
        } else if (input.wasPressed(Keys.NUM_3)) {
            List<String[]> lines = IOUtils.readCsv(PROPS.getProperty("level3File"));
            initialiseLevel(lines);
            return true;
        }
        return false;
    }

    /**
     * Method that initialises all level properties, sets the borders and populates the game environment with
     * entities from the CSV level file
     * @param lines List of lines read from the CSV level file
     */
    private void initialiseLevel(List<String[]> lines) {
        // Initialise score value and populate game environment with entities
        score = 0;
        populateGameObjects(lines);
    }

    /**
     * Method that increases the current score by the value of the coin given as a parameter.
     * @param coinValue Numerical value of coin
     */
    public void setScore(int coinValue) {
        // Increment current score by the coin value. This will only hold a value if a coin is collided.
        this.score += coinValue;
    }

    /**
     * Method that updates the game objects each frame, when the game is running.
     * @param input Input provided by the user
     */
    public void updateGameObjects(Input input) {
        // Calls update methods for all game objects in level
        platform.updateWithTarget(input, player);

        for(Enemy e: enemies) {
            e.updateWithTarget(input, player);
        }

        for(Coin c: coins) {
            setScore(c.updateWithTarget(input, player));
        }

        for(DoubleScore d: doubleScores) {
            d.updateWithTarget(input, player);
        }

        for(InvinciblePower i: invinciblePowers) {
            i.updateWithTarget(input, player);
        }

        for(FlyingPlatform f: flyingPlatforms) {
            f.updateWithTarget(input, player);
        }

        player.update(input, enemyBoss, enemyBossExists);
        endFlag.updateWithTarget(input, player);

        // Only update the Enemy Boss entity if it exists in the level.
        if (enemyBossExists) {
            enemyBoss.updateWithTarget(input, player, PROPS);
        }
    }


    /**
     * Method that creates the game objects using the lines read from the CSV file.
     * @param lines List of lines read from the CSV level file
     */
    private void populateGameObjects(List<String[]> lines) {
        // Create ArrayLists for all entities with multiple occurrences in the level
        coins = new ArrayList<Coin>();
        enemies = new ArrayList<Enemy>();
        doubleScores = new ArrayList<DoubleScore>();
        invinciblePowers = new ArrayList<InvinciblePower>();
        flyingPlatforms = new ArrayList<FlyingPlatform>();

        // Iterate through list of CSV level file lines and instantiate new entities for each line
        // For entities with multiple occurrences, add these objects to the ArrayLists
        for(String[] lineElement: lines) {
            int x = Integer.parseInt(lineElement[1]);
            int y = Integer.parseInt(lineElement[2]);

            if (lineElement[0].equals("PLAYER")) {
                player = new Player(x, y, this.PROPS);
            } else if (lineElement[0].equals("PLATFORM")) {
                platform = new Platform(x, y, this.PROPS);
            } else if (lineElement[0].equals("ENEMY")) {
                Enemy enemy = new Enemy(x, y, PROPS);
                enemies.add(enemy);
            } else if (lineElement[0].equals("COIN")) {
                Coin coin = new Coin(x, y, PROPS);
                coins.add(coin);
            } else if (lineElement[0].equals("END_FLAG")) {
                endFlag = new EndFlag(x, y, PROPS);
            } else if (lineElement[0].equals("DOUBLE_SCORE")) {
                DoubleScore doubleScore = new DoubleScore(x, y, PROPS);
                doubleScores.add(doubleScore);
            } else if (lineElement[0].equals("INVINCIBLE_POWER")) {
                InvinciblePower invinciblePower = new InvinciblePower(x, y, PROPS);
                invinciblePowers.add(invinciblePower);
            } else if (lineElement[0].equals("ENEMY_BOSS")) {
                enemyBoss = new EnemyBoss(x, y, PROPS);
                // If an Enemy Boss entity is created, indicate that it exists
                enemyBossExists = true;
            } else if (lineElement[0].equals("FLYING_PLATFORM")) {
                FlyingPlatform flyingPlatform = new FlyingPlatform(x, y, PROPS);
                flyingPlatforms.add(flyingPlatform);
            }
        }
    }

    /**
     * Method that determines if the game has ended by testing if the player is dead and the death animation has
     * concluded (via testing its vertical position in the game environment)
     * @param WINDOW_HEIGHT Height of the game window
     */
    public boolean gameOver(int WINDOW_HEIGHT) {
        return (player.isDead() && player.concludedDeathAnimation(WINDOW_HEIGHT));
    }

    /**
     * Method that determines if the player has reached the flag to indicate if the game has been won
     * @return boolean Returns if the player has collided with the flag and won the game
     */
    public boolean endFlagCollided() {
        // If there is an Enemy Boss, the player must defeat it before they can cross the End Flag and win the game
        if (enemyBossExists) {
            if (enemyBoss.isDead()) {
                return endFlag.isCollided();
            } else {
                // If the boss is still alive, the collision is reset and the game is not won
                endFlag.resetCollision();
                return false;
            }
        } else {
            return endFlag.isCollided();
        }
    }

    /**
     * Method that resets the level by resetting the fireballs of all FireThrower entities
     * (i.e. Player and Enemy Boss)
     */
    public void reset() {
        player.resetFireballs();

        if (enemyBossExists) {
            enemyBoss.resetFireballs();
        }

        // Reset Enemy Boss existence for next level
        enemyBossExists = false;
    }
}
