import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    @Test
    void addChecker() {
        Pile pile = new Pile();
        Checker checker = new Checker(0, Checker.Colour.WHITE);

        pile.addChecker(checker);
        List<Checker> checkers = pile.getCheckers();

        // Verify the checker was added to the pile
        assertEquals(1, checkers.size(), "Pile should contain one checker after adding");
        assertEquals(checker, checkers.get(0), "The added checker should be at the top of the pile");
    }

    @Test
    void removeTopChecker() {
        Pile pile = new Pile();
        Checker checker1 = new Checker(0, Checker.Colour.WHITE);
        Checker checker2 = new Checker(1, Checker.Colour.BLACK);

        pile.addChecker(checker1);
        pile.addChecker(checker2);

        // Remove the top checker and verify it
        Checker removedChecker = pile.removeTopChecker();
        assertEquals(checker2, removedChecker, "removeTopChecker should return the top checker");
        assertEquals(1, pile.getNoOfCheckers(), "Pile size should decrease by 1 after removing a checker");
        assertEquals(checker1, pile.getCheckers().get(0), "The remaining checker should be the first one added");
    }

    @Test
    void removeTopChecker_EmptyPile() {
        Pile pile = new Pile();

        // Verify exception is thrown when removing from an empty pile
        assertThrows(IndexOutOfBoundsException.class, pile::removeTopChecker, "Removing from an empty pile should throw an exception");
    }

    @Test
    void getNoOfCheckers() {
        Pile pile = new Pile();
        Checker checker1 = new Checker(0, Checker.Colour.WHITE);
        Checker checker2 = new Checker(1, Checker.Colour.BLACK);

        pile.addChecker(checker1);
        pile.addChecker(checker2);

        // Verify the number of checkers in the pile
        assertEquals(2, pile.getNoOfCheckers(), "Pile should have 2 checkers after adding 2");
    }

    @Test
    void getColour() {
        Pile pile = new Pile();
        Checker checker1 = new Checker(0, Checker.Colour.WHITE);
        Checker checker2 = new Checker(1, Checker.Colour.BLACK);

        // Verify color when pile has checkers
        pile.addChecker(checker1);
        assertEquals(Checker.Colour.WHITE, pile.getColour(), "getColour should return the color of the first checker");

        pile.addChecker(checker2);
        assertEquals(Checker.Colour.WHITE, pile.getColour(), "getColour should always return the color of the first checker");

        // Verify color for empty pile
        pile.removeTopChecker();
        pile.removeTopChecker();
        assertNull(pile.getColour(), "getColour should return null for an empty pile");
    }

    @Test
    void get() {
        Pile pile = new Pile();
        Checker checker1 = new Checker(0, Checker.Colour.WHITE);
        Checker checker2 = new Checker(1, Checker.Colour.BLACK);

        pile.addChecker(checker1);
        pile.addChecker(checker2);

        // Verify retrieval of specific checkers
        assertEquals(checker1, pile.get(0), "get should return the first checker added");
        assertEquals(checker2, pile.get(1), "get should return the second checker added");
    }

    @Test
    void get_OutOfBounds() {
        Pile pile = new Pile();
        Checker checker1 = new Checker(0, Checker.Colour.WHITE);

        pile.addChecker(checker1);

        // Verify exception for invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> pile.get(-1), "get should throw exception for negative index");
        assertThrows(IndexOutOfBoundsException.class, () -> pile.get(1), "get should throw exception for index out of bounds");
    }

    @Test
    void emptyPileEdgeCase() {
        Pile pile = new Pile();

        // Edge case: Empty pile
        assertEquals(0, pile.getNoOfCheckers(), "Empty pile should have 0 checkers");
        assertNull(pile.getColour(), "Empty pile should have null color");
    }

    @Test
    void singleCheckerEdgeCase() {
        Pile pile = new Pile();
        Checker checker = new Checker(0, Checker.Colour.WHITE);

        // Edge case: Single checker
        pile.addChecker(checker);
        assertEquals(1, pile.getNoOfCheckers(), "Pile with one checker should report size 1");
        assertEquals(Checker.Colour.WHITE, pile.getColour(), "Pile with one checker should return its color");
    }

    @Test
    void addRemoveStressTest() {
        Pile pile = new Pile();
        int n = 1000;

        // Add 1000 checkers
        for (int i = 0; i < n; i++) {
            pile.addChecker(new Checker(i, i % 2 == 0 ? Checker.Colour.WHITE : Checker.Colour.BLACK));
        }
        assertEquals(n, pile.getNoOfCheckers(), "Pile should contain 1000 checkers after adding");

        // Remove all 1000 checkers
        for (int i = n - 1; i >= 0; i--) {
            pile.removeTopChecker();
        }
        assertEquals(0, pile.getNoOfCheckers(), "Pile should be empty after removing all checkers");
        assertNull(pile.getColour(), "Empty pile should have null color after removing all checkers");
    }
}