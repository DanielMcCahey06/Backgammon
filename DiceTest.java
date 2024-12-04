import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    private Dice dice;

    @BeforeEach
    void setUp() {
        dice = new Dice();  // Initialize the Dice object before each test
    }

    @Test
    void testRoll() {
        int[] rollResult = dice.roll();

        // Ensure that the roll result contains two values
        assertEquals(2, rollResult.length);

        // Ensure both dice values are between 1 and 6 (inclusive)
        assertTrue(rollResult[0] >= 1 && rollResult[0] <= 6, "Dice value should be between 1 and 6");
        assertTrue(rollResult[1] >= 1 && rollResult[1] <= 6, "Dice value should be between 1 and 6");
    }

    @Test
    void testSetDice() {
        dice.setDice(3, 5);
        int[] setDiceValues = dice.getSetDice();

        // Check if the set dice values are correctly set
        assertEquals(3, setDiceValues[0]);
        assertEquals(5, setDiceValues[1]);
    }

    @Test
    void testDiceFace() {
        // Test the diceFace method for all possible values
        assertEquals("1⚀", Dice.diceFace(1));
        assertEquals("2⚁", Dice.diceFace(2));
        assertEquals("3⚂", Dice.diceFace(3));
        assertEquals("4⚃", Dice.diceFace(4));
        assertEquals("5⚄", Dice.diceFace(5));
        assertEquals("6⚅", Dice.diceFace(6));

        // Test invalid dice values (should return an empty string)
        assertEquals("", Dice.diceFace(0));
        assertEquals("", Dice.diceFace(7));
    }

    @Test
    void testDiceFaceInvalidValue() {
        // Test invalid dice values outside the valid range (0 and 7)
        assertEquals("", Dice.diceFace(0));
        assertEquals("", Dice.diceFace(7));
    }
}
