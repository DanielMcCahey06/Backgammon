import java.util.Scanner;

public class Match {
    public static int pointsToWin; // the amount of points needed to win
    public static boolean playAgain = true; // a flag indicating whether to play another match
    private final Scanner scanner; // Declare the scanner field

    /**
     * Constructs a new {@code Match} object with the provided scanner for user input.
     *
     * @param scanner A {@code Scanner} object for reading user input.
     */
    public Match(Scanner scanner){
        this.scanner = scanner;
    }

    /**
     * Creates a new {@code Game} object for the match.
     * <p>
     * This method can be overridden for custom game behavior.
     *
     * @return A new {@code Game} object.
     */
    protected Game createGame() {
        return new Game();
    }

    /**
     * Starts and manages the match, consisting of multiple games.
     * <p>
     * Players take turns until one player reaches the required number of points to win the match.
     * After the match ends, the user is prompted to play again or quit.
     */
    public void playMatch() {
        while (playAgain) {

            System.out.println("Welcome to Backgammon");
            getMatchPoints();

            // Get player names
            Player player1 = getPlayerDetails(1, Checker.Colour.WHITE);
            Player player2 = getPlayerDetails(2, Checker.Colour.BLACK);

            // Continue playing until a player has enough points to win
            while (player1.getScore() < pointsToWin && player2.getScore() < pointsToWin && !Game.quitGame) {
                Game game = createGame();
                game.start(player1, player2);
            }

            displayMatchWinner(player1, player2);
            askToPlayAgain();
        }
    }

    /**
     * Prompts the user to enter the number of points needed to win the match.
     * <p>
     * Validates the input to ensure it is a positive integer.
     */
    public void getMatchPoints() {
        System.out.println("How many points would you like to play to?");
        boolean validPointsInput = false;

        while (!validPointsInput) {
            if (scanner.hasNextInt()) {
                pointsToWin = scanner.nextInt();
                scanner.nextLine(); // Clear the newline left in the buffer

                if (pointsToWin > 0) {
                    validPointsInput = true;
                } else {
                    System.out.println("Please enter a positive integer for points to win:");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer:");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    /**
     * Prompts the user to enter player details.
     *
     * @param playerNumber The player number (1 or 2).
     * @param colour       The checker color for the player.
     * @return A Player object representing the player.
     */
    public Player getPlayerDetails(int playerNumber, Checker.Colour colour) {
        System.out.println("Enter name " + playerNumber + " (" + colour + " Checker):");
        String playerName = scanner.nextLine();
        return new Player(playerName, colour);
    }

    /**
     * Displays the match winner based on the scores.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public void displayMatchWinner(Player player1, Player player2) {
        if (player1.getScore() >= pointsToWin) {
            Game.outputMessage(player1.getName() + " has won the game! ");
        }
        if (player2.getScore() >= pointsToWin) {
            Game.outputMessage(player2.getName() + " has won the match, congratulations! ");
        }
    }

    /**
     * Asks the user if they want to play another match.
     */
    public void askToPlayAgain() {
        String userResponse;
        boolean validResponse = false;

        System.out.println("Would you like to play again? (Y/N):");
        while (!validResponse) {
            userResponse = scanner.nextLine();
            if (userResponse.equalsIgnoreCase("Y")) {
                validResponse = true;
                playAgain = true;
                Game.quitGame = false;
                Game.gameInPlay = true;
            } else if (userResponse.equalsIgnoreCase("N")) {
                validResponse = true;
                playAgain = false;
            } else {
                System.out.println("Invalid response. Please enter 'Y' or 'N':");
            }
        }
    }
}


