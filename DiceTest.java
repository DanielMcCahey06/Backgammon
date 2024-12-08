import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void roll() {
        Dice dice = new Dice();
        int[] diceValues = dice.roll();

        // Assert that the roll produces two values between 1 and 6
        assertNotNull(diceValues, "Dice roll should not return null");
        assertEquals(2, diceValues.length, "Dice roll should return an array of size 2");
        assertTrue(diceValues[0] >= 1 && diceValues[0] <= 6, "First die value should be between 1 and 6");
        assertTrue(diceValues[1] >= 1 && diceValues[1] <= 6, "Second die value should be between 1 and 6");
    }

    @Test
    void diceFace() {
        // Test each possible dice value and ensure correct face representation
        assertEquals("1⚀", Dice.diceFace(1), "Dice face for value 1 should match");
        assertEquals("2⚁", Dice.diceFace(2), "Dice face for value 2 should match");
        assertEquals("3⚂", Dice.diceFace(3), "Dice face for value 3 should match");
        assertEquals("4⚃", Dice.diceFace(4), "Dice face for value 4 should match");
        assertEquals("5⚄", Dice.diceFace(5), "Dice face for value 5 should match");
        assertEquals("6⚅", Dice.diceFace(6), "Dice face for value 6 should match");
        assertEquals("", Dice.diceFace(0), "Dice face for invalid value should be an empty string");
        assertEquals("", Dice.diceFace(7), "Dice face for invalid value should be an empty string");
    }

    @Test
    void setDice() {
        Dice dice = new Dice();
        dice.setDice(3, 5);

        // Assert that the dice values are correctly set
        int[] diceValues = dice.getSetDice();
        assertEquals(3, diceValues[0], "First die value should be set to 3");
        assertEquals(5, diceValues[1], "Second die value should be set to 5");
    }

    @Test
    void getSetDice() {
        Dice dice = new Dice();
        dice.setDice(4, 6);

        // Retrieve and verify the dice values
        int[] diceValues = dice.getSetDice();
        assertNotNull(diceValues, "getSetDice should not return null");
        assertEquals(2, diceValues.length, "getSetDice should return an array of size 2");
        assertEquals(4, diceValues[0], "First die value should be 4");
        assertEquals(6, diceValues[1], "Second die value should be 6");
    }
}