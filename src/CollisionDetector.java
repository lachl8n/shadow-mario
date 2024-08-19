/**
 * Class that handles the collision detection.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class CollisionDetector {
    /**
     * Method that checks for a collision between the fire thrower entity (player/enemy boss) and the given entity's
     * position.
     * @param target Reference to the FireThrower object, being either Player or Enemy Boss
     * @param x Horizontal position of given entity's position
     * @param y Vertical position of given entity's position
     * @param radius Collision radius value of given entity
     * @return boolean Indicates if a collision has occurred
     */
    public static boolean isCollided(FireThrower target, double x, double y, double radius) {
        return Math.sqrt(Math.pow(target.getX() - x, 2) +
                Math.pow(target.getY() - y, 2)) <= target.getRADIUS() + radius;
    }
}