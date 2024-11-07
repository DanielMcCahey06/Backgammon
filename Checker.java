public class Checker {

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

        return colour == Colour.WHITE ? "⚪" : "⚫";
}

}