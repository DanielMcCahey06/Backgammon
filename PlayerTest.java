import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player whitePlayer;
    private Player blackPlayer;

    @BeforeEach
    void setUp() {
        whitePlayer = new Player("Alice", Checker.Colour.WHITE);
        blackPlayer = new Player("Bob", Checker.Colour.BLACK);
    }

    @Test
    void testIsWhite() {
        assertTrue(whitePlayer.isWhite());
        assertFalse(blackPlayer.isWhite());
    }

    @Test
    void testGetName() {
        assertEquals("Alice", whitePlayer.getName());
        assertEquals("Bob", blackPlayer.getName());
    }

    @Test
    void testGetChecker() {
        assertEquals(Checker.Colour.WHITE, whitePlayer.getChecker());
        assertEquals(Checker.Colour.BLACK, blackPlayer.getChecker());
    }

    @Test
    void testGetDisplay() {
        assertEquals("⚪", whitePlayer.getDisplay());
        assertEquals("⚫", blackPlayer.getDisplay());
    }

    @Test
    void testGetScore() {
        assertEquals(0, whitePlayer.getScore());
        assertEquals(0, blackPlayer.getScore());
    }

    @Test
    void testIncreaseScore() {
        whitePlayer.increaseScore(10);
        assertEquals(10, whitePlayer.getScore());

        blackPlayer.increaseScore(5);
        assertEquals(5, blackPlayer.getScore());
    }
}
