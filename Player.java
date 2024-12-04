import java.util.*;

public class Player {
    private final String name;
    private final Checker.Colour checker;
    private int score; //Players current score

    // Constructor for Player Class
    public Player(String playerName, Checker.Colour checker) {
        this.name = playerName;
        this.checker = checker;
        score = 0; //when player is made their initial score is 0
    }

    public boolean isWhite(){
        return checker.equals(Checker.Colour.WHITE);
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

    //getter to obtain players current score
    public int getScore() {
        return score;
    }

    // function to increase players score by a set amount
    public void increaseScore(int amount) {
        score += amount;
    }
}
