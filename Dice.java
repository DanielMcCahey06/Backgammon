import java.util.*;

public class Dice {
    private static ArrayList<Integer> dice1;
    private static ArrayList<Integer> dice2;
    private static final Random random = new Random();

    public Dice() {
        dice1 = new ArrayList<>();
        dice2 = new ArrayList<>();
        initialiseDice();
    }

    private void initialiseDice() {
        for (int i=1; i <= 6; i++) {
            dice1.add(i);
            dice2.add(i);
        }
    }

    public static int[] roll() {
        int dice1Value = dice1.get(random.nextInt(6));
        int dice2Value = dice2.get(random.nextInt(6));
        return new int[]{dice1Value, dice2Value};
    }
}
