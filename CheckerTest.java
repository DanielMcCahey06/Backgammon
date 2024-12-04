import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
    private Checker blackChecker;
    private Checker whiteChecker;

    @BeforeEach
    void setUp() {
        blackChecker = new Checker(3, Checker.Colour.BLACK);
        whiteChecker = new Checker(5, Checker.Colour.WHITE);
    }

    @Test
    void testInitialPosition() {
        assertEquals(3, blackChecker.getPosition(), "Black checker should have initial position 3.");
        assertEquals(5, whiteChecker.getPosition(), "White checker should have initial position 5.");
    }

    @Test
    void testSetPosition() {
        blackChecker.setPosition(7);
        assertEquals(7, blackChecker.getPosition(), "Black checker position should be updated to 7.");

        whiteChecker.setPosition(2);
        assertEquals(2, whiteChecker.getPosition(), "White checker position should be updated to 2.");
    }

    @Test
    void testGetColour() {
        assertEquals(Checker.Colour.BLACK, blackChecker.getColour(), "Black checker should return BLACK as its colour.");
        assertEquals(Checker.Colour.WHITE, whiteChecker.getColour(), "White checker should return WHITE as its colour.");
    }

    @Test
    void testToString() {
        assertEquals("⚫", blackChecker.toString(), "Black checker should be represented as ⚫.");
        assertEquals("⚪", whiteChecker.toString(), "White checker should be represented as ⚪.");
    }
}
