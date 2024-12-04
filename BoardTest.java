import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        board = new Board();
        player1 = new Player("Player1", Checker.Colour.WHITE);
        player2 = new Player("Player2", Checker.Colour.BLACK);
    }

    @Test
    void testAddChecker() {
        Checker checker = new Checker(0, Checker.Colour.WHITE);
        board.addChecker(0, checker);
        assertEquals(1, Board.piles[0].getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, Board.piles[0].getCheckers().get(0).getColour());
    }

    @Test
    void testRemoveChecker() {
        Checker checker = new Checker(0, Checker.Colour.WHITE);
        board.addChecker(0, checker);
        board.removeChecker(0);
        assertEquals(0, Board.piles[0].getNoOfCheckers());
    }

    @Test
    void testAddBarChecker() {
        Checker checker = new Checker(0, Checker.Colour.BLACK);
        board.addBarChecker(Board.PLAYER2BARINDEX, checker);
        assertEquals(1, Board.bar[Board.PLAYER2BARINDEX].getNoOfCheckers());
        assertEquals(Checker.Colour.BLACK, Board.bar[Board.PLAYER2BARINDEX].getCheckers().get(0).getColour());
    }

    @Test
    void testRemoveBarChecker() {
        Checker checker = new Checker(0, Checker.Colour.BLACK);
        board.addBarChecker(Board.PLAYER2BARINDEX, checker);
        board.removeBarChecker(Board.PLAYER2BARINDEX);
        assertEquals(0, Board.bar[Board.PLAYER2BARINDEX].getNoOfCheckers());
    }

    @Test
    void testAddHomeChecker() {
        Checker checker = new Checker(0, Checker.Colour.WHITE);
        board.addHomeChecker(Board.PLAYER1HOMEINDEX, checker);
        assertEquals(1, Board.home[Board.PLAYER1HOMEINDEX].getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, Board.home[Board.PLAYER1HOMEINDEX].getCheckers().get(0).getColour());
    }

    @Test
    void testCheckWinCondition() {
        for (int i = 0; i < Board.TOTAL_CHECKERS; i++) {
            board.addHomeChecker(Board.PLAYER1HOMEINDEX, new Checker(0, Checker.Colour.WHITE));
        }
        assertTrue(board.checkWinCondition(player1));
        assertFalse(board.checkWinCondition(player2));
    }

    @Test
    void testIsLegalMoveEmptyPile() {
        assertTrue(board.isLegalMove(0, Checker.Colour.WHITE));
    }

    @Test
    void testIsLegalMoveSameColour() {
        board.addChecker(0, new Checker(0, Checker.Colour.WHITE));
        assertTrue(board.isLegalMove(0, Checker.Colour.WHITE));
    }

    @Test
    void testIsLegalMoveDifferentColourWithOneChecker() {
        board.addChecker(0, new Checker(0, Checker.Colour.BLACK));
        assertTrue(board.isLegalMove(0, Checker.Colour.WHITE));
    }

    @Test
    void testIsLegalMoveDifferentColourWithMultipleCheckers() {
        board.addChecker(0, new Checker(0, Checker.Colour.BLACK));
        board.addChecker(0, new Checker(0, Checker.Colour.BLACK));
        assertFalse(board.isLegalMove(0, Checker.Colour.WHITE));
    }

    @Test
    void testGetCheckersByColour() {
        Checker whiteChecker = new Checker(0, Checker.Colour.WHITE);
        Checker blackChecker = new Checker(0, Checker.Colour.BLACK);
        board.addChecker(0, whiteChecker);
        board.addChecker(1, blackChecker);

        List<Checker> whiteCheckers = Board.getCheckers(Checker.Colour.WHITE);
        List<Checker> blackCheckers = Board.getCheckers(Checker.Colour.BLACK);

        assertEquals(1, whiteCheckers.size(), "Should return 1 white checker.");
        assertEquals(1, blackCheckers.size(), "Should return 1 black checker.");
    }
}
