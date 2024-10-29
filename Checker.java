public class Checker {
    private static final String Black = "\u001B[30m";
    public static final String Reset = "\u001B[0m";


    public enum Colour {
        WHITE, BLACK
    }

    private Colour colour;

    public Checker(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public String toString() {
        return colour == Colour.WHITE ? "O" : Black + "@" + Reset;
    }

}
