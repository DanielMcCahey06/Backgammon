import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    // Tests the addObserver method to ensure observers are added correctly
    @Test
    void addObserver() {
        Board board = new Board(); // Create a new Board instance
        MockObserver observer = new MockObserver(); // Create a MockObserver instance
        board.addObserver(observer); // Add the observer to the board

        board.notifyObservers(); // Notify all observers
        assertTrue(observer.wasUpdated, "Observer should have been notified"); // Check if observer was updated
    }

    // Tests the notifyObservers method to ensure all observers are notified
    @Test
    void notifyObservers() {
        Board board = new Board();
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();

        board.addObserver(observer1); // Add observer1
        board.addObserver(observer2); // Add observer2

        board.notifyObservers(); // Notify all observers
        assertTrue(observer1.wasUpdated, "Observer 1 should have been notified"); // Check if observer1 was updated
        assertTrue(observer2.wasUpdated, "Observer 2 should have been notified"); // Check if observer2 was updated
    }

    // Tests the getPiles method to ensure it returns the correct piles array
    @Test
    void getPiles() {
        Board board = new Board();
        assertNotNull(board.getPiles(), "Piles array should not be null"); // Ensure piles are not null
        assertEquals(24, board.getPiles().length, "Piles array should have 24 elements"); // Ensure piles array has 24 elements
    }

    // Tests the getPile method for valid and invalid indices
    @Test
    void getPile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getPile(0), "Should not throw exception for valid index"); // Test valid index
        assertThrows(IllegalArgumentException.class, () -> board.getPile(-1), "Should throw exception for negative index"); // Test invalid index (-1)
        assertThrows(IllegalArgumentException.class, () -> board.getPile(24), "Should throw exception for index >= 24"); // Test invalid index (24)
    }

    // Tests the getHomePile method for valid and invalid indices
    @Test
    void getHomePile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getHomePile(0), "Should not throw exception for valid index"); // Test valid index
        assertThrows(IllegalArgumentException.class, () -> board.getHomePile(-1), "Should throw exception for negative index"); // Test invalid index (-1)
        assertThrows(IllegalArgumentException.class, () -> board.getHomePile(2), "Should throw exception for index >= 2"); // Test invalid index (2)
    }

    // Tests the getBarPile method for valid and invalid indices
    @Test
    void getBarPile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getBarPile(0), "Should not throw exception for valid index"); // Test valid index
        assertThrows(IllegalArgumentException.class, () -> board.getBarPile(-1), "Should throw exception for negative index"); // Test invalid index (-1)
        assertThrows(IllegalArgumentException.class, () -> board.getBarPile(2), "Should throw exception for index >= 2"); // Test invalid index (2)
    }

    // Tests the checkWinCondition method to ensure the win condition is correctly evaluated
    @Test
    void checkWinCondition() {
        Board board = new Board();
        String playerName = "Daniel";
        Player player = new Player(playerName, Checker.Colour.WHITE);

        // Fill the player's home pile with 15 checkers
        for (int i = 0; i < Board.TOTAL_CHECKERS; i++) {
            board.getHomePile(Board.PLAYER1HOMEINDEX).addChecker(new Checker(0, Checker.Colour.WHITE));
        }

        assertTrue(board.checkWinCondition(player), "Player should meet win condition"); // Check if the win condition is met
    }

    // Tests the isLegalMove method to ensure moves are correctly validated
    @Test
    void isLegalMove() {
        Board board = new Board();

        // Test for empty pile (should be legal)
        assertTrue(board.isLegalMove(1, Checker.Colour.WHITE), "Move should be legal for an empty pile");

        // Test for same color checker (should be legal)
        board.getPile(1).addChecker(new Checker(1, Checker.Colour.WHITE));
        assertTrue(board.isLegalMove(1, Checker.Colour.WHITE), "Move should be legal for pile with same color checkers");

        // Test for different color with one checker (should be legal)
        board.getPile(2).addChecker(new Checker(2, Checker.Colour.BLACK));
        assertTrue(board.isLegalMove(2, Checker.Colour.WHITE), "Move should be legal for pile with one checker of a different color");

        // Test for different color with two checkers (should not be legal)
        board.getPile(3).addChecker(new Checker(3, Checker.Colour.BLACK));
        board.getPile(3).addChecker(new Checker(3, Checker.Colour.BLACK));
        assertFalse(board.isLegalMove(3, Checker.Colour.WHITE), "Move should not be legal for pile with two or more checkers of a different color");
    }

    // Tests the getMaxHeight method to ensure it returns the correct maximum heights for piles, bar, and home
    @Test
    void getMaxHeight() {
        Board board = new Board();

        // Simulate adding checkers to piles, bar, and home
        board.getPile(0).addChecker(new Checker(0, Checker.Colour.WHITE));
        board.getPile(0).addChecker(new Checker(0, Checker.Colour.WHITE));
        board.getBarPile(Board.PLAYER1BARINDEX).addChecker(new Checker(0, Checker.Colour.WHITE));
        board.getHomePile(Board.PLAYER1HOMEINDEX).addChecker(new Checker(0, Checker.Colour.WHITE));
        board.getHomePile(Board.PLAYER1HOMEINDEX).addChecker(new Checker(0, Checker.Colour.WHITE));

        int[] maxHeights = board.getMaxHeight();
        assertEquals(5, maxHeights[0], "Max height of piles should be 2"); // Max height of piles
        assertEquals(1, maxHeights[1], "Max height of bar should be 1"); // Max height of bar
        assertEquals(2, maxHeights[2], "Max height of home should be 2"); // Max height of home
    }

    // MockObserver for testing observer functionality
    static class MockObserver implements Observer {
        boolean wasUpdated = false;

        @Override
        public void update(Board board) {
            wasUpdated = true; // Set flag to true when update is called
        }
    }
}
