import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class PileTest {
    private Pile pile;
    private Checker blackChecker;
    private Checker whiteChecker;

    @BeforeEach
    void setUp() {
        pile = new Pile();
        blackChecker = new Checker(1, Checker.Colour.BLACK);
        whiteChecker = new Checker(2, Checker.Colour.WHITE);
    }

    @Test
    void getCheckers() {
        pile.addChecker(blackChecker);
        pile.addChecker(whiteChecker);
        List<Checker> checkers = pile.getCheckers();
        assertEquals(2, checkers.size());
        assertEquals(blackChecker, checkers.get(0));
        assertEquals(whiteChecker, checkers.get(1));
    }

    @Test
    void addChecker() {
        pile.addChecker(blackChecker);
        assertEquals(1, pile.getNoOfCheckers());
        assertEquals(blackChecker, pile.get(0));
    }

    @Test
    void removeTopChecker() {
        pile.addChecker(blackChecker);
        pile.addChecker(whiteChecker);
        Checker removedChecker = pile.removeTopChecker();
        assertEquals(whiteChecker, removedChecker);
        assertEquals(1, pile.getNoOfCheckers());
    }

    @Test
    void getNoOfCheckers() {
        assertEquals(0, pile.getNoOfCheckers());
        pile.addChecker(blackChecker);
        assertEquals(1, pile.getNoOfCheckers());
        pile.addChecker(whiteChecker);
        assertEquals(2, pile.getNoOfCheckers());
    }

    @Test
    void getColour() {
        assertNull(pile.getColour());
        pile.addChecker(blackChecker);
        assertEquals(Checker.Colour.BLACK, pile.getColour());
        pile.addChecker(whiteChecker);  // Even after adding white, top colour should still be black.
        assertEquals(Checker.Colour.BLACK, pile.getColour());
    }

    @Test
    void get() {
        pile.addChecker(blackChecker);
        pile.addChecker(whiteChecker);
        assertEquals(blackChecker, pile.get(0));
        assertEquals(whiteChecker, pile.get(1));
    }
}
