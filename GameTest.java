import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
public class GameTest {
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice", Checker.Colour.WHITE);
        player2 = new Player("Bob", Checker.Colour.BLACK);
        game = new Game();
    }

    @Test
    void testInitialiseGame() {
        assertEquals(15, Board.getCheckers(Checker.Colour.WHITE).size(), "Initial setup should have 15 white checkers.");
        assertEquals(15, Board.getCheckers(Checker.Colour.BLACK).size(), "Initial setup should have 15 black checkers.");
    }


    @Test
    void testGenerateLegalMoves() {
        Board.piles[6].addChecker(new Checker(6, Checker.Colour.WHITE));
        List<String> moves = game.generateLegalMoves(player1, new int[]{2, 3}, new boolean[]{false, false}, false);
        assertFalse(moves.isEmpty(), "Legal moves should be generated for valid roll.");
    }

    @Test
    void testGenerateNonDoubleMoves() {
        Board.piles[6].addChecker(new Checker(6, Checker.Colour.WHITE));
        List<String> moves = game.generateLegalMoves(player1, new int[]{2, 3}, new boolean[]{false, false}, false);
        assertTrue(moves.stream().anyMatch(move -> move.contains("6-8")), "Should generate non-double moves.");
    }

    @Test
    void testGenerateDoubleMoves() {
        Board.piles[6].addChecker(new Checker(6, Checker.Colour.WHITE));
        List<String> moves = game.generateLegalMoves(player1, new int[]{3, 3}, new boolean[]{false, false}, true);
        assertTrue(moves.stream().anyMatch(move -> move.contains("6-9")), "Should generate double moves.");
    }

    @Test
    void testIsLegalTarget() {
        assertTrue(game.isLegalTarget(5, Checker.Colour.WHITE), "Position 5 should be legal for WHITE.");
        assertFalse(game.isLegalTarget(24, Checker.Colour.WHITE), "Position 24 should be out of bounds.");
    }

    @Test
    void testProcessMove() {
        Board.piles[0].addChecker(new Checker(0, Checker.Colour.WHITE));
        assertTrue(game.processMove(player1, 1, 4), "Move from 1 to 4 should be valid.");
    }

    @Test
    void testProcessMoveToHome() {
        Board.piles[22].addChecker(new Checker(22, Checker.Colour.WHITE));
        assertTrue(game.processMoveToHome(player1, 23), "Moving to home should succeed.");
    }

    @Test
    void testAllCheckersInHomeRange() {
        assertFalse(game.allCheckersInHomeRange(player1), "Not all checkers should be in home range initially.");
    }

    @Test
    void testMarkUsedDice() {
        boolean[] usedDice = {false, false};
        game.markUsedDice(new int[]{3, 4}, 3, usedDice);
        assertTrue(usedDice[0], "Dice with value 3 should be marked used.");
    }

    @Test
    void testGetWinCondition() {
        Board.home[Board.PLAYER1HOMEINDEX].addChecker(new Checker(0, Checker.Colour.WHITE));
        assertEquals(1, game.getWinCondition(player1, player2), "Should return single win multiplier.");
        Board.bar[Board.PLAYER2BARINDEX].addChecker(new Checker(0, Checker.Colour.BLACK));
        assertEquals(2, game.getWinCondition(player1, player2), "Should return Gammon win multiplier.");
        Board.piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        assertEquals(3, game.getWinCondition(player1, player2), "Should return Backgammon win multiplier.");
    }

    @Test
    void testIsGameWon() {
        Board.home[Board.PLAYER1HOMEINDEX].addChecker(new Checker(0, Checker.Colour.WHITE));
        assertTrue(game.isGameWon(player1), "Game should be won when all checkers are in home.");
    }

    @Test
    void testIsValidDicePair() {
        assertTrue(game.isValidDicePair(new String[]{"4", "5"}), "Dice pair '4 5' should be valid.");
        assertFalse(game.isValidDicePair(new String[]{"0", "7"}), "Dice pair '0 7' should be invalid.");
    }

    @Test
    void testPipCount() {
        game.pipCount(); // Ensures the method runs without errors.
    }
}
