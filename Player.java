public class Player {
    private final String name;
    private final Checker.Colour checker;
    public Player(String playerName, Checker.Colour checker) {
        this.name = playerName;
        this.checker = checker;
    }
    public String getName() {
        return name;
    }
    public Checker.Colour getChecker() {
        return checker;
    }

    public String getDisplay() {
        return checker == Checker.Colour.WHITE ? "O" : Checker.Black + "@" + Checker.Reset;
    }
}
