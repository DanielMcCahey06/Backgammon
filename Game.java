import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private Dice dice;
    private boolean running = true;

    public Game() {
        initialiseGame();
    }

    private void initialiseGame() {
        Board board = new Board();
        dice = new Dice();

        // add initial checkers
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

        System.out.println("Welcome to Backgammon");
        System.out.println();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in); // allows reading from the console
        System.out.println("Enter name 1:");
        String player1 = scanner.nextLine();
        System.out.println( player1 + " plays with the White Checkers");
        System.out.println("Enter name 2:");
        String player2 = scanner.nextLine();
        System.out.println( player2 + " plays with the Black Checkers");

        while (running) {
            Board.display();
            System.out.println("Enter move:");
            String action = scanner.nextLine();// stores user input into 'action'
            processAction(action);
        }
        scanner.close();
    }

    private void processAction(String action) {
        action = action.toUpperCase();
        if (action.equals("Q")) { // quit game
            running = false;
            System.out.println("Game over!");
        } else if (action.equals("ROLL")) {
            int[] roll = Dice.roll();
            System.out.println("Dice roll:" + Arrays.toString(roll));
        }

    }
}
