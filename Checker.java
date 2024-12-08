public class Checker {

    public enum Colour {
        WHITE, BLACK
    }

    private Colour colour;
    private int position;

    // Constructor
    public Checker(int position, Colour colour) {
        this.position = position;
        this.colour = colour;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    // Getter for checker Colour
    public Colour getColour() {
        return colour;
    }

    public String toString() {
        return colour == Colour.WHITE ? "⚪" : "⚫";
    }

}
