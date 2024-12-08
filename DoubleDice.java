public class DoubleDice {
    private static int multiplier; // Current double amount
    private static int owner; // Represents who currently owns the double dice (1 or 2)

    // Constructor initializes the multiplier and owner
    public DoubleDice() {
        multiplier = 1; // Initial multiplier is 1
        owner = 0; // 0 signifies the dice has no owner yet
    }

    // Getter to return the current multiplier value
    public static int getDouble() {
        return multiplier;
    }

    // Setter to manually set the multiplier value
    public void setDouble(int d) {
        multiplier = d;
    }

    // Increases the multiplier by a factor of 2, up to a maximum of 32
    public void increaseDouble() {
        if (multiplier == 32) {
            System.out.println("The double amount can't be increased further, remains at 32"); // Max multiplier reached
        } else {
            multiplier = multiplier * 2; // Double the multiplier
        }
    }

    // Sets the owner of the double dice (1 for player 1, 2 for player 2)
    public void setOwner(int newOwner) {
        owner = newOwner;
    }

    // Getter to return the current owner of the double dice
    public static int getOwner() {
        return owner;
    }
}
