//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Board board = new Board();

        // Adding some checkers for demonstration
        board.addChecker(23, new Checker(Checker.Colour.WHITE));
        board.addChecker(23, new Checker(Checker.Colour.WHITE));
        board.addChecker(18, new Checker(Checker.Colour.BLACK));
        board.addChecker(18, new Checker(Checker.Colour.BLACK));
        board.addChecker(18, new Checker(Checker.Colour.BLACK));
        board.addChecker(18, new Checker(Checker.Colour.BLACK));
        board.addChecker(18, new Checker(Checker.Colour.BLACK));
        board.addChecker(16, new Checker(Checker.Colour.BLACK));
        board.addChecker(16, new Checker(Checker.Colour.BLACK));
        board.addChecker(16, new Checker(Checker.Colour.BLACK));
        board.addChecker(12, new Checker(Checker.Colour.WHITE));
        board.addChecker(12, new Checker(Checker.Colour.WHITE));
        board.addChecker(12, new Checker(Checker.Colour.WHITE));
        board.addChecker(12, new Checker(Checker.Colour.WHITE));
        board.addChecker(12, new Checker(Checker.Colour.WHITE));
        board.addChecker(11, new Checker(Checker.Colour.BLACK));
        board.addChecker(11, new Checker(Checker.Colour.BLACK));
        board.addChecker(11, new Checker(Checker.Colour.BLACK));
        board.addChecker(11, new Checker(Checker.Colour.BLACK));
        board.addChecker(11, new Checker(Checker.Colour.BLACK));
        board.addChecker(7, new Checker(Checker.Colour.WHITE));
        board.addChecker(7, new Checker(Checker.Colour.WHITE));
        board.addChecker(7, new Checker(Checker.Colour.WHITE));
        board.addChecker(5, new Checker(Checker.Colour.WHITE));
        board.addChecker(5, new Checker(Checker.Colour.WHITE));
        board.addChecker(5, new Checker(Checker.Colour.WHITE));
        board.addChecker(5, new Checker(Checker.Colour.WHITE));
        board.addChecker(5, new Checker(Checker.Colour.WHITE));
        board.addChecker(0, new Checker(Checker.Colour.BLACK));
        board.addChecker(0, new Checker(Checker.Colour.BLACK));

        // Display the board
        board.display();
    }
}