package ex11Q2;

import java.util.Random;

/**
 * Used for generating random numbers for stochastic processes.
 *
 * @author Eldar Weiss
 */
public class RNGUtilities {
    private static Random _randomNumberGenerator; // Used for generating random (or rather mainly
    // pseudo-random) numbers.

    /**
     * Generates a random integer in the interval [start, end] (interval includes both start and end)
     * and returns it.
     */
    public static int generateRandomInteger(int start, int end) {
        int range = end - start;
        if (_randomNumberGenerator == null)
            _randomNumberGenerator = new Random();
        if (range > 0)
            return _randomNumberGenerator.nextInt(range) + start;
        return start;
    }
}
