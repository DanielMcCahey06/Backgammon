import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.awt.Color.WHITE;

public class Game {
    private Dice dice;
    private Player player1;
    private Player player2;
    private boolean running = true;
    private boolean isPlayer1Turn = true;

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
        System.out.println("Enter name 1 (White Checker):");
        String player1Name = scanner.nextLine();
        player1 = new Player(player1Name, Checker.Colour.WHITE);

        System.out.println("Enter name 2 (Black Checker):");
        String player2Name = scanner.nextLine();
        player2 = new Player(player2Name, Checker.Colour.BLACK);

        while (running) {
            Board.display();
            Player currentPlayer = isPlayer1Turn ? player1 : player2;
            System.out.println(currentPlayer.getName() + "'s turn ('" + currentPlayer.getDisplay() + "' Checkers)");
            System.out.println("Enter 'Roll' to roll the dices, or 'Q' to quit the game:");
            String action = scanner.nextLine();// stores user input into 'action'
            if (processAction(action)) {
                isPlayer1Turn = !isPlayer1Turn;
            }
        }
        scanner.close();
    }

    private boolean processAction(String action) {
        action = action.toUpperCase();
        if (action.equals("Q")) { // quit game
            running = false;
            System.out.println("Game over!");
            return false;
        } else if (action.equals("ROLL")) {
            int[] roll = Dice.roll();
            System.out.println("Dice roll:" + Arrays.toString(roll));
            return true;
        } else {
            System.out.println("Invalid action");
            return false;
        }
    }
}
