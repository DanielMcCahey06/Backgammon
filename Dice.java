import java.util.*;

public class Dice {
   private static final Random random = new Random();

   // Constructor
    public Dice() {
    }

    public static int[] roll() {
        int dice1Value = random.nextInt(6) + 1;
        int dice2Value = random.nextInt(6) + 1;
        return new int[]{dice1Value, dice2Value};
    }

    public static String diceFace(int value) {
        return switch (value) {
            case 1 -> "1⚀";
            case 2 -> "2⚁";
            case 3 -> "3⚂";
            case 4 -> "4⚃";
            case 5 -> "5⚄";
            case 6 -> "6⚅";
            default -> "";
        };
    }

    public static int[] roll(int val1, int val2) {
        return new int[]{val1, val2};
    }
}
