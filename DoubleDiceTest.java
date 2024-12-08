import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DoubleDiceTest {

    private DoubleDice dice;

    @BeforeEach
    void setUp() {
        // Create a new instance of DoubleDice before each test
        dice = new DoubleDice();
    }

    @Test
    @DisplayName("Checks that an object has been created.")
    void testConstructor() {
        // Ensure the object is not null
        assertNotNull(dice, "The DoubleDice object should not be null after instantiation.");
        // Check initial values
        assertEquals(1, DoubleDice.getDouble(), "Initial multiplier should be 1.");
        assertEquals(0, DoubleDice.getOwner(), "Initial owner should be 0 (no owner).");
    }

    @Test
    @DisplayName("Gets the current double multiplier.")
    void testGetDouble() {
        // Verify the initial multiplier
        assertEquals(1, DoubleDice.getDouble(), "The initial multiplier should be 1.");
    }

    @Test
    @DisplayName("Increases the double multiplier correctly.")
    void testIncreaseDouble() {
        // Test increasing the multiplier
        dice.increaseDouble();
        assertEquals(2, DoubleDice.getDouble(), "Multiplier should double to 2 after one increase.");

        // Increase multiple times and check
        dice.increaseDouble(); // 4
        dice.increaseDouble(); // 8
        assertEquals(8, DoubleDice.getDouble(), "Multiplier should double correctly to 8.");

        // Test max limit of 32
        for (int i = 0; i < 5; i++) {
            dice.increaseDouble();
        }
        assertEquals(32, DoubleDice.getDouble(), "Multiplier should cap at 32.");

        // Try increasing beyond 32
        dice.increaseDouble();
        assertEquals(32, DoubleDice.getDouble(), "Multiplier should remain at 32 after exceeding the max limit.");
    }

    @Test
    @DisplayName("Sets the owner correctly.")
    void testSetOwner() {
        // Set owner to player 1
        dice.setOwner(1);
        assertEquals(1, DoubleDice.getOwner(), "Owner should be set to 1.");

        // Set owner to player 2
        dice.setOwner(2);
        assertEquals(2, DoubleDice.getOwner(), "Owner should be set to 2.");
    }

    @Test
    @DisplayName("Gets the current owner correctly.")
    void testGetOwner() {
        // Ensure the default owner is 0
        assertEquals(0, DoubleDice.getOwner(), "Initial owner should be 0.");

        // Change the owner and verify
        dice.setOwner(1);
        assertEquals(1, DoubleDice.getOwner(), "Owner should return 1 after being set.");

        dice.setOwner(2);
        assertEquals(2, DoubleDice.getOwner(), "Owner should return 2 after being set.");
    }
}