import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    void setPosition() {
        Checker checker = new Checker(5, Checker.Colour.WHITE);
        checker.setPosition(10);

        assertEquals(10, checker.getPosition(), "Checker's position should be updated to 10");
    }

    @Test
    void getPosition() {
        Checker checker = new Checker(7, Checker.Colour.BLACK);

        assertEquals(7, checker.getPosition(), "Checker's position should be initialized to 7");
    }

    @Test
    void getColour() {
        Checker whiteChecker = new Checker(3, Checker.Colour.WHITE);
        Checker blackChecker = new Checker(8, Checker.Colour.BLACK);

        assertEquals(Checker.Colour.WHITE, whiteChecker.getColour(), "Checker's color should be WHITE");
        assertEquals(Checker.Colour.BLACK, blackChecker.getColour(), "Checker's color should be BLACK");
    }

    @Test
    void testToString() {
        Checker whiteChecker = new Checker(0, Checker.Colour.WHITE);
        Checker blackChecker = new Checker(1, Checker.Colour.BLACK);

        assertEquals("⚪", whiteChecker.toString(), "White checker's toString should return '⚪'");
        assertEquals("⚫", blackChecker.toString(), "Black checker's toString should return '⚫'");
    }
}
