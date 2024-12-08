import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void isWhite() {
        Player whitePlayer = new Player("Alice", Checker.Colour.WHITE);
        Player blackPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Exhaustive check for all possible checker colors
        assertTrue(whitePlayer.isWhite(), "isWhite should return true for WHITE checker");
        assertFalse(blackPlayer.isWhite(), "isWhite should return false for BLACK checker");
    }

    @Test
    void getName() {
        Player player = new Player("Charlie", Checker.Colour.WHITE);

        // Check name retrieval
        assertEquals("Charlie", player.getName(), "getName should return the correct player name");
    }

    @Test
    void getChecker() {
        Player whitePlayer = new Player("Alice", Checker.Colour.WHITE);
        Player blackPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Exhaustive checks for all enum values
        assertEquals(Checker.Colour.WHITE, whitePlayer.getChecker(), "getChecker should return WHITE for white player");
        assertEquals(Checker.Colour.BLACK, blackPlayer.getChecker(), "getChecker should return BLACK for black player");
    }

    @Test
    void getDisplay() {
        Player whitePlayer = new Player("Alice", Checker.Colour.WHITE);
        Player blackPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Exhaustive checks for all possible outputs
        assertEquals("⚪", whitePlayer.getDisplay(), "getDisplay should return '⚪' for WHITE checker");
        assertEquals("⚫", blackPlayer.getDisplay(), "getDisplay should return '⚫' for BLACK checker");
    }

    @Test
    void getScore() {
        Player player = new Player("Eve", Checker.Colour.WHITE);

        // Edge case: Initial score
        assertEquals(0, player.getScore(), "getScore should return 0 for a new player");
    }

    @Test
    void increaseScore() {
        Player player = new Player("Eve", Checker.Colour.WHITE);

        // Edge case: Positive increment
        player.increaseScore(10);
        assertEquals(10, player.getScore(), "increaseScore should correctly add 10 to the score");

        // Edge case: Cumulative score
        player.increaseScore(5);
        assertEquals(15, player.getScore(), "increaseScore should correctly add 5 to the score, making it 15 total");

        // Edge case: Negative increment
        player.increaseScore(-3);
        assertEquals(12, player.getScore(), "increaseScore should correctly handle negative values, reducing the score");

        // Edge case: Extreme large value
        player.increaseScore(Integer.MAX_VALUE - 12);
        assertEquals(Integer.MAX_VALUE, player.getScore(), "increaseScore should handle large values, reaching Integer.MAX_VALUE");
    }

    @Test
    void constructorEdgeCases() {
        // Edge case: Empty name
        assertThrows(IllegalArgumentException.class, () -> new Player("", Checker.Colour.WHITE), "Constructor should throw exception for empty name");

        // Edge case: Null name
        assertThrows(IllegalArgumentException.class, () -> new Player(null, Checker.Colour.BLACK), "Constructor should throw exception for null name");

        // Edge case: Normal inputs
        Player player = new Player("Daniel", Checker.Colour.WHITE);
        assertEquals("Daniel", player.getName(), "Constructor should initialize name correctly");
        assertEquals(Checker.Colour.WHITE, player.getChecker(), "Constructor should initialize checker color correctly");
    }

    @Test
    void randomTestsForScore() {
        Player player = new Player("RandomTester", Checker.Colour.BLACK);

        // Generate 100 random score increments
        int total = 0;
        for (int i = 0; i < 100; i++) {
            int randomValue = (int) (Math.random() * 100 - 50); // Random values between -50 and 50
            total += randomValue;
            player.increaseScore(randomValue);
        }

        assertEquals(total, player.getScore(), "Random tests should match cumulative score increments");
    }

    @Test
    void edgeCasesWithThresholds() {
        Player player = new Player("ThresholdTester", Checker.Colour.WHITE);

        // Threshold -1, 0, +1
        player.increaseScore(-1);
        assertEquals(-1, player.getScore(), "Score should handle threshold -1 correctly");

        player.increaseScore(1);
        assertEquals(0, player.getScore(), "Score should return to 0 at threshold 0");

        player.increaseScore(1);
        assertEquals(1, player.getScore(), "Score should handle threshold +1 correctly");
    }
}

