import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoubleDiceTest {
    private DoubleDice doubleDice;

    @BeforeEach
    void setUp() {
        doubleDice = new DoubleDice();
    }

    @Test
    void testInitialMultiplier() {
        assertEquals(1, DoubleDice.getDouble(), "Initial multiplier should be 1.");
    }

    @Test
    void testInitialOwner() {
        assertEquals(0, DoubleDice.getOwner(), "Initial owner should be 0 (no owner).");
    }

    @Test
    void testIncreaseDouble() {
        doubleDice.increaseDouble();
        assertEquals(2, DoubleDice.getDouble(), "Multiplier should double to 2.");

        doubleDice.increaseDouble();
        assertEquals(4, DoubleDice.getDouble(), "Multiplier should double to 4.");

        for (int i = 0; i < 4; i++) {
            doubleDice.increaseDouble();
        }
        assertEquals(32, DoubleDice.getDouble(), "Multiplier should reach 32.");

        doubleDice.increaseDouble();
        assertEquals(32, DoubleDice.getDouble(), "Multiplier should remain 32 when maxed out.");
    }

    @Test
    void testSetOwner() {
        doubleDice.setOwner(1);
        assertEquals(1, DoubleDice.getOwner(), "Owner should be set to 1 (player 1).");

        doubleDice.setOwner(2);
        assertEquals(2, DoubleDice.getOwner(), "Owner should be set to 2 (player 2).");
    }
}
