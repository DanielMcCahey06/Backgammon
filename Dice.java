import java.util.*;

public class Dice {
   private static final Random random = new Random();
   private final int[] diceValues = new int[2];

   // Constructor
    public Dice() {
    }

    public int[] roll() {
        diceValues[0] = random.nextInt(6) + 1;
        diceValues[1] = random.nextInt(6) + 1;
        return new int[]{diceValues[0], diceValues[1]};
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

    public void setDice(int val1, int val2) {
        diceValues[0] = val1;
        diceValues[1] = val2;
    }

    public int[] getSetDice(){
        return diceValues;
    }

}
