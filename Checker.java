public class Checker {

    // Enum representing the possible colors for a checker
    public enum Colour {
        WHITE, BLACK
    }

    private Colour colour; // The color of the checker
    private int position; // The position of the checker on the board

    // Constructor to initialize position and colour
    public Checker(int position, Colour colour) {
        this.position = position; // Set the checker's position
        this.colour = colour; // Set the checker's color
    }

    // Setter for position
    public void setPosition(int position) {
        this.position = position; // Update the checker's position
    }

    // Getter for position
    public int getPosition() {
        return position; // Return the current position of the checker
    }

    // Getter for checker color
    public Colour getColour() {
        return colour; // Return the checker's color
    }

    // Returns a string representation of the checker (⚪ for white, ⚫ for black)
    public String toString() {
        return colour == Colour.WHITE ? "⚪" : "⚫";
    }
}
