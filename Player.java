import java.util.*;

public class Player {
    private final String name;
    private final Checker.Colour checker;

    // Constructor for Player Class
    public Player(String playerName, Checker.Colour checker) {
        this.name = playerName;
        this.checker = checker;
    }

    public boolean isWhite(){
        return checker.equals(Checker.Colour.WHITE);
    }

    public List<Checker> getCheckers() {
        return Board.getCheckers(checker); // Fetch checkers for the current player
    }

    public String getName() {
        return name;
    }
    public Checker.Colour getChecker() {
        return checker;
    }
    public String getDisplay() {
        return checker == Checker.Colour.WHITE ? "⚪" : "⚫";
    }
}
