import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void addObserver() {
        Board board = new Board();
        MockObserver observer = new MockObserver();
        board.addObserver(observer);

        // Notify observers and check if the observer's update method was triggered
        board.notifyObservers();
        assertTrue(observer.wasUpdated, "Observer should have been notified");
    }

    @Test
    void notifyObservers() {
        Board board = new Board();
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();

        board.addObserver(observer1);
        board.addObserver(observer2);

        board.notifyObservers();
        assertTrue(observer1.wasUpdated, "Observer 1 should have been notified");
        assertTrue(observer2.wasUpdated, "Observer 2 should have been notified");
    }

    @Test
    void getPiles() {
        Board board = new Board();
        assertNotNull(board.getPiles(), "Piles array should not be null");
        assertEquals(24, board.getPiles().length, "Piles array should have 24 elements");
    }

    @Test
    void getPile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getPile(0), "Should not throw exception for valid index");
        assertThrows(IllegalArgumentException.class, () -> board.getPile(-1), "Should throw exception for negative index");
        assertThrows(IllegalArgumentException.class, () -> board.getPile(24), "Should throw exception for index >= 24");
    }

    @Test
    void getHomePile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getHomePile(0), "Should not throw exception for valid index");
        assertThrows(IllegalArgumentException.class, () -> board.getHomePile(-1), "Should throw exception for negative index");
        assertThrows(IllegalArgumentException.class, () -> board.getHomePile(2), "Should throw exception for index >= 2");
    }

    @Test
    void getBarPile() {
        Board board = new Board();
        assertDoesNotThrow(() -> board.getBarPile(0), "Should not throw exception for valid index");
        assertThrows(IllegalArgumentException.class, () -> board.getBarPile(-1), "Should throw exception for negative index");
        assertThrows(IllegalArgumentException.class, () -> board.getBarPile(2), "Should throw exception for index >= 2");
    }

    @Test
    void checkWinCondition() {
        Board board = new Board();
        String playerName = "Daniel";
        Player player = new Player(playerName ,Checker.Colour.WHITE);

        // Fill the player's home pile with 15 checkers
        for (int i = 0; i < Board.TOTAL_CHECKERS; i++) {
            board.getHomePile(Board.PLAYER1HOMEINDEX).addChecker(new Checker(0, Checker.Colour.WHITE));
        }

        assertTrue(board.checkWinCondition(player), "Player should meet win condition");
    }

    @Test
    void isLegalMove() {
        Board board = new Board();

        // Empty pile case
        assertTrue(board.isLegalMove(1, Checker.Colour.WHITE), "Move should be legal for an empty pile");

        // Same color case
        board.getPile(1).addChecker(new Checker(1, Checker.Colour.WHITE));
        assertTrue(board.isLegalMove(1, Checker.Colour.WHITE), "Move should be legal for pile with same color checkers");

        // Different color with one checker
        board.getPile(2).addChecker(new Checker(2, Checker.Colour.BLACK));
        assertTrue(board.isLegalMove(2, Checker.Colour.WHITE), "Move should be legal for pile with one checker of a different color");

        // Different color with two checkers
        board.getPile(3).addChecker(new Checker(3, Checker.Colour.BLACK));
        board.getPile(3).addChecker(new Checker(3, Checker.Colour.BLACK));
        assertFalse(board.isLegalMove(3, Checker.Colour.WHITE), "Move should not be legal for pile with two or more checkers of a different color");
    }

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
        assertEquals(5, maxHeights[0], "Max height of piles should be 2");
        assertEquals(1, maxHeights[1], "Max height of bar should be 1");
        assertEquals(2, maxHeights[2], "Max height of home should be 2");
    }

    // MockObserver for testing observer functionality
    static class MockObserver implements Observer {
        boolean wasUpdated = false;

        @Override
        public void update(Board board) {
            wasUpdated = true;
        }
    }
}
