import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.awt.Color.WHITE;

public class Game {
    private Dice dice;
    private boolean equalDice = true;
    private boolean running = true;
    private boolean isPlayer1Turn = true;
    public static final String Green = "\u001B[32m";
    public static final String Red = "\u001B[31m";
    public static final String Reset = "\u001B[0m";
    private static final int width = 50;
    public static final String border = "-".repeat(width);
    private static int noOfMoves = 0;



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
        Player player1 = new Player(player1Name, Checker.Colour.WHITE);

        System.out.println("Enter name 2 (Black Checker):");
        String player2Name = scanner.nextLine();
        Player player2 = new Player(player2Name, Checker.Colour.BLACK);

        System.out.println("Now to determine who goes first!");

        while (equalDice) {
            int[] startingRoll = Dice.roll();
            int player1Roll = startingRoll[0];
            int player2Roll = startingRoll[1];
            outputMessage(player1.getName() + " rolls a " + Dice.diceFace(player1Roll) + " and " + player2.getName() + " rolls a " + Dice.diceFace(player2Roll));
            if (player1Roll > player2Roll) {
                outputMessage(player1.getName() + " will go first!");
                equalDice = false;
            } else if (player2Roll > player1Roll) {
                outputMessage(player2.getName() + " will go first!");
                isPlayer1Turn = false;
                equalDice = false;
            } else {
                outputMessage("Dice rolls are equal so will go again!");
            }
        }
        while (running) {
            Player currentPlayer = isPlayer1Turn ? player1 : player2;
            Board.display(isPlayer1Turn);
            /*if (winGame()) {
                outputMessage("Congratulations " + currentPlayer.getName() + " you win!");
            }*/
            outputMessage(currentPlayer.getName() + "'s turn ('" + currentPlayer.getDisplay() + "' Checkers)");
            outputMessage("Enter 'hint' for a list of possible commands");
            String action = scanner.nextLine();// stores user input into 'action'
            if (processAction(action)) {
                isPlayer1Turn = !isPlayer1Turn;
            }
        }
        scanner.close();
    }

    /*private boolean winGame() {
        if(homeIsFull) {
            return true;
        }
        return false;
    }*/

    public static String stripAnsiCodes(String message) {
        return message.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static void errorMessage(String message) {
        int numberOfSpaces = (width - message.length()) / 2; // finds number of spaces needed to center message
        String spaces = " ".repeat(numberOfSpaces);
        System.out.println(Red + border); // creates border in red
        System.out.println(spaces + message + Red); // centers message in red
        System.out.println(border + Reset); // creates border in red and resets the colour back to default
    }

    public static void outputMessage(String message) {
        String formattedMessage = stripAnsiCodes(message); //
        if (formattedMessage.length() <= width) {
            int numberOfSpaces = (width - formattedMessage.length()) / 2; // finds number of spaces needed to center message
            String spaces = " ".repeat(numberOfSpaces);
            System.out.println(Green + border); // creates border in green
            System.out.println(spaces + message + Green); // centers message in green
            System.out.println(border + Reset); // creates border in green and resets the colour back to default
        } else { // method for long messages
            StringBuilder newMessage = new StringBuilder();
            String[] words = formattedMessage.split(" "); // splits words within the message
            int currentLineLength = 0;
            for (String word : words) {
                if (currentLineLength + word.length() > width) {
                    if (currentLineLength > 0) { // creates new line for the message if it exceeds the width
                        newMessage.append("\n");
                    }
                    currentLineLength = 0;
                }
                newMessage.append(word).append(" ");
                currentLineLength += word.length() + 1;
            }
            System.out.println(Green + border); // creates border in green
            for (String line : newMessage.toString().split("\n")) {
                int numberOfSpaces = (width - line.length()) / 2; // finds number of spaces needed to center message
                String spaces = " ".repeat(Math.max(0, numberOfSpaces)); // prevents negative spaces
                System.out.println(spaces + line.trim() + Green); // trims trailing space
            }
            System.out.println(border + Reset); // creates border in green and resets the colour back to default
        }
    }

    private void gameHelp() {
        outputMessage("Enter 'roll' to roll the dice, 'Q' to quit the game, 'pip' to view pip count!");
        System.out.println();
    }

    private boolean processAction(String action) {
        action = action.toUpperCase();
        switch (action) {
            case "HINT" -> {
                gameHelp();
                return true;
            }
            case "Q" -> {
                running = false;
                errorMessage("Game over!");
                return false;
            }
            case "ROLL" -> {
                int[] roll = Dice.roll();
                outputMessage("Dice roll:" + Dice.diceFace(roll[0]) + " " + Dice.diceFace(roll[1]));
                noOfMoves += 1;
                return true;
            }
            case "PIP" -> {
                pipCount();
                return true;
            }
            default -> {
                errorMessage("Invalid command");
                return false;
            }
        }
    }

    private void pipCount() {
        int noOfPipsWhite = 0;
        int noOfPipsBlack = 0;
        for (int i = 0; i < 24; i++) {
                if (Board.piles[i].getColour() == Checker.Colour.WHITE) {
                    noOfPipsWhite += Board.piles[i].getNoOfCheckers() * (i + 1);
                } else {
                    noOfPipsBlack += Board.piles[i].getNoOfCheckers() * (24 - i);
                }
            }
        outputMessage("⚪ Pip count : " + noOfPipsWhite + "  ⚫ Pip count : " + noOfPipsBlack);
    }

    private void possibleMoves(int [] roll) {

    }
}
