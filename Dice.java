import java.util.*;

public class Dice {
    private static final Random random = new Random(); // Random object to generate dice rolls
    private final int[] diceValues = new int[2]; // Array to store the two dice values

    // Constructor to initialize Dice object
    public Dice() {
    }

    // Rolls two dice and returns their values
    public int[] roll() {
        diceValues[0] = random.nextInt(6) + 1; // Generate random value for the first die (1-6)
        diceValues[1] = random.nextInt(6) + 1; // Generate random value for the second die (1-6)
        return new int[]{diceValues[0], diceValues[1]}; // Return the rolled values as an array
    }

    // Returns a string representation of a dice face based on its value
    public static String diceFace(int value) {
        return switch (value) {
            case 1 -> "1⚀"; // Unicode for die face 1
            case 2 -> "2⚁"; // Unicode for die face 2
            case 3 -> "3⚂"; // Unicode for die face 3
            case 4 -> "4⚃"; // Unicode for die face 4
            case 5 -> "5⚄"; // Unicode for die face 5
            case 6 -> "6⚅"; // Unicode for die face 6
            default -> ""; // Default for invalid value
        };
    }

    // Manually sets the values of the dice
    public void setDice(int val1, int val2) {
        diceValues[0] = val1; // Set the first die value
        diceValues[1] = val2; // Set the second die value
    }

    // Returns the current values of the dice
    public int[] getSetDice() {
        return diceValues; // Return the current dice values
    }
}
